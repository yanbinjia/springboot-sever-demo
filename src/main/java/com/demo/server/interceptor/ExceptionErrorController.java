package com.demo.server.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;

@RestController
public class ExceptionErrorController implements ErrorController {

	final String errorDefaultPath = "/error";

	@Override
	public String getErrorPath() {
		return errorDefaultPath;
	}

	@TokenPass
	@SignPass
	@RequestMapping(value = errorDefaultPath, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Result<String> error(HttpServletRequest request) {

		HttpStatus status = getStatus(request);
		String path = getPath(request);
		Result<String> result = new Result<>(ResultCode.SYS_ERROR);
		result.setCode(status.value());
		String msg = "status:" + status.value() + ",path:" + path + ",reason:" + status.getReasonPhrase();
		result.setExtMsg(msg);

		return result;
	}

	protected Map<String, Object> getErrorAttributes(HttpServletRequest request) {
		WebRequest webRequest = new ServletWebRequest(request);
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		addStatus(errorAttributes, webRequest);
		addErrorMsg(errorAttributes, webRequest);
		addPath(errorAttributes, webRequest);
		return errorAttributes;
	}

	private void addStatus(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
		Integer status = getAttribute(requestAttributes, "javax.servlet.error.status_code");
		if (status == null) {
			errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorAttributes.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return;
		}
		errorAttributes.put("status", status);
		try {
			errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());
		} catch (Exception ex) {
			// Unable to obtain a reason
			errorAttributes.put("error", "Http Status " + status);
		}
	}

	private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
		String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
		if (path != null) {
			errorAttributes.put("path", path);
		}
	}

	private void addErrorMsg(Map<String, Object> errorAttributes, WebRequest webRequest) {
		Object message = getAttribute(webRequest, "javax.servlet.error.message");
		errorAttributes.put("message", StringUtils.isEmpty(message) ? "No message available" : message);
	}

	protected HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		} catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	public String getPath(HttpServletRequest request) {
		WebRequest webRequest = new ServletWebRequest(request);
		String path = getAttribute(webRequest, "javax.servlet.error.request_uri");
		if (path == null) {
			path = "";
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
		return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}
}
