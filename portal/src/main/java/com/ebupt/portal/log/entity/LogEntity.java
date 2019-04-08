package com.ebupt.portal.log.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "eb_log")
@Setter
@Getter
@NoArgsConstructor
public class LogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(100) comment '请求类'")
    private String controller;

    @Column(columnDefinition = "varchar(100) comment '请求方法'")
    private String method;

    @Column(columnDefinition = "varchar(50) comment '请求用户'")
    private String operator;

    @Column(columnDefinition = "char(1) comment '请求结果 1:成功 2:失败 3:异常请求'")
    private String result;

    @Column(columnDefinition = "text comment '响应内容'")
    private String response;

    @Column(columnDefinition = "text comment '错误信息'")
    private String error;

    @Column(columnDefinition = "varchar(255) comment '操作描述'")
    private String description;

    @Column(columnDefinition = "varchar(50) comment '访问者IP'")
    private String ip;

    @Column(columnDefinition = "char(14) comment '请求时间 格式:yyyyMMddHHmmss'")
    private String time;

    @Column(columnDefinition = "integer comment '执行耗时 单位:ms'")
    private Long execTime;

    public LogEntity(String controller, String method, String operator, String result, String response,
                     String error, String description, String ip, String time, Long execTime) {
        this.controller = controller;
        this.method = method;
        this.operator = operator;
        this.result = result;
        this.response = response;
        this.error = error;
        this.description = description;
        this.ip = ip;
        this.time = time;
        this.execTime = execTime;
    }

}
