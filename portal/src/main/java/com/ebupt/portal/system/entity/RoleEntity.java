package com.ebupt.portal.system.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "eb_role")
@Setter
@Getter
public class RoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) comment '角色ID'", unique = true)
    private String roleId;

    @Column(columnDefinition = "varchar(50) comment '角色名称'")
    private String roleName;

    @Column(columnDefinition = "char(14) comment '角色创建时间'")
    private String time;

}
