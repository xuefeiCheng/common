package com.ebupt.portal.canyon.common.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * redis实现共享session
 *
 * @author chy
 * @date 2019-03-14 15:17
 */
@Slf4j
@Component
public class ShiroSessionDao extends EnterpriseCacheSessionDAO {

	private final RedisTemplate<String, Object> redisTemplate;

	private static final String PREFIX = "ShiroSession:";
	private static final int SESSION_TIMEOUT = 3600;

	@Lazy
	@Autowired
	public ShiroSessionDao(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 创建session
	 *
	 * @param session
	 *                  Session对象
	 * @return
	 *                  sessionId
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		// 关联session和sessionId
		((ShiroSession) session).setId(sessionId);
		if (log.isDebugEnabled()) {
			log.debug("创建session并保存到redis中: {}", session.getId());
		}
		if (sessionId != null) {
			redisTemplate.opsForValue().set(PREFIX + sessionId.toString(), session);
		}
		return sessionId;
	}

	/**
	 * 获取session
	 *
	 * @param sessionId
	 *                  sessionId
	 * @return
	 *                  session
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = null;
		if (sessionId != null) {
			if (log.isDebugEnabled()) {
				log.debug("从redis中读取session: {}", sessionId);
			}
			session = (Session) redisTemplate.opsForValue().get(PREFIX + sessionId.toString());
		}
		return session;
	}

	/**
	 * 更新session的最后一次访问时间
	 *
	 * @param session
	 *                  sesison
	 */
	@Override
	public void doUpdate(Session session) throws UnknownSessionException {
		if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
			// 会话已停止或者已过期
			return;
		}

		if (session instanceof ShiroSession) {
			ShiroSession shiroSession = (ShiroSession) session;
			if (!shiroSession.isChanged()) {
				// 未改变属性值
				return;
			}
			// 重置为未改变属性值
			shiroSession.setChanged(false);
		}

		if (log.isDebugEnabled()) {
			log.debug("更新session: {}", session.getId());
		}
		if (session != null && session.getId() != null) {
			String key = PREFIX + session.getId();
			redisTemplate.opsForValue().set(key, session, SESSION_TIMEOUT, TimeUnit.SECONDS);
		}
	}

	/**
	 * 删除session
	 *
	 * @param session
	 *                  session
	 */
	@Override
	public void doDelete(Session session) {
		if (log.isDebugEnabled()) {
			log.error("删除session: {}", session.getId());
		}
		if (session != null && session.getId() != null) {
			redisTemplate.delete(PREFIX + session.getId());
		}
	}
}
