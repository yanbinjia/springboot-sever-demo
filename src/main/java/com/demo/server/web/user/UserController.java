package com.demo.server.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.entity.UserInfo;
import com.demo.server.bean.response.Result;
import com.demo.server.service.user.UserInfoService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserInfoService userInfoService;

	@GetMapping("/view")
	@ResponseBody
	public Result<UserInfo> view(@RequestParam Integer id) {
		UserInfo result = userInfoService.getUserById(id);
		return new Result<>(result);
	}

}
