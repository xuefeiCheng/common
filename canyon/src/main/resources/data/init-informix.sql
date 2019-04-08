-- 初始化角色表
insert into eb_role (role_id, role_name, create_time)
  select 'admin', '系统管理员', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role where role_id = 'admin') and tabid = 1;
insert into eb_role (role_id, role_name, create_time)
  select 'base', '基础操作员', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role where role_id = 'base') and tabid = 1;

-- 初始化基础功能
insert into eb_menu (menu_id, menu_name, menu_type, create_time, url)
  select 'updatePwd', '修改密码', '2', to_char(current, '%Y%m%d%H%M%S'), 'POST:user/updatePwd' from systables
    where not exists (select 1 from eb_menu where menu_id = 'updatePwd') and tabid = 1;
-- 初始化系统管理菜单
insert into eb_menu (menu_id, menu_name, menu_type, create_time)
  select 'systemManage', '系统管理', '1', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_menu where menu_id = 'systemManage') and tabid = 1;
-- 初始化用户管理
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time)
  select 'userManage', '用户管理', '1', 'systemManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_view', '用户管理-查看用户', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:user' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_view') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_update', '用户管理-更新用户信息', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:user/update' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_update') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_updateState', '用户管理-修改用户状态', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:user/updateState' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_updateState') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_add', '用户管理-新增用户', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:user/add' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_add') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_allRoles', '用户管理-获取所有角色信息', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'GET:role' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_allRoles') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_roles', '用户管理-获取已拥有的角色', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'GET:user/role/*' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_roles') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_relateRoles', '用户管理-关联角色', '2', 'userManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:user/role' from systables
    where not exists (select 1 from eb_menu where menu_id = 'userManage_relateRoles') and tabid = 1;
-- 初始化角色管理
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time)
  select 'roleManage', '角色管理', '1', 'systemManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_list', '角色管理-查看角色', '2', 'roleManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:role' from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_list') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_save', '角色管理-新增角色', '2', 'roleManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:role/add' from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_save') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_delete', '角色管理-删除角色', '2', 'roleManage', to_char(current, '%Y%m%d%H%M%S'), 'DELETE:role/*' from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_delete') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_allMenus', '角色管理-获取所有权限信息', '2', 'roleManage', to_char(current, '%Y%m%d%H%M%S'), 'GET:menu' from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_allMenus') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_ownedMenus', '角色管理-获取已拥有的权限信息', '2', 'roleManage', to_char(current, '%Y%m%d%H%M%S'), 'GET:role/auth/*' from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_ownedMenus') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_relateMenus', '角色管理-关联权限', '2', 'roleManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:role/auth' from systables
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_relateMenus') and tabid = 1;
-- 初始化日志管理
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time)
  select 'logManage', '日志管理', '1', 'systemManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_menu where menu_id = 'logManage') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'logManage_list', '日志管理-查看', '2', 'logManage', to_char(current, '%Y%m%d%H%M%S'), 'POST:log' from systables
    where not exists (select 1 from eb_menu where menu_id = 'logManage_list') and tabid = 1;
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'logManage_delete', '日志管理-删除日志', '2', 'logManage', to_char(current, '%Y%m%d%H%M%S'), 'DELETE:log/*' from systables
    where not exists (select 1 from eb_menu where menu_id = 'logManage_delete') and tabid = 1;

-- 初始化用户角色表
insert into eb_user_role (user_name, role_id, create_time)
  select 'root', 'admin', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_user_role where user_name = 'root' and role_id = 'admin') and tabid = 1;
insert into eb_user_role (user_name, role_id, create_time)
  select 'root', 'base', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_user_role where user_name = 'root' and role_id = 'base') and tabid = 1;

-- 初始化基础角色权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'base', 'updatePwd', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'base' and menu_id = 'updatePwd') and tabid = 1;
-- 初始化角色菜单表
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'systemManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'systemManage') and tabid = 1;
-- 初始化用户管理权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_view', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_view') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_update', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_update') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_updateState', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_updateState') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_add', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_add') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_allRoles', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_allRoles') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_roles', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_roles') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_relateRoles', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_relateRoles') and tabid = 1;
-- 初始化角色管理权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_list', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_list') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_save', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_save') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_delete', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_delete') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_allMenus', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_allMenus') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_ownedMenus', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_ownedMenus') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_relateMenus', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_relateMenus') and tabid = 1;
-- 初始化日志管理权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'logManage', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'logManage') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'logManage_list', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'logManage_list') and tabid = 1;
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'logManage_delete', to_char(current, '%Y%m%d%H%M%S') from systables
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'logManage_delete') and tabid = 1;
