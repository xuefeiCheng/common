create table if not exists eb_user (
  `id` int not null auto_increment,
  `user_name` varchar(50) not null comment '登录用户名',
  `real_name` varchar(50) comment '真实姓名',
  `password` char(32) not null comment '登录密码',
  `salt` char(6) not null comment 'MD5加密盐值',
  `department` varchar(200) comment '所属部门',
  `contact` varchar(50) comment '联系方式',
  `create_time` char(14) not null comment '创建时间',
  `last_login_time` char(14) comment '最后一次登录时间',
  `last_pwd_time` char(14) comment '最后一次修改密码时间',
  `pwd_validity` int not null comment '密码有效期 0-永久有效 >1-有效天数',
  `state` char(1) not null comment '用户状态 1-正常 2-密码已过期 3-多次登录失败锁定 4-注销',
  primary key (`id`),
  unique key (`user_name`),
  index (`user_name`)
);

create table if not exists eb_role (
  `id` int not null auto_increment,
  `role_id` varchar(50) not null comment '角色ID',
  `role_name` varchar(50) not null comment '角色名称',
  `create_time` char(14) not null comment '创建时间',
  primary key (`id`),
  unique key (`role_id`),
  index (`role_id`)
);

create table if not exists eb_menu (
  `id` int not null auto_increment,
  `menu_id` varchar(50) not null comment '菜单ID',
  `menu_name` varchar(50) not null comment '菜单名称',
  `menu_type` char(1) not null comment '菜单类型 1-菜单 2-按钮',
  `parent_menu` varchar(50) comment '上级菜单ID',
  `create_time` char(14) not null comment '创建时间',
  `url` varchar(100) comment '请求url',
  primary key (`id`),
  unique key (`menu_id`),
  index (`menu_id`)
);

create table if not exists eb_user_role (
  `id` int not null auto_increment,
  `user_name` varchar(50) not null comment '用户名',
  `role_id` varchar(50) not null comment '角色ID',
  `create_time` char(14) not null comment '创建时间',
  primary key (`id`),
  index (`user_name`)

);

create table if not exists eb_role_menu (
  `id` int not null auto_increment,
  `role_id` varchar(50) not null comment '角色ID',
  `menu_id` varchar(50) not null comment '菜单ID',
  `create_time` char(14) not null comment '创建时间',
  primary key (`id`),
  index(`role_id`)
);

create table if not exists eb_log (
  `id` int not null auto_increment,
  `ip` varchar(50) comment '请求IP',
  `method` varchar(50) comment '请求方式',
  `url` varchar(100) comment '请求url',
  `description` varchar(200) not null comment '描述',
  `exec_time` int not null comment '请求处理时间',
  `operator` varchar(50) comment '请求用户',
  `result` char(1) not null comment '请求处理结果 1-成功 2-失败 3-异常',
  `error` varchar(200) comment '请求失败原因',
  `create_time` char(14) not null comment '请求时间',
  primary key (`id`),
  index (`create_time`)
);