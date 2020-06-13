package com.demo.server.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;

@RestController
public class AppExceptionExtErrorController implements ErrorController {

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
		String url = getPath(request);
		Result<String> result = new Result<>(ResultCode.SYS_ERROR);
		result.setCode(status.value());
		String msg = "status:" + status.value() + ", url:" + url + ", reason:" + status.getReasonPhrase();
		result.setExtMsg(msg);
		result.setData(url);

		return result;
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
		if (java.util.Objects.isNull(path)) {
			path = "";
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
		return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}
}
