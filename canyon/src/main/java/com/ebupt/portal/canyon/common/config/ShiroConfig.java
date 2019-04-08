package com.ebupt.portal.canyon.common.config;

import com.ebupt.portal.canyon.common.filter.LoginAndRedirectFilter;
import com.ebupt.portal.canyon.common.filter.PermissionsAndRedirectFilter;
import com.ebupt.portal.canyon.common.shiro.*;
import com.ebupt.portal.canyon.common.util.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 *
 * @author chy
 * @date 2019-03-10 13:08
 */
@Slf4j
@Configuration
public class ShiroConfig {

	private final EbShiroRealm ebShiroRealm;
	private final ShiroSessionDao shiroSessionDao;
	private final RedisCacheManager redisCacheManager;
	private final CacheManager cacheManager;

	@Lazy
	@Autowired
	public ShiroConfig(EbShiroRealm ebShiroRealm, ShiroSessionDao shiroSessionDao,
	                   RedisCacheManager redisCacheManager, CacheManager cacheManager) {
		this.ebShiroRealm = ebShiroRealm;
		this.shiroSessionDao = shiroSessionDao;
		this.redisCacheManager = redisCacheManager;
		this.cacheManager = cacheManager;
	}

	@Value("${spring.profiles.active:prod}")
	private String env;
	@Value("${spring.cache.type:ehcache}")
	private String cacheType;
	private static final String REDIS = "redis";

	/**
	 * Shiro生命周期处理器
	 *
	 * @return
	 *          LifecycleBeanPostProcessor对象
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * ehCache缓存管理器
	 *
	 * @return
	 *          ehCacheManager
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManager(cacheManager);
		return ehCacheManager;
	}

	/**
	 * 自定义session工厂
	 *
	 * @return
	 *              sessionFactory
	 */
	@Bean
	public SessionFactory shiroSessionFactory() {
		return new ShiroSessionFactory();
	}

	/**
	 * 配置安全管理
	 *
	 * @return
	 *          SecurityManager对象
	 */
	@Bean
	public SecurityManager securityManager() {
		// 配置shiro密码校验
		this.ebShiroRealm.setCredentialsMatcher(new EbCredentialsMatcher());
		// 设置权限比较器
		this.ebShiroRealm.setPermissionResolver(new EbWildcardPermissionResolver());
		// 设置缓存名称
		this.ebShiroRealm.setAuthenticationCacheName("auth_login");
		this.ebShiroRealm.setAuthorizationCacheName("auth_perms");

		// 配置核心安全事务管理
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 指定realm
		securityManager.setRealm(ebShiroRealm);
		// 指定session管理器
		securityManager.setSessionManager(sessionManager());
		// 指定缓存管理器
		if (REDIS.equals(cacheType)) {
			securityManager.setCacheManager(redisCacheManager);
		} else {
			securityManager.setCacheManager(ehCacheManager());
		}

		return securityManager;
	}

	/**
	 * 配置session管理器
	 *
	 * @return
	 *          Session管理器
	 */
	@Bean
	public SessionManager sessionManager () {
		EbWebShiroSessionManager sessionManager = new EbWebShiroSessionManager();
		if (REDIS.equals(cacheType)) {
			// 指定session dao
			sessionManager.setSessionDAO(shiroSessionDao);
			// 指定sessionFactory
			sessionManager.setSessionFactory(shiroSessionFactory());
		}
		return sessionManager;
	}

	/**
	 * 配置Filter工厂，设置对应的过滤和跳转条件
	 *
	 * @return
	 *          ShiroFilterFactoryBean对象
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 设置未登录和没有权限的跳转url
		shiroFilterFactoryBean.setLoginUrl("/auth/noLogin");
		shiroFilterFactoryBean.setUnauthorizedUrl("/auth/noAuth");

		// 配置自定义过滤器
		Map<String, Filter> filterMap = new LinkedHashMap<>();
		filterMap.put("authc", new LoginAndRedirectFilter());
		filterMap.put("perms", new PermissionsAndRedirectFilter());
		shiroFilterFactoryBean.setFilters(filterMap);

		// 配置过滤器过滤规则
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 开发环境中，不对swagger相关请求进行校验
		if (SystemConstant.DEV.equals(env)) {
			log.info("开发环境，启用swagger");
			// druid相关请求
			filterChainDefinitionMap.put("/swagger-ui.html", "anon");
			filterChainDefinitionMap.put("/webjars/**", "anon");
			filterChainDefinitionMap.put("/swagger-resources/**", "anon");
			filterChainDefinitionMap.put("/v2/api-docs", "anon");
		}
		// druid相关请求
		filterChainDefinitionMap.put("/druid/login.html", "authc,perms");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/auth/**", "anon");
		filterChainDefinitionMap.put("/favicon.ico", "anon");
		filterChainDefinitionMap.put("/**", "authc,perms");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

}
