package com.ebupt.portal.canyon.common.advice;

import com.ebupt.portal.canyon.common.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局统一异常处理切面
 *
 * @author chy
 * @date 2019-02-27 17:28
 */
@Slf4j
@Component
public class GlobalExceptionHandler {

	JsonResult handleControllerException(HttpServletRequest request, Throwable ex) {
		log.error("throw an exception: ", ex);

		HttpStatus status = null;
		if (ex instanceof NoHandlerFoundException) {
			status = HttpStatus.NOT_FOUND;
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			status = HttpStatus.METHOD_NOT_ALLOWED;
		} else if (ex instanceof HttpMediaTypeNotSupportedException) {
			status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		} else if (ex instanceof HttpMediaTypeNotAcceptableException) {
			status = HttpStatus.NOT_ACCEPTABLE;
		} else if (ex instanceof MissingPathVariableException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} else if (ex instanceof MissingServletRequestParameterException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof ServletRequestBindingException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof ConversionNotSupportedException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} else if (ex instanceof TypeMismatchException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof HttpMessageNotReadableException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof HttpMessageNotWritableException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} else if (ex instanceof MethodArgumentNotValidException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof MissingServletRequestPartException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof BindException) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof AsyncRequestTimeoutException) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}
		if (status != null) {
			return JsonResult.error(status.value(), status.name());
		}

		status = getStatus(request);
		return JsonResult.error(status.value(), status.name());
	}

	/**
	 * 获取HTTP状态码
	 *
	 * @param request
	 *                  HttpServletRequest对象
	 * @return
	 *                  HTTP状态码
	 */
	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
