package com.demo.server.web.test;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.interceptor.TokenPass;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test/req")
@Slf4j
public class TestReqRespController {
	@TokenPass
	@GetMapping("/test") // RequestContextHolder 中获取 request 和 response
	public void testRequestResponse() {
		// 获得request对象,response对象
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpServletResponse response = attributes.getResponse();

		long ctm = System.currentTimeMillis();
		response.setHeader("testrr", "testrr:" + ctm);

		StringBuilder headerStrSB = new StringBuilder();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			headerStrSB.append(name);
			headerStrSB.append(":");
			headerStrSB.append(value);
			headerStrSB.append(";");
		}

		try {
			response.getWriter().write(headerStrSB.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("testRequestResponse ok.");
	}

	@TokenPass
	@GetMapping("/test2") // 方法参数中传递request和response
	public Result<String> testRequestResponse(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "testId", required = false, defaultValue = "defaultValue") String id) {
		Result<String> result = new Result<String>(ResultCode.SUCCESS);

		String tokenHeader = request.getHeader("Authorization");
		result.setData("testId=" + id + ",token=" + tokenHeader);

		return result;
	}

}
