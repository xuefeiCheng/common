create table if not exists eb_user (
  id serial not null,
  user_name varchar(50) not null unique,
  real_name varchar(50),
  password char(32) not null,
  salt char(6) not null,
  department varchar(200),
  contact varchar(50),
  create_time char(14) not null,
  last_login_time char(14),
  last_pwd_time char(14),
  pwd_validity int not null,
  state char(1) not null,
  primary key (id)
);

create table if not exists eb_role (
  id serial not null,
  role_id varchar(50) not null unique,
  role_name varchar(50) not null,
  create_time char(14) not null,
  primary key (id)
);

create table if not exists eb_menu (
  id serial not null,
  menu_id varchar(50) not null unique,
  menu_name varchar(50) not null,
  menu_type char(1) not null,
  parent_menu varchar(50),
  create_time char(14) not null,
  url varchar(100),
  primary key (id)
);

create table if not exists eb_user_role (
  id serial not null,
  user_name varchar(50) not null,
  role_id varchar(50) not null,
  create_time char(14) not null,
  primary key (id)
);
create index if not exists idx_ebuserrole_01 on eb_user_role(user_name);

create table if not exists eb_role_menu (
  id serial not null,
  role_id varchar(50) not null,
  menu_id varchar(50) not null,
  create_time char(14) not null,
  primary key (id)
);
create index if not exists idx_ebrolemenu_01 on eb_role_menu(role_id);

create table if not exists eb_log (
  id serial not null,
  ip varchar(50),
  method varchar(50),
  url varchar(100),
  description varchar(200) not null,
  exec_time int not null,
  operator varchar(50),
  result char(1) not null,
  error varchar(200),
  create_time char(14) not null,
  primary key (id)
);
create index if not exists idx_eblog_01 on eb_log(create_time);
