package com.ebupt.portal.canyon.common.shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 权限比较器
 *
 * @author chy
 * @date 2019-03-14 11:27
 */
public class EbWildcardPermission  implements Permission, Serializable {

	/**
	 * 权限通配符
	 */
	private static final String WILDCARD_TOKEN = "*";
	/**
	 * 权限分隔符 GET:user/delete
	 */
	private static final String PART_DIVIDER_TOKEN = ":";
	/**
	 * 子权限分隔符
 	 */
	private static final String SUBPART_DIVIDER_TOKEN = "/";
	/**
	 * 大小写是否敏感
 	 */
	private static final boolean DEFAULT_CASE_SENSITIVE = true;
	/**
	 * 权限集合长度
	 */
	private static final int PART_LENGTH = 2;
	/**
	 * 该用户拥有的权限集合
 	 */
	private List<List<String>> parts;

	EbWildcardPermission(String wildcardString) {
		this(wildcardString, DEFAULT_CASE_SENSITIVE);
	}

	private EbWildcardPermission(String wildcardString, boolean caseSensitive) {
		setParts(wildcardString, caseSensitive);
	}

	private void setParts(String wildcardString, boolean caseSensitive) {
		wildcardString = StringUtils.clean(wildcardString);

		if (wildcardString == null || wildcardString.isEmpty()) {
			throw new IllegalArgumentException("Wildcard string cannot be null or empty. Make sure permission strings are properly formatted.");
		}

		if (!caseSensitive) {
			wildcardString = wildcardString.toLowerCase();
		}

		List<String> parts = CollectionUtils.asList(wildcardString.split(PART_DIVIDER_TOKEN));

		this.parts = new ArrayList<>();
		for (String part : parts) {
			List<String> subparts = CollectionUtils.asList(part.split(SUBPART_DIVIDER_TOKEN));

			if (subparts.isEmpty()) {
				throw new IllegalArgumentException("Wildcard string cannot contain parts with only dividers. Make sure permission strings are properly formatted.");
			}
			this.parts.add(subparts);
		}

		if (this.parts.isEmpty()) {
			throw new IllegalArgumentException("Wildcard string cannot contain only dividers. Make sure permission strings are properly formatted.");
		}
	}

	private List<List<String>> getParts() {
		return this.parts;
	}

	@Override
	public boolean implies(Permission p) {
		if (!(p instanceof EbWildcardPermission)) {
			return false;
		}

		// 获取访问所需权限
		EbWildcardPermission wp = (EbWildcardPermission) p;
		List<List<String>> otherParts = wp.getParts();

		// 获取用户已具有权限
		List<List<String>> parts = this.getParts();

		// 权限格式不一致，鉴权失败
		if (otherParts.size() != parts.size() || parts.size() != PART_LENGTH) {
			return false;
		}

		// 判断请求方式是否一致
		List<String> otherMethods = otherParts.get(0);
		List<String> methods = parts.get(0);
		if (otherMethods.size() == 1 && methods.size() == 1 && otherMethods.get(0).equals(methods.get(0))) {
			List<String> otherUrls = otherParts.get(1);
			List<String> urls = parts.get(1);

			// 请求url校验
			if (otherUrls.size() == urls.size()) {
				for (int i = 0; i < otherUrls.size(); i++) {
					String otherUrl = otherUrls.get(i);
					String url = urls.get(i);

					if (!WILDCARD_TOKEN.equals(url) && !url.equals(otherUrl)) {
						return false;
					}
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (List<String> part : parts) {
			if (buffer.length() > 0) {
				buffer.append(PART_DIVIDER_TOKEN);
			}
			Iterator<String> partIt = part.iterator();
			while(partIt.hasNext()) {
				buffer.append(partIt.next());
				if (partIt.hasNext()) {
					buffer.append(SUBPART_DIVIDER_TOKEN);
				}
			}
		}
		return buffer.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WildcardPermission) {
			EbWildcardPermission wp = (EbWildcardPermission) o;
			return parts.equals(wp.parts);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return parts.hashCode();
	}

}
