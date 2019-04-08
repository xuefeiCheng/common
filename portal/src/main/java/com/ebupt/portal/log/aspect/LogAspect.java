package com.ebupt.portal.log.aspect;

import com.ebupt.portal.common.utils.NetWorkUtil;
import com.ebupt.portal.log.annotations.EBLog;
import com.ebupt.portal.common.Results.JSONResult;
import com.ebupt.portal.common.utils.StringUtil;
import com.ebupt.portal.common.utils.TimeUtil;
import com.ebupt.portal.log.entity.LogEntity;
import com.ebupt.portal.log.enums.LogEnum;
import com.ebupt.portal.log.service.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
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
import java.util.List;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final LogService logService;

    @Autowired
    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("execution(public * com.ebupt.portal.*.controller.*.*(..))")
    public void apiLog() {}

    @Around("apiLog() && @annotation(ebLog)")
    public JSONResult recordLog(ProceedingJoinPoint joinPoint, EBLog ebLog) {
        // 获取request
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String username = ""; // 用户名
        String class_name = joinPoint.getTarget().getClass().getSimpleName(); // 类名
        String method_name = joinPoint.getSignature().getName(); // 方法名
        String result = "1"; // 请求结果
        String response = ""; // 响应内容
        String ip = NetWorkUtil.getClientIpAddr(request); // 请求IP
        StringBuilder error = new StringBuilder(); // 错误信息
        String desc = ""; // 描述
        long exec_time; // 执行时间
        String time = TimeUtil.getCurrentTime14(); // 请求时间
        JSONResult jsonResult;

        // 获取登录用户
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) username = (String) subject.getPrincipal();

        if (ebLog.type() == LogEnum.LOG) {
            desc = "用户" + username + "执行" + ebLog.value() + "操作";
        } else if (ebLog.type() == LogEnum.CUSTOM){
            desc = ebLog.value();
        } else {
            SavedRequest shiroSavedRequest = (SavedRequest) SecurityUtils.getSubject()
                    .getSession().getAttribute("shiroSavedRequest");
            String url = "";
            if (shiroSavedRequest != null)
                url = shiroSavedRequest.getRequestURI();

            if (ebLog.type() == LogEnum.NO_AUTH) {
                desc = "用户" + username + "越权请求:" + url;
            } else if (ebLog.type() == LogEnum.NO_LOGIN) {
                desc = "未登录用户请求:" + url;
            } else if (ebLog.type() == LogEnum.LOGOUT) {
                desc = "用户" + username + "退出系统";
            }
        }

        List<String> list = StringUtil.getPlaceholder(desc, "\\{", "\\}");
        if (list != null && list.size() > 0) {
            for (String placeholder : list) {
                Object value = getValueByName(joinPoint, placeholder);
                desc = desc.replace(placeholder, value.toString());
            }
        }

        Long start = System.currentTimeMillis();
        try {
            jsonResult = (JSONResult) joinPoint.proceed();
        } catch (Throwable throwable) {
            //return JSONResult.ERROR("失败");
            log.error("方法执行失败:{}", throwable.getMessage());
            jsonResult = JSONResult.ERROR("内部错误");
            result = "2";

            // 错误描述
            error = new StringBuilder(throwable.getMessage() + "\r\n");
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (StackTraceElement trace: stackTrace) {
                error.append(trace.toString()).append("\r\n");
            }
        }
        Long end = System.currentTimeMillis();
        exec_time = end - start;

        ObjectMapper mapper = new ObjectMapper();
        try {
            if (jsonResult != null)
                response = mapper.writeValueAsString(jsonResult);
        } catch (JsonProcessingException e) {
            log.warn("将响应转换JSON字符串失败:{}", e.getMessage());
        }

        LogEntity entity = new LogEntity(class_name, method_name, username, result, response, error.toString(),
                desc, ip, time, exec_time);
        logService.save(entity);

        return jsonResult;
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
        Object[] args = joinPoint.getArgs(); // 值集合
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();// 名称集合

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
                if (idx > 0) { // 对象
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

}
