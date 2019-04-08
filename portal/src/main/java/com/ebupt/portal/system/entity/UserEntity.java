package com.ebupt.portal.system.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "eb_user")
@Setter
@Getter
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) comment '用户名'", unique = true)
    private String userName;

    @Column(columnDefinition = "char(32) comment 'md5混淆后的密码'")
    private String password;

    @Column(columnDefinition = "integer default 0 comment '密码有效期(天) 0表示永久有效'")
    private Integer validity;

    @Column(columnDefinition = "char(1) default 1 comment '用户状态 1:正常 2:异常 3:已注销'")
    private String state;

    @Column(columnDefinition = "varchar(50) comment '用户真实姓名'")
    private String realName;

    @Column(columnDefinition = "varchar(100) comment '用户所属部门'")
    private String department;

    @Column(columnDefinition = "varchar(16) comment '用户手机号'")
    private String phone;

    @Column(columnDefinition = "char(14) comment '用户创建时间'")
    private String createTime;

    @Column(columnDefinition = "char(14) comment '用户生效时间'")
    private String effectTime;

    @Column(columnDefinition = "char(14) comment '最后一次更新密码时间'")
    private String pwdTime;

    @Column(columnDefinition = "char(14) comment '最后一次登录时间'")
    private String loginTime;

}
