package com.ebupt.portal.canyon.system.dao;

import com.ebupt.portal.canyon.system.entity.User;
import com.ebupt.portal.canyon.system.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 用户管理持久层
 *
 * @author chy
 * @date 2019-03-07 20:58
 */
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


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
	 * @param userName
	 *                  用户名
	 * @param password
	 *                  用户密码
	 * @param salt
	 *                  盐值
	 * @param time
	 *                  当前时间
	 * @return
	 *                  成功记录数
	 */
	@Modifying
	@Query("update User u set u.password = :password, u.salt = :salt, u.lastPwdTime = :time " +
			"where u.userName = :userName")
	void updatePwd(@Param("userName") String userName, @Param("password") String password,
	              @Param("salt") String salt, @Param("time") String time);

	/**
	 * 根据用户名修改用户信息
	 *
	 * @param user
	 *                  用户信息
	 * @return
	 *                  成功记录数
	 */
	@Modifying
	@Query("update User u set u.department = :#{#user.department}, u.realName = :#{#user.realName}, " +
			"u.contact = :#{#user.contact}, u.pwdValidity = :#{#user.pwdValidity} where u.userName = :#{#user.userName}")
	void updateUser(@Param("user") UserVo user);

	/**
	 * 根据用户名修改用户状态
	 *
	 * @param userName
	 *                  用户名
	 * @param state
	 *                  用户状态
	 * @return
	 *                  成功记录数
	 */
	@Modifying
	@Query("update User u set u.state = :state where u.userName = :userName")
	void updateState(@Param("userName") String userName, @Param("state") String state);

	/**
	 * 根据用户名修改最后一次登陆时间
	 *
	 * @param userName
	 *                      用户名
	 * @param loginTime
	 *                      登录时间
	 */
	@Modifying
	@Query("update User u set u.lastLoginTime = :loginTime where u.userName = :userName")
	void updateLoginTime(@Param("userName") String userName, @Param("loginTime") String loginTime);

}
