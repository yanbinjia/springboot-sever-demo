package com.demo.server.web.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.base.Result;
import com.demo.server.common.exception.AppException;
import com.demo.server.interceptor.SignPass;
import com.demo.server.interceptor.TokenPass;

@RestController
@RequestMapping("/")
public class PingController {

	@SignPass
	@TokenPass()
	@GetMapping("/ping")
	@ResponseBody
	public Result<String> ping() {
		Result<String> result = new Result<>("pong");
		return result;
	}

	@SignPass
	@TokenPass()
	@GetMapping("/pingerror")
	@ResponseBody
	public Result<String> pingError() {
		// 测试Controller异常捕获和日志记录
		throw new AppException("pingerror.");

	}

}
