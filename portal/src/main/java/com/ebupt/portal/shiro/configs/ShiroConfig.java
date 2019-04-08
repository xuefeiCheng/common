package com.ebupt.portal.shiro.configs;

import com.ebupt.portal.shiro.filter.EBAuthenticationFilter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private final EBRealm realm;

    @Autowired
    public ShiroConfig(EBRealm realm) {
        this.realm = realm;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ecm = new EhCacheManager();
        ecm.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ecm;
    }

    /**
     * 将shiro生命周期交给spring boot管理
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 默认跳转
        shiroFilterFactoryBean.setLoginUrl("/auth/nologin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/noauth"); // 没有权限URL

        // 定义过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new EBAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/error", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/auth/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        EhCacheManager ehCacheManager = ehCacheManager();
        // 配置密码比较器
        realm.setCredentialsMatcher(new EBCredentialsMatcher());
        // 配置缓存管理器
        realm.setCacheManager(ehCacheManager);
        realm.setAuthenticationCacheName("authenticationCache"); // 认证缓存
        realm.setAuthorizationCacheName("authorizationCache"); // 权限缓存
        realm.setPermissionResolver(new EBPermissionResolver()); // 自定义权限校验

        // 配置核心安全事务管理
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);

        // 配置session管理
        securityManager.setSessionManager(new EBShiroSessionManager());
        // 配置缓存管理器
        securityManager.setCacheManager(ehCacheManager);

        return securityManager;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
