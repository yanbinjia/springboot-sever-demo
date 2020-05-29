package com.demo.server.web.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.bean.response.Result;
import com.demo.server.common.exception.AppException;

@RestController
@RequestMapping("/")
public class PingController {

	@GetMapping("/ping")
	@ResponseBody
	public Result<String> ping() {
		Result<String> result = new Result<>("pong");
		return result;
	}

	@GetMapping("/pingerror")
	@ResponseBody
	public Result<String> pingError() {

		throw new AppException("pingerror.");

	}

}
