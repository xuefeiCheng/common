package com.ebupt.portal.canyon.common.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * shiro自定义session管理器
 *
 * @author chy
 * @date 2019-03-15 10:49
 */
@Slf4j
public class EbWebShiroSessionManager extends DefaultWebSessionManager {

	private static final String AUTHORIZATION = "token";
	private static final String HEADER_SESSION_ID_SOURCE = "header";

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 从Header中获取
		String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
		if (StringUtils.isEmpty(id)) {
			// 从请求参数中获取
			id = request.getParameter(AUTHORIZATION);
			if (StringUtils.isEmpty(id)) {
				Serializable serializable = super.getSessionId(request, response);
				if (serializable != null) {
					id = serializable.toString();
				}
			} else {
				request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
						ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
			}
		} else {
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
					HEADER_SESSION_ID_SOURCE);
		}

		if (StringUtils.isNotEmpty(id)) {
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
		}
		return super.getSessionId(request, response);
	}

	@Override
	protected void onStop(Session session, SessionKey key) {
		super.onStop(session, key);
	}

	/**
	 * 获取session，优化单次请求需要多次访问redis的问题
	 *
	 * @param sessionKey
	 *                      sessionKey
	 * @return
	 *                      Session对象
	 * @throws UnknownSessionException
	 *                      未知session异常
	 */
	@Override
	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
		// 获取sessionId
		Serializable sessionId = getSessionId(sessionKey);

		ServletRequest request = null;
		// 在Web下使用Shiro时这个SessionKey是WebSessionKey类型的
		if (sessionKey instanceof WebSessionKey) {
			request = ((WebSessionKey) sessionKey).getServletRequest();
		}

		// 尝试从request中获取session
		if (request != null && sessionId != null) {
			Object sessionObj = request.getAttribute(sessionId.toString());
			if (sessionObj != null) {
				if (log.isDebugEnabled()) {
					log.debug("从request中获取到session: {}", sessionId);
				}
				return (Session) sessionObj;
			}
		}

		// 从request中获取session失败，则从redis中获取session，并把获取到的session存储到request中方便下次获取
		Session session = super.retrieveSession(sessionKey);
		if (request != null && sessionId != null) {
			if (log.isDebugEnabled()) {
				log.debug("从redis中获取到session并存储到request中: {}", sessionId);
			}
			request.setAttribute(sessionId.toString(), session);
		}

		return session;
	}
}
