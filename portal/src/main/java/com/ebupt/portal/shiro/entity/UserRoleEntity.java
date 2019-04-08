package com.ebupt.portal.shiro.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "eb_user_role")
@Setter
@Getter
public class UserRoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) comment '用户名'")
    private String userName;

    @Column(columnDefinition = "varchar(50) comment '角色ID'")
    private String roleId;

}
