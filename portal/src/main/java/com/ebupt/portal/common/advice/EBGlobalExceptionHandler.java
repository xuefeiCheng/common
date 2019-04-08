package com.ebupt.portal.common.advice;

import com.ebupt.portal.common.Results.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class EBGlobalExceptionHandler {

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public JSONResult NotFound(HttpServletRequest request, Exception e) {
        return JSONResult.NOT_FOUND();
    }

}
