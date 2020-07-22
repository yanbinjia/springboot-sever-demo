package com.demo.server.web.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.TokenPass;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test/header")
@Slf4j
public class TestHeaderController {
	@TokenPass
	@GetMapping("/test")
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
	}

	@TokenPass
	@GetMapping("/getToken") // 请求头中获取Authorization，不是必须参数
	public Result<String> getToken(@RequestHeader(name = "Authorization", required = false) String token) {
		Result<String> result = new Result<String>(ResultCode.SUCCESS);
		result.setData("Authorization:" + token);
		return result;
	}

	@TokenPass
	@GetMapping("/getTokenDefault") // 请求头中获取Authorization，不是必须参数
	public Result<String> getTokenDefault(
			@RequestHeader(name = "Authorization", required = false, defaultValue = "DefaultValue") String token) {
		Result<String> result = new Result<String>(ResultCode.SUCCESS);
		result.setData("Authorization:" + token);
		return result;
	}

	@TokenPass
	@GetMapping("/getTokenRequired") // 请求头中获取Authorization，必须参数，如果没有会 MissingRequestHeaderException
	public Result<String> getTokenRequired(@RequestHeader("Authorization") String token) {
		Result<String> result = new Result<String>(ResultCode.SUCCESS);
		result.setData("Authorization:" + token);
		return result;
	}

	@TokenPass
	@GetMapping("/getAllHeaderByHttpHeaders") // 将 headers 作为 HttpHeaders 对象获取
	public Result<String> getAllHeaderByHttpHeaders(@RequestHeader HttpHeaders headers) {
		Result<String> result = new Result<String>(ResultCode.SUCCESS);

		InetSocketAddress host = headers.getHost();
		String url = "http://" + host.getHostName() + ":" + host.getPort();
		log.info("getAllHeader base-url={}", url);

		result.setData(headers.toString());
		return result;
	}

	@TokenPass
	@GetMapping("/getAllHeaderByMap")
	// 以 Map 形式获取头信息
	// 如果使用Map，并且其中一个头信息具有多个值，则只能获得第一个值。这等效于在 MultiValueMap 上使用getFirst() 方法。
	public Result<String> getAllHeaderByMap(@RequestHeader Map<String, String> headers) {

		Result<String> result = new Result<String>(ResultCode.SUCCESS);

		headers.forEach((key, value) -> {
			log.info(String.format("Header '%s' = %s", key, value));
		});

		result.setData(headers.toString());

		return result;
	}

	@TokenPass
	@GetMapping("/getAllHeaderByMVMap") // 以 MultiValueMap 进行获取头信息
	public Result<String> getAllHeaderByMVMap(@RequestHeader MultiValueMap<String, String> headers) {

		Result<String> result = new Result<String>(ResultCode.SUCCESS);

		headers.forEach((key, value) -> {
			log.info(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
		});

		result.setData(headers.toString());

		return result;
	}

}
