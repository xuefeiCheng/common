package com.ebupt.portal.canyon.system.service;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.entity.Role;
import com.ebupt.portal.canyon.system.vo.RoleSearchVo;
import com.ebupt.portal.canyon.system.vo.RoleVo;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理业务层接口
 *
 * @author chy
 * @date 2019-03-22 15:13
 */
public interface RoleService {

	/**
	 * 获取所有的角色信息
	 *
	 * @return
	 *          角色集合
	 */
	List<Role> findAll();

	/**
	 * 分页查询角色信息
	 *
	 * @param roleSearchVo
	 *                      角色查询信息
	 * @return
	 *                      查询结果
	 */
	Page<Role> findByPage(PageVo<RoleSearchVo> roleSearchVo);

	/**
	 * 新增角色
	 *
	 * @param roleVo
	 *                  角色信息
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum save(RoleVo roleVo);

	/**
	 * 根据角色ID删除角色
	 *
	 * @param roleId
	 *              角色ID
	 * @return
	 *              操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum deleteByRoleId(String roleId);
}
