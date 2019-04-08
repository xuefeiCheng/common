package com.ebupt.portal.canyon.common.advice;

import com.alibaba.druid.util.DruidWebUtils;
import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.util.TimeUtil;
import com.ebupt.portal.canyon.common.annotation.EBLog;
import com.ebupt.portal.canyon.system.entity.Log;
import com.ebupt.portal.canyon.common.enums.LogEnum;
import com.ebupt.portal.canyon.system.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志切面
 *
 * @author chy
 * @date 2019-03-08 15:33
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

	private static final String BASE_PACKAGE = "com.ebupt.portal";
	private static final int SUCCESS = 200;

	private final LogService logService;
	private final GlobalExceptionHandler globalExceptionHandler;

	@Autowired
	public LogAspect(LogService logService, GlobalExceptionHandler globalExceptionHandler) {
		this.logService = logService;
		this.globalExceptionHandler = globalExceptionHandler;
	}

	@Pointcut("execution(public * com.ebupt.portal.canyon.*.controller.*.*(..))")
	public void logPointCut() {}

	@Around("logPointCut() && @annotation(ebLog)")
	public JsonResult recordLog(ProceedingJoinPoint pjp, EBLog ebLog) {
		long startTime = System.currentTimeMillis();
		String requestTime = TimeUtil.getCurrentTime14();

		// 获取请求相关信息
		String operator = (String) SecurityUtils.getSubject().getPrincipal();
		ServletRequestAttributes requestAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest req = null;
		String method = null;
		String ip = null;
		if (requestAttributes != null) {
			req = requestAttributes.getRequest();
			method = req.getMethod();
			ip = DruidWebUtils.getRemoteAddr(req);
		} else {
			log.error("get ServletRequestAttributes failed.");
		}
		String url = this.getRealUrl(req);

		// 获取请求处理结果相关信息
		String result = "1";
		String error = null;
		Throwable throwException = null;

		JsonResult jsonResult = JsonResult.ok();
		try {
			jsonResult = (JsonResult) pjp.proceed();

			if (jsonResult.getCode() != SUCCESS) {
				result = "2";
				error = jsonResult.getMsg();
			}
		} catch (Throwable throwable) {
			result = "3";
			throwException = throwable;

			// 封装异常消息
			error = throwable.getMessage() + "\r\n";
			StackTraceElement[] stackTrace = throwable.getStackTrace();
			for (StackTraceElement trace: stackTrace) {
				if (trace.toString().startsWith(BASE_PACKAGE)) {
					error += trace.toString();
					break;
				}
			}
		}

		// 封装日志描述
		if (StringUtils.isEmpty(operator)) {
			operator = (String) SecurityUtils.getSubject().getPrincipal();
		}
		String description = this.getDescription(ebLog.value(), ebLog.type(), operator, pjp);

		Log log = new Log(ip, method, url, this.cutStr(description), System.currentTimeMillis() - startTime,
				operator, result, this.cutStr(error), requestTime);
		this.logService.save(log);

		if (throwException != null) {
			return globalExceptionHandler.handleControllerException(req, throwException);
		}

		return jsonResult;
	}

	/**
	 * 获取真实的URL请求
	 *
	 * @param request
	 *                  HttpServletRequest
	 * @return
	 *                  真实url
	 */
	private String getRealUrl(HttpServletRequest request) {
		String url = null;
		if (request != null) {
			url = request.getParameter("redirectUrl");
			if (StringUtils.isEmpty(url)) {
				url = request.getRequestURI();
			}
		}
		return url;
	}

	/**
	 * 获取操作描述内容
	 *
	 * @param description
	 *                      用户输入的描述内容
	 * @param type
	 *                      日志类型
	 * @param operator
	 *                      操作员
	 * @param pjp
	 *                      ProceedingJoinPoint
	 * @return
	 *                      组装完成的描述内容
	 */
	private String getDescription(String description, LogEnum type, String operator, ProceedingJoinPoint pjp) {
		// 处理占位符
		List<String> list = getPlaceholder(description);
		if (list != null && list.size() > 0) {
			for (String placeholder : list) {
				Object value = getValueByName(pjp, placeholder);
				if (value != null) {
					description = description.replace("{" + placeholder + "}", value.toString());
				}
			}
		}

		if (type == LogEnum.LOG) {
			description = "用户" + operator + description;
		} else if (type == LogEnum.NO_AUTH) {
			description = "用户" + operator + "尝试访问未授权功能";
		} else if (type == LogEnum.NO_LOGIN) {
			description = "未登录用户尝试访问本系统";
		} else if (type == LogEnum.LOGOUT) {
			description = "用户" + operator + "退出登录";
		}

		return description;
	}

	/**
	 * 获取占位符之间的字符串
	 *
	 * @param str
	 *                  源字符串
	 * @return
	 *                  占位符
	 */
	private List<String> getPlaceholder(String str) {
		String regex =  "\\{" + "(.*?)" + "}";
		List<String> list = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

	/**
	 * 利用java反射根据属性名获取属性值
	 *
	 * @param joinPoint
	 *                      ProceedingJoinPoint对象
	 * @param placeholder
	 *                      属性名
	 * @return
	 *                      值
	 */
	private Object getValueByName(ProceedingJoinPoint joinPoint, String placeholder) {
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();

		// 判断是类对象还是单个值
		Object value = "";
		String name;
		String field = "";
		int idx = placeholder.indexOf(".");
		if (idx > 0) {
			name = placeholder.substring(0, idx);
			field = placeholder.substring(idx + 1);
		} else {
			name = placeholder;
		}

		for (int i = 0; i < parameterNames.length; i++) {
			if (parameterNames[i].equalsIgnoreCase(name)) {
				if (idx > 0) {
					try {
						Field declaredField = args[i].getClass().getDeclaredField(field);
						declaredField.setAccessible(true);
						value = declaredField.get(args[i]);
						break;
					} catch (NoSuchFieldException | IllegalAccessException e) {
						log.warn("反射获取参数[{}]失败,做空值处理", field);
					}
				} else {
					value = args[i];
					break;
				}
			}
		}

		return value;
	}

	/**
	 * 截取指定长度的字符（支持中文），防止中文被截取一半导致错误
	 *
	 * @param str
	 *              原字符串
	 * @return
	 *              最大长度以内最长字符串
	 */
	private String cutStr(String str) {
		int sum = 0, maxLen = 200;
		String tmpStr;
		StringBuilder builder = new StringBuilder();

		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				tmpStr = String.valueOf(str.charAt(i));
				sum += tmpStr.getBytes(StandardCharsets.UTF_8).length;
				if (sum <= maxLen) {
					builder.append(tmpStr);
				} else {
					break;
				}
			}
		}

		return builder.toString();
	}

}
