package com.demo.server.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.vo.Token;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.service.base.security.TokenService;

@RestController
@RequestMapping(path = { "/user", "/users" }, method = { RequestMethod.POST, RequestMethod.GET })
// path可以配置多个,如例子2个路径都mapping到
// 该path的全局配置支持GET&POST,path下都生效
public class TokenController {

	@Autowired
	private TokenService tokenService;

	@SignPass
	@TokenPass
	@RequestMapping(path = { "/createToken" }, method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Result<Token> createToken(@RequestParam String userId) {

		Token token = tokenService.createToken(userId);

		return new Result<>(token);
	}

	@SignPass
	@TokenPass
	@PostMapping("/refreshToken") // 支持POST
	@ResponseBody
	public Result<Token> refreshToken(@RequestParam String userId, @RequestParam String refreshToken) {

		Token token = tokenService.refreshToken(userId, refreshToken);

		return new Result<>(token);
	}

}
