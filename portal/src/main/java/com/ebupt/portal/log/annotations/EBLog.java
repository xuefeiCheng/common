package com.ebupt.portal.log.annotations;

import com.ebupt.portal.log.enums.LogEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EBLog {

    String value() default ""; // 操作描述

    LogEnum type() default LogEnum.LOG; // 日志类型

}
