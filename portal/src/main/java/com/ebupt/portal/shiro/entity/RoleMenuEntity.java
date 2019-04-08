package com.ebupt.portal.shiro.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "eb_role_menu")
@Setter
@Getter
public class RoleMenuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) comment '角色ID'")
    private String roleId;

    @Column(columnDefinition = "varchar(50) comment '菜单ID'")
    private String menuId;

}
