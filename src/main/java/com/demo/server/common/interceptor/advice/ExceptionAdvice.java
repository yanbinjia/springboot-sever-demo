/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-25T09:01:14.975+08:00
 */

package com.demo.server.common.interceptor.advice;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.exception.AppException;
import com.demo.server.common.utils.LogUtil;
import com.demo.server.common.utils.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用ControllerAdvice全局异常处理,记录请求异常日志
 * <p>
 * 注意:</br>
 * 进入Controller层的错误才会由@ControllerAdvice处理,</br>
 * 拦截器抛出的错误以及访问无效地址的情况@ControllerAdvice处理不了,</br>
 * 由SpringBoot默认的异常处理机制处理(例如404由x.error.BasicErrorController处理).</br>
 * <p>
 * 如要处理404这类异常,统一响应格式,可以实现ErrorController接口做统一处理,例如ExceptionErrorController
 */
@ControllerAdvice
@Order(-1)
@Slf4j
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(AppException.class)
    public Result<Void> handleException(AppException e, HttpServletRequest request, HttpServletResponse response) {

        Result<Void> result = new Result<Void>();
        result.setCode(e.getCode());
        result.setMsg(e.getMsg());

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * HttpRequestMethodNotSupportedException
     * <p>
     * Request method 'POST'/'GET' not supported
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleException(HttpRequestMethodNotSupportedException e, HttpServletRequest request,
                                        HttpServletResponse response) {

        Result<Void> result = new Result<Void>(ResultCode.METHOD_NOT_ALLOWED);
        result.setExtMsg(e.getMessage());

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * HttpMediaTypeException
     * <p>
     * Content type 'text/plain;charset=UTF-8' not supported
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeException.class)
    public Result<Void> handleException(HttpMediaTypeException e, HttpServletRequest request,
                                        HttpServletResponse response) {

        Result<Void> result = new Result<Void>(ResultCode.NOT_ACCEPTABLE);
        result.setExtMsg(e.getMessage());

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * org.springframework.web.bind.MissingServletRequestParameterException
     * spring 参数缺失，框架默认异常, 例如: Required Long parameter 'id' is not present
     */
    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result<Void> handleException(MissingServletRequestParameterException e, HttpServletRequest request,
                                        HttpServletResponse response) {

        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
        result.setExtMsg(e.getParameterName() + ":参数不存在.");

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * Hibernate Validator
     * org.springframework.web.bind.MethodArgumentNotValidException
     * 拦截参数校验异常, 需要validate的参数不符合要求, 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result<?> handleException(MethodArgumentNotValidException e, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);

        result.setExtMsg(this.buildMessage(e));

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * org.springframework.http.converter.HttpMessageNotReadableException
     * 未传递RequestBody参数/RequestBody参数解析异常
     */
    @ResponseBody
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public Result<?> handleException(HttpMessageNotReadableException e, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
        result.setExtMsg("HttpMessageNotReadable: request body param error.");

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * Hibernate Validator
     * javax.validation.ConstraintViolationException
     * Controller上加@Validated注解, 在@RequestParam上开启validate能力.
     * 需要validate的参数不符合要求, 抛出异常
     */
    @ResponseBody
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result<?> handleException(ConstraintViolationException e, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
        result.setExtMsg(this.buildMessage(e));

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * org.springframework.web.multipart.MultipartException:
     * Current request is not a multipart request
     */
    @ResponseBody
    @ExceptionHandler(value = {MultipartException.class})
    public Result<?> handleException(MultipartException e, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
        result.setExtMsg(e.getMessage());

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * org.springframework.web.multipart.MaxUploadSizeExceededException
     * <p>
     * # 上传文件总的最大值
     * spring.servlet.multipart.max-request-size=10MB
     * # 单个文件的最大值
     * spring.servlet.multipart.max-file-size=10MB
     */
    @ResponseBody
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public Result<?> handleException(MaxUploadSizeExceededException e, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);

        String readableSize = "";
        try {
            String eMessage = e.getMessage();
            String maxUploadSizeStr = eMessage.substring(eMessage.lastIndexOf("(") + 1).replace(")", "").trim();
            FileUtil.getReadableSizeStr(Long.parseLong(maxUploadSizeStr));
        } catch (Exception numberFormatException) {
            log.error("handleException error.", numberFormatException);
        }

        result.setExtMsg("上传文件大小超过限制" + readableSize);

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public Result<?> handleException(MethodArgumentTypeMismatchException e, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
        result.setExtMsg(e.getParameter().getParameterName() + ":参数类型不匹配.");

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * 拦截其他未知的 Http(Servlet) Exception
     */
    @ResponseBody
    @ExceptionHandler(ServletException.class)
    public Result<Void> handleException(ServletException e, HttpServletRequest request,
                                        HttpServletResponse response) {

        Result<Void> result = new Result<Void>(ResultCode.PARAM_ERROR);
        result.setExtMsg(e.getMessage());

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    /**
     * 拦截服务器未知异常
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {

        Result<Void> result = new Result<Void>(ResultCode.SYS_ERROR);

        LogUtil.exceptionLog(request, result, e);

        return result;
    }

    private String buildMessage(MethodArgumentNotValidException e) {
        // 统一格式, "param:msg; ..."
        return e.getBindingResult().getAllErrors().stream().map(error -> {
            return ((FieldError) error).getField() + ":" + error.getDefaultMessage();
        }).sorted().collect(Collectors.joining("; "));
    }

    private String buildMessage(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        // 统一格式,原格式为"method.param:msg, ...", 处理为"param:msg; ..."
        return constraintViolations.stream().map((cv) -> {
            String param = cv.getPropertyPath().toString();
            int dotindex = param.lastIndexOf(".");
            if (dotindex > 0) {
                param = param.substring(dotindex + 1);
            }
            return param + ":" + cv.getMessage();
        }).sorted().collect(Collectors.joining("; "));
    }
}
