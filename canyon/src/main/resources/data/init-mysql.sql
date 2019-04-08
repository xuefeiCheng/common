-- 初始化角色表
insert into eb_role (role_id, role_name, create_time)
  select 'admin', '系统管理员', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role where role_id = 'admin');
insert into eb_role (role_id, role_name, create_time)
  select 'base', '基础操作员', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role where role_id = 'base');

-- 初始化基础功能
insert into eb_menu (menu_id, menu_name, menu_type, create_time, url)
  select 'updatePwd', '修改密码', '2', date_format(now(), '%Y%m%d%H%i%s'), 'POST:user/updatePwd' from dual
    where not exists (select 1 from eb_menu where menu_id = 'updatePwd');
-- 初始化系统管理菜单
insert into eb_menu (menu_id, menu_name, menu_type, create_time)
  select 'systemManage', '系统管理', '1', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_menu where menu_id = 'systemManage');
-- 初始化用户管理
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time)
  select 'userManage', '用户管理', '1', 'systemManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_view', '用户管理-查看用户', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:user' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_view');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_update', '用户管理-更新用户信息', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:user/update' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_update');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_updateState', '用户管理-修改用户状态', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:user/updateState' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_updateState');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_add', '用户管理-新增用户', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:user/add' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_add');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_allRoles', '用户管理-获取所有角色信息', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'GET:role' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_allRoles');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_roles', '用户管理-获取已拥有的角色信息', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'GET:user/role/*' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_roles');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'userManage_relateRoles', '用户管理-关联角色', '2', 'userManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:user/role' from dual
    where not exists (select 1 from eb_menu where menu_id = 'userManage_relateRoles');
-- 初始化角色管理
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time)
  select 'roleManage', '角色管理', '1', 'systemManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_list', '角色管理-查看角色', '2', 'roleManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:role' from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_list');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_save', '角色管理-新增角色', '2', 'roleManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:role/add' from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_save');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_delete', '角色管理-删除角色', '2', 'roleManage', date_format(now(), '%Y%m%d%H%i%s'), 'DELETE:role/*' from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_delete');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_allMenus', '角色管理-获取所有权限信息', '2', 'roleManage', date_format(now(), '%Y%m%d%H%i%s'), 'GET:menu' from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_allMenus');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_ownedMenus', '角色管理-获取已拥有的权限信息', '2', 'roleManage', date_format(now(), '%Y%m%d%H%i%s'), 'GET:role/auth/*' from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_ownedMenus');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'roleManage_relateMenus', '角色管理-关联权限', '2', 'roleManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:role/auth' from dual
    where not exists (select 1 from eb_menu where menu_id = 'roleManage_relateMenus');
-- 初始化日志管理
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time)
  select 'logManage', '日志管理', '1', 'systemManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_menu where menu_id = 'logManage');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'logManage_list', '日志管理-查看', '2', 'logManage', date_format(now(), '%Y%m%d%H%i%s'), 'POST:log' from dual
    where not exists (select 1 from eb_menu where menu_id = 'logManage_list');
insert into eb_menu (menu_id, menu_name, menu_type, parent_menu, create_time, url)
  select 'logManage_delete', '日志管理-删除日志', '2', 'logManage', date_format(now(), '%Y%m%d%H%i%s'), 'DELETE:log/*' from dual
    where not exists (select 1 from eb_menu where menu_id = 'logManage_delete');

-- 初始化用户角色表
insert into eb_user_role (user_name, role_id, create_time)
  select 'root', 'admin', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_user_role where user_name = 'root' and role_id = 'admin');
insert into eb_user_role (user_name, role_id, create_time)
  select 'root', 'base', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_user_role where user_name = 'root' and role_id = 'base');

-- 初始化基础角色权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'base', 'updatePwd', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'base' and menu_id = 'updatePwd');
-- 初始化角色菜单表
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'systemManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'systemManage');
-- 初始化用户管理权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_view', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_view');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_update', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_update');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_updateState', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_updateState');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_add', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_add');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_allRoles', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_allRoles');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_roles', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_roles');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'userManage_relateRoles', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'userManage_relateRoles');
-- 初始化角色管理权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_list', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_list');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_save', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_save');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_delete', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_delete');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_allMenus', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_allMenus');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_ownedMenus', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_ownedMenus');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'roleManage_relateMenus', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'roleManage_relateMenus');
-- 初始化日志管理权限关联
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'logManage', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'logManage');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'logManage_list', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'logManage_list');
insert into eb_role_menu (role_id, menu_id, create_time)
  select 'admin', 'logManage_delete', date_format(now(), '%Y%m%d%H%i%s') from dual
    where not exists (select 1 from eb_role_menu where role_id = 'admin' and menu_id = 'logManage_delete');
