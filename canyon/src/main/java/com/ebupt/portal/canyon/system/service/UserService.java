package com.ebupt.portal.canyon.system.service;

import com.ebupt.portal.canyon.common.dto.JsonResult;
import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.vo.PageVo;
import com.ebupt.portal.canyon.system.entity.User;
import com.ebupt.portal.canyon.system.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理业务层接口
 *
 * @author chy
 * @date 2019-03-07 20:51
 */
public interface UserService {

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param userName
	 *                  用户名
	 * @return
	 *                  用户信息
	 */
	User findByUserName(String userName);

	/**
	 * 根据用户名修改用户密码
	 *
	 * @param pwdVo
	 *                      密码信息
	 * @return
	 *                      操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum updatePwd(PwdVo pwdVo);

	/**
	 * 根据用户名修改用户信息
	 *
	 * @param user
	 *              用户信息
	 * @return
	 *              操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum updateUser(UserVo user);

	/**
	 * 根据用户名修改用户状态
	 *
	 * @param userName
	 *                  用户名
	 * @param state
	 *                  用户状态
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum updateState(String userName, String state);

	/**
	 * 新增用户信息
	 *
	 * @param user
	 *              用户信息
	 * @param initRole
	 *              是否初始化角色
	 * @return
	 *              操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum save(UserVo user, boolean initRole);

	/**
	 * 用户登录
	 *
	 * @param request
	 *                  HttpServletRequest
	 * @param loginVo
	 *                  登录信息
	 * @return
	 *                  登录结果
	 */
	@Transactional(rollbackFor = Exception.class)
	JsonResult login(HttpServletRequest request, LoginVo loginVo);

	/**
	 * 条件查询用户信息
	 *
	 * @param page
	 *              查询信息
	 */
	Page<User> findByPage(PageVo<UserSearchVo> page);

	/**
	 * 根据用户名修改用户状态
	 *
	 * @param userStateVo
	 *                  用户状态信息
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum updateUserState(UserStateVo userStateVo);

	/**
	 * 根据用户名修改用户信息
	 *
	 * @param userVo
	 *                  用户信息
	 * @return
	 *                  操作结果
	 */
	@Transactional(rollbackFor = Exception.class)
	UpdateEnum update(UserVo userVo);
}
