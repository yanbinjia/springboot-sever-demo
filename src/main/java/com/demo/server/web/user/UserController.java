package com.demo.server.web.user;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.entity.UserInfo;
import com.demo.server.bean.vo.UserInfoParam;
import com.demo.server.bean.vo.UserInfoResult;
import com.demo.server.service.user.UserInfoService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserInfoService userInfoService;

	// 条件查询,分页列表
	@GetMapping("/page")
	@ResponseBody
	public Result<PageInfo<UserInfoResult>> list(UserInfoParam userInfoParam) {
		PageInfo<UserInfoResult> result = userInfoService.listByCondition(userInfoParam);
		return new Result<>(result);
	}

	@GetMapping("/view")
	@ResponseBody
	public Result<UserInfo> view(@RequestParam Long id) {
		UserInfo result = userInfoService.getById(id);
		return new Result<>(result);
	}

	@PostMapping("/add")
	@ResponseBody // @RequestBody 请求body {"userName":"121231323","password":"ssssss","email":"ssss@ssss.com","mobile":"456"}
	public Result<Boolean> add(@RequestBody @Validated UserInfo userInfo) {
		Boolean result = userInfoService.saveUser(userInfo);
		return new Result<>(result);
	}

	@PostMapping("/update")
	@ResponseBody
	public Result<Boolean> update(@RequestBody @Validated UserInfo userInfo) {
		if (Objects.isNull(userInfo.getId())) {
			return new Result<>(ResultCode.PARAM_ERROR.code, "id不能为空");
		}
		Boolean result = userInfoService.updateUser(userInfo);
		return new Result<>(result);
	}

	@PostMapping("/del")
	@ResponseBody
	public Result<Boolean> delete(@RequestParam Long id) {
		Boolean result = userInfoService.deleteUserById(id);
		return new Result<>(result);
	}

}
