package com.ebupt.portal.common.controller;

import com.ebupt.portal.common.Results.JSONResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EBNotFoundController implements ErrorController {

    @GetMapping("/error")
    public JSONResult handleError() {
        return JSONResult.NOT_FOUND();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
