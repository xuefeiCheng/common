package com.ebupt.portal.canyon.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 日志记录实体类
 *
 * @author chy
 * @date 2019-03-08 14:56
 */
@Entity
@Table(name = "eb_log")
@Setter
@Getter
@NoArgsConstructor
public class Log {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 请求url
	 */
	@Column(length = 50)
	private String ip;

	/**
	 * 请求方式
	 */
	@Column(length = 50)
	private String method;

	/**
	 * 请求url
	 */
	@Column(length = 100)
	private String url;

	/**
	 * 描述
	 */
	@Column(length = 200, nullable = false)
	private String description;

	/**
	 * 请求处理时间
	 */
	@Column(nullable = false)
	private Long execTime;

	/**
	 * 操作用户
	 */
	@Column(length = 50)
	private String operator;

	/**
	 * 请求处理结果 1-成功 2-失败 3-异常
	 */
	@Column(length = 1, nullable = false)
	private String result;

	/**
	 * 请求失败原因
	 */
	@Column(length = 200)
	private String error;

	/**
	 * 请求时间
	 */
	@Column(length = 14, nullable = false)
	private String createTime;

	public Log(String ip, String method, String url, String description, Long execTime,
	           String operator, String result, String error, String createTime) {
		this.ip = ip;
		this.method = method;
		this.url = url;
		this.description = description;
		this.execTime = execTime;
		this.operator = operator;
		this.result = result;
		this.error = error;
		this.createTime = createTime;
	}
}
