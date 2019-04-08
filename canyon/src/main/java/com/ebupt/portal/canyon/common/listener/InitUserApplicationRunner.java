package com.ebupt.portal.canyon.common.listener;

import com.ebupt.portal.canyon.common.enums.UpdateEnum;
import com.ebupt.portal.canyon.common.util.EncryptUtil;
import com.ebupt.portal.canyon.common.util.SystemConstant;
import com.ebupt.portal.canyon.system.service.UserService;
import com.ebupt.portal.canyon.system.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 项目启动成功后初始化用户
 *
 * @author chy
 * @date 2019-03-19 14:27
 */
@Component
@Order(1)
@Slf4j
public class InitUserApplicationRunner implements ApplicationRunner {

	private final UserService userService;

	@Autowired
	public InitUserApplicationRunner(UserService userService) {
		this.userService = userService;
	}

	@Value("${login.salt}")
	private String salt;

	@Override
	public void run(ApplicationArguments args) {
		UserVo userVo = new UserVo("root", EncryptUtil.md5("root" + salt + "1qaz@WSX"),
				"", "", "", 0);
		UpdateEnum result = this.userService.save(userVo, false);
		if (result.getCode() == SystemConstant.UPDATE_ENUM_SUCCESS) {
			log.info("系统第一次运行，初始化用户root，密码为1qaz@WSX，请登录后第一时间完善用户信息并修改初始密码");
		}
	}
}
