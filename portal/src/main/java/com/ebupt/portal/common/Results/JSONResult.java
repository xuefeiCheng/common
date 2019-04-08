package com.ebupt.portal.common.Results;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("rest接口响应内容")
public class JSONResult {

    @ApiModelProperty("响应码\r\n200:成功\r\n400:参数校验失败\r\n401:无访问权限\r\n402:未登录\r\n" +
            "403:登录失败\r\n404:未找到请求资源\r\n500:内部错误")
    private Integer code;

    @ApiModelProperty("响应码描述")
    private String msg;

    @ApiModelProperty("响应内容 为空时不返回")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    private JSONResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JSONResult OK() {
        return new JSONResult(200, "OK", null);
    }

    public static JSONResult OK(Object data) {
        return new JSONResult(200, "OK", data);
    }

    public static JSONResult ERROR(String msg) {
        return new JSONResult(500, msg, null);
    }

    public static JSONResult NO_LOGIN() {
        return new JSONResult(402, "未进行登录", null);
    }

    public static JSONResult LOGIN_FAIL(String msg) {
        return new JSONResult(403, msg, null);
    }

    public static JSONResult PARAM_ERROR(String msg) {
        return new JSONResult(400, msg, null);
    }

    public static JSONResult NO_AUTH() {
        return new JSONResult(401, "无访问权限", null);
    }

    public static JSONResult NOT_FOUND() {
        return new JSONResult(404, "未发现处理方法", null);
    }

}
