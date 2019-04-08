package com.ebupt.portal.canyon.common.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Druid数据库连接池配置
 *
 * @author chy
 * @date 2019-02-28 11:14
 */
@Slf4j
@Configuration
public class DruidConfig {

	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver-class-name}")
	private String driver;
	@Value("${spring.datasource.platform}")
	private String platform;
	@Value("${spring.druid.initial-size}")
	private int initialSize;
	@Value("${spring.druid.max-active}")
	private int maxActive;
	@Value("${spring.druid.min-idle}")
	private int minIdle;
	@Value("${spring.druid.max-wait}")
	private int maxWait;
	@Value("${spring.druid.pool-prepared-statements}")
	private boolean poolPreparedStatements;
	@Value("${spring.druid.max-pool-prepared-statement-per-connection-size}")
	private int maxPoolPreparedStatementPerConnectionSize;
	@Value("${spring.druid.validation-query}")
	private String validationQuery;
	@Value("${spring.druid.validation-query-timeout}")
	private int validationQueryTimeout;
	@Value("${spring.druid.test-on-borrow}")
	private boolean testOnBorrow;
	@Value("${spring.druid.test-on-return}")
	private boolean testOnReturn;
	@Value("${spring.druid.test-while-idle}")
	private boolean testWhileIdle;
	@Value("${spring.druid.keep-alive}")
	private boolean keepAlive;
	@Value("${spring.druid.time-between-eviction-runs-millis}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${spring.druid.min-evictable-idle-time-millis}")
	private int minEvictableIdleTimeMillis;
	@Value("${spring.druid.filters}")
	private String filters;
	@Value("${spring.druid.username}")
	private String druidUsername;
	@Value("${spring.druid.password}")
	private String druidPassword;

	private static final String PLATFORM_IFX = "informix";

	@Bean
	@Primary
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		// 设置连接信息
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		dataSource.setDriverClassName(this.driver);

		// 设置配置信息
		dataSource.setInitialSize(this.initialSize);
		dataSource.setMaxActive(this.maxActive);
		dataSource.setMinIdle(this.minIdle);
		dataSource.setMaxWait(this.maxWait);
		dataSource.setPoolPreparedStatements(this.poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
		if (PLATFORM_IFX.equals(platform)) {
			dataSource.setValidationQuery("select count(*) from systables");
		} else {
			dataSource.setValidationQuery(this.validationQuery);
		}
		dataSource.setValidationQueryTimeout(this.validationQueryTimeout);
		dataSource.setTestOnBorrow(this.testOnBorrow);
		dataSource.setTestOnReturn(this.testOnReturn);
		dataSource.setTestWhileIdle(this.testWhileIdle);
		dataSource.setKeepAlive(this.keepAlive);
		dataSource.setTimeBetweenConnectErrorMillis(this.timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
		try {
			dataSource.setFilters(this.filters);
		} catch (SQLException e) {
			log.error("druid configuration initialization filter failed: {}", e);
		}

		return dataSource;
	}

	/**
	 * 注册一个StatViewServlet:用于展示Druid的统计信息
	 *
	 * @return
	 *          ServletRegistrationBean对象
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> statViewServlet() {
		// 创建Servlet注册实体
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
				new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

		// 配置登录名和密码
		servletRegistrationBean.addInitParameter("loginUsername", druidUsername);
		servletRegistrationBean.addInitParameter("loginPassword", druidPassword);

		// 配置resetEnable
		servletRegistrationBean.addInitParameter("resetEnable", "true");

		return servletRegistrationBean;
	}

	/**
	 * 注册一个WebStatFilter:用于采集web-jdbc关联监控的数据
	 *
	 * @return
	 *          FilterRegistrationBean对象
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> webStatFilter() {
		// 创建Filter注册实体
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean =
				new FilterRegistrationBean<>(new WebStatFilter());
		// 设置过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
		// 设置忽略过滤的格式信息
		filterRegistrationBean.addInitParameter("exclusions", "/druid/*");
		return filterRegistrationBean;
	}

	/**
	 * sql日志过滤器配置
	 *
	 * @return
	 *          Slf4jLogFilter对象
	 */
	@Bean
	public Slf4jLogFilter logFilter() {
		Slf4jLogFilter logFilter = new Slf4jLogFilter();
		// 配置是否展示可执行SQL语句
		logFilter.setStatementExecutableSqlLogEnable(true);
		// 配置是否显示SQL语句
		logFilter.setStatementLogEnabled(false);
		return logFilter;
	}

	/**
	 * sql监控过滤器配置
	 *
	 * @return
	 *          StatFilter对象
	 */
	@Bean
	public StatFilter statFilter() {
		StatFilter statFilter = new StatFilter();
		// 配置慢sql的执行时间
		statFilter.setSlowSqlMillis(3000);
		// 配置慢sql是否记录到日志中
		statFilter.setLogSlowSql(true);
		// 配置是否合并sql
		statFilter.setMergeSql(true);
		return statFilter;
	}

	/**
	 * sql防火墙过滤器配置
	 *
	 * @param wallConfig
	 *                      sql防火墙配置
	 * @return
	 *                      WallFilter对象
	 */
	@Bean
	public WallFilter wallFilter(WallConfig wallConfig) {
		WallFilter wallFilter = new WallFilter();
		wallFilter.setConfig(wallConfig);
		// 对被认为是攻击的SQL进行LOG.error输出
		wallFilter.setLogViolation(false);
		// 对被认为是攻击的SQL抛出SQLException
		wallFilter.setThrowException(true);
		return wallFilter;
	}

	/**
	 * sql防火墙配置
	 *
	 * @return
	 *              WallConfig对象
	 */
	@Bean
	public WallConfig wallConfig() {
		WallConfig wallConfig = new WallConfig();
		wallConfig.setMergeAllow(false);
		wallConfig.setDescribeAllow(false);
		wallConfig.setShowAllow(false);
		return wallConfig;
	}
}
