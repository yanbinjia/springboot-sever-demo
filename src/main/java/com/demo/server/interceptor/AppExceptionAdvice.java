package com.demo.server.interceptor;

import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.exception.AppException;
import com.demo.server.common.util.LoggerUtil;

/**
 * 应用ControllerAdvice全局异常处理,记录请求异常日志
 * 
 * 注意:</br>
 * 进入Controller层的错误才会由@ControllerAdvice处理,</br>
 * 拦截器抛出的错误以及访问错误地址的情况@ControllerAdvice处理不了,</br>
 * 由SpringBoot默认的异常处理机制处理(例如404由x.error.BasicErrorController处理).</br>
 * 
 * 如果要处理404这类异常,统一异常格式,可以扩展BasicErrorController,重写error和errorHtml方法
 * 
 */
@ControllerAdvice
public class AppExceptionAdvice {

	public static String DETAIL_TITLE = " ";

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

		Result<Void> result = new Result<Void>(ResultCode.METHOD_NOT_ALLOWED);
		result.setExtMsg(e.getMessage());

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * 
	 * HttpMediaTypeException
	 * 
	 * Content type 'text/plain;charset=UTF-8' not supported
	 * 
	 */
	@ResponseBody
	@ExceptionHandler(HttpMediaTypeException.class)
	public Result<Void> handlerMissParamException(HttpMediaTypeException e, HttpServletRequest request,
			HttpServletResponse response) {

		Result<Void> result = new Result<Void>(ResultCode.NOT_ACCEPTABLE);
		result.setExtMsg(e.getMessage());

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

		Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
		result.setExtMsg(e.getMessage());

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

		Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);

		result.setExtMsg(e.getBindingResult().getAllErrors().stream().map(AppExceptionAdvice::buildMessage)
				.collect(Collectors.joining(";")));

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * 拦截其他未知的 Http(Servlet) Exception
	 */
	@ResponseBody
	@ExceptionHandler(ServletException.class)
	public Result<Void> handlerMissParamException(ServletException e, HttpServletRequest request,
			HttpServletResponse response) {

		Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
		result.setExtMsg(e.getMessage());

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	/**
	 * 拦截服务器未知异常
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Void> handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) {

		Result<Void> result = new Result<Void>(ResultCode.SYS_ERROR);

		// Record exception log.
		LoggerUtil.exceptionLog(request, result, e);

		return result;
	}

	private static String buildMessage(ObjectError error) {
		if (error instanceof FieldError) {
			return ((FieldError) error).getField() + error.getDefaultMessage();
		}
		return error.getDefaultMessage();
	}

}
