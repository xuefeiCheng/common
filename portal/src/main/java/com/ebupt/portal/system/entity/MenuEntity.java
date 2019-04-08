package com.ebupt.portal.system.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "eb_menu")
@Setter
@Getter
public class MenuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) comment '菜单id'", unique = true)
    private String menuId;

    @Column(columnDefinition = "varchar(50) comment '父菜单id，为空则表示为一级菜单'")
    private String parentMenu;

    @Column(columnDefinition = "char(1) comment '菜单类别，1:菜单 2:按钮'")
    private String menuType;

    @Column(columnDefinition = "varchar(100) comment '菜单名称'")
    private String menuName;

    @Column(columnDefinition = "varchar(255) comment '菜单url'", unique = true)
    private String url;

    @Column(columnDefinition = "char(14) comment '创建时间'")
    private String time;

}
