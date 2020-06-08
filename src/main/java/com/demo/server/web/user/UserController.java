package com.demo.server.web.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.entity.UserInfo;
import com.demo.server.bean.vo.UserInfoParam;
import com.demo.server.bean.vo.UserInfoResult;
import com.demo.server.interceptor.SignPass;
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
	public Result<UserInfo> view(@RequestParam Integer id) {
		UserInfo result = userInfoService.getById(id);
		return new Result<>(result);
	}

	@SignPass
	@GetMapping("/getAll")
	@ResponseBody
	public Result<List<UserInfo>> getAll() {
		List<UserInfo> result = userInfoService.getAll();
		return new Result<>(result);
	}

	@GetMapping("/listAll")
	@ResponseBody
	public Result<List<UserInfo>> listAll() {
		List<UserInfo> result = userInfoService.listAll();
		return new Result<>(result);
	}

	@GetMapping("/getByMobile")
	@ResponseBody
	public Result<UserInfo> getByMobile(@RequestParam String mobile) {
		UserInfo result = userInfoService.getByMobile(mobile);
		return new Result<>(result);
	}

}
