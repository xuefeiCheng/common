package com.ebupt.portal.canyon.system.service.impl;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.PageableUtil;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.dao.RoleDao;
import com.ebupt.portal.canyon.system.entity.Role;
import com.ebupt.portal.canyon.system.service.RoleMenuService;
import com.ebupt.portal.canyon.system.service.RoleService;
import com.ebupt.portal.canyon.system.service.UserRoleService;
import com.ebupt.portal.canyon.system.vo.RoleSearchVo;
import com.ebupt.portal.canyon.system.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理业务层实现
 *
 * @author chy
 * @date 2019-03-22 15:14
 */
@Service
public class RoleServiceImpl implements RoleService {

	private final UserRoleService userRoleService;
	private final RoleMenuService roleMenuService;
	private final RoleDao roleDao;

	@Autowired
	public RoleServiceImpl(UserRoleService userRoleService, RoleMenuService roleMenuService, RoleDao roleDao) {
		this.userRoleService = userRoleService;
		this.roleMenuService = roleMenuService;
		this.roleDao = roleDao;
	}

	@Override
	public List<Role> findAll() {
		return this.roleDao.findAll();
	}

	@Override
	public Page<Role> findByPage(PageVo<RoleSearchVo> roleSearchVo) {
		RoleSearchVo search = roleSearchVo.getSearch();
		Specification<Role> specification = ((root, query, criteriaBuilder) -> {
			List<Predicate> predicateList = new ArrayList<>();
			if (search != null) {
				if (StringUtils.isNotEmpty(search.getRoleName())) {
					predicateList.add(criteriaBuilder.like(root.get("roleName"), search.getRoleName() + "%"));
				}
				if (StringUtils.isNotEmpty(search.getStartTime())) {
					predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), search.getStartTime()));
				}
				if (StringUtils.isNotEmpty(search.getEndTime())) {
					predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), search.getEndTime()));
				}
			}
			Predicate[] predicates = new Predicate[predicateList.size()];
			return query.where(predicateList.toArray(predicates)).getRestriction();
		});

		return this.roleDao.findAll(specification, PageableUtil.getPageable(roleSearchVo));
	}

	@Override
	public UpdateEnum save(RoleVo roleVo) {
		Role role = new Role(roleVo.getRoleId(), roleVo.getRoleName());
		this.roleDao.save(role);
		return UpdateEnum.SUCCESS;
	}

	@Override
	public UpdateEnum deleteByRoleId(String roleId) {
		this.roleDao.deleteByRoleId(roleId);
		this.userRoleService.deleteByRoleId(roleId);
		this.roleMenuService.deleteByRoleId(roleId);
		return UpdateEnum.SUCCESS;
	}
}
