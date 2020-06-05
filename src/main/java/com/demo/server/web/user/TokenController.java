package com.demo.server.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.response.Result;
import com.demo.server.bean.vo.Token;
import com.demo.server.interceptor.SignPass;
import com.demo.server.interceptor.TokenPass;
import com.demo.server.service.security.TokenService;

@RestController
@RequestMapping("/user")
public class TokenController {

	@Autowired
	private TokenService tokenService;

	@SignPass
	@TokenPass
	@GetMapping("/createToken")
	@ResponseBody
	public Result<Token> createToken(@RequestParam String userId) {

		Token token = tokenService.createToken(userId);

		return new Result<>(token);
	}

	@SignPass
	@TokenPass
	@GetMapping("/refreshToken")
	@ResponseBody
	public Result<Token> refreshToken(@RequestParam String userId, @RequestParam String refreshToken) {

		Token token = tokenService.refreshToken(userId, refreshToken);

		return new Result<>(token);
	}

}
