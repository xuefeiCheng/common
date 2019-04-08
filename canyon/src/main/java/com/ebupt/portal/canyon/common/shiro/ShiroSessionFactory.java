package com.ebupt.portal.canyon.common.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * 自定义Shiro的sessionFactory
 *
 * @author chy
 * @date 2019-03-17 15:01
 */
public class ShiroSessionFactory implements SessionFactory {

	@Override
	public Session createSession(SessionContext initData) {
		ShiroSession session = new ShiroSession();
		return session;
	}
}
