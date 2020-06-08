package com.demo.server.interceptor;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.exception.AppException;
import com.demo.server.common.util.LoggerUtil;

/**
 * 应用ControllerAdvice全局异常处理,记录请求异常日志
 * 
 */
@ControllerAdvice
public class AppExceptionAdvice {

	private static String buildMessage(ObjectError error) {
		if (error instanceof FieldError) {
			return ((FieldError) error).getField() + error.getDefaultMessage();
		}
		return error.getDefaultMessage();
	}

	@ResponseBody
	@ExceptionHandler(AppException.class)
	public Result<Void> handlerAppException(AppException e, HttpServletRequest request, HttpServletResponse response) {

		Result<Void> result = new Result<Void>();
		result.setCode(e.getCode());
		result.setMsg(e.getMsg());

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * HttpRequestMethodNotSupportedException
	 * 
	 * Request method 'POST'/'GET' not supported
	 */
	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<Void> handlerMissParamException(HttpRequestMethodNotSupportedException e, HttpServletRequest request,
			HttpServletResponse response) {

		String detailMsg = " Detail:" + e.getMessage();

		Result<Void> result = new Result<Void>();
		result.setCode(ResultCode.METHOD_NOT_ALLOWED.code);
		result.setMsg(ResultCode.METHOD_NOT_ALLOWED.msg + detailMsg);

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * spring 参数缺失，框架默认异常
	 */
	@ResponseBody
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Result<Void> handlerMissParamException(MissingServletRequestParameterException e, HttpServletRequest request,
			HttpServletResponse response) {
		String detailMsg = " Detail:" + e.getMessage();

		Result<Void> result = new Result<Void>();
		result.setCode(ResultCode.PARAM_ERROR.code);
		result.setMsg(ResultCode.PARAM_ERROR.msg + detailMsg);

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * 拦截参数校验异常
	 *
	 */
	@ResponseBody
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Result<?> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request,
			HttpServletResponse response) {

		Result<Void> result = new Result<Void>();
		result.setCode(ResultCode.PARAM_ERROR.code);
		result.setMsg(e.getBindingResult().getAllErrors().stream().map(AppExceptionAdvice::buildMessage)
				.collect(Collectors.joining(";")));

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * 拦截服务器未知异常
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Result<Void> handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) {

		Result<Void> result = new Result<Void>(ResultCode.SYSTEM_ERROR);

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

}
