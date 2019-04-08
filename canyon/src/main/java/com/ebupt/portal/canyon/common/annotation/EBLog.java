package com.ebupt.portal.canyon.common.annotation;

import com.ebupt.portal.canyon.common.enums.LogEnum;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author chy
 * @date 2019-03-08 15:23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EBLog {

	String value() default ""; // 操作描述

	LogEnum type() default LogEnum.LOG; // 操作类型

}
