package com.ebupt.portal.User.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(50) comment '用户姓名'")
    private String name;

    @Column(columnDefinition = "integer comment '用户年龄'")
    private int age;

}
