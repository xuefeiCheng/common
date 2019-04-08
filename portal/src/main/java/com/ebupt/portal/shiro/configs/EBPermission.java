package com.ebupt.portal.shiro.configs;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EBPermission implements Permission, Serializable {

    protected static final String EBCARD_TOKEN = "*"; // 通配符
    protected static final String PART_DIVIDER_TOKEN = ":"; // 分隔符1 分开请求方式和URI
    protected static final String SUBPART_DIVIDER_TOKEN = "/"; // 分隔符2 URL分隔符
    protected static final boolean DEFAULT_CASE_SENSITIVE = true; // 大小写敏感
    private List<List<String>> parts;

    protected EBPermission() {
    }

    public EBPermission(String ebcardString) {
        this(ebcardString, DEFAULT_CASE_SENSITIVE);
    }

    public EBPermission(String ebcardString, boolean caseSensitive) {
        this.setParts(ebcardString, caseSensitive);
    }

    protected void setParts(String ebcardString, boolean caseSensitive) {
        ebcardString = StringUtils.clean(ebcardString); // 规整字符串
        if (ebcardString != null && !ebcardString.isEmpty()) {
            if (!caseSensitive) { // 大小写不敏感则统一转小写
                ebcardString = ebcardString.toLowerCase();
            }

            List<String> parts = CollectionUtils.asList(ebcardString.split(PART_DIVIDER_TOKEN));
            this.parts = new ArrayList();
            Iterator var4 = parts.iterator();

            while(var4.hasNext()) {
                String part = (String)var4.next();
                List<String> subparts = CollectionUtils.asList(part.split(SUBPART_DIVIDER_TOKEN));
                if (subparts.isEmpty()) {
                    throw new IllegalArgumentException("Wildcard string cannot contain parts with only dividers. Make sure permission strings are properly formatted.");
                }

                this.parts.add(subparts);
            }

            if (this.parts.isEmpty()) {
                throw new IllegalArgumentException("Wildcard string cannot contain only dividers. Make sure permission strings are properly formatted.");
            }
        } else {
            throw new IllegalArgumentException("Wildcard string cannot be null or empty. Make sure permission strings are properly formatted.");
        }
    }

    protected List<List<String>> getParts() {
        return this.parts;
    }

    public boolean implies(Permission p) {
        if (!(p instanceof EBPermission)) {
            return false;
        } else {
            EBPermission wp = (EBPermission) p;
            List<List<String>> otherParts = wp.getParts(); // 请求权限
            List<List<String>> parts = this.getParts(); // 拥有的权限

            if (parts.size() != otherParts.size() || parts.size() != 2) {
                return false;
            }
            if (!(parts.get(0).get(0)).equals(otherParts.get(0).get(0))) { // 请求方式判断
                return false;
            }

            List<String> otherUri = otherParts.get(1);
            List<String> uri = parts.get(1);
            if (otherUri.size() != uri.size()) { // URI长度
                return false;
            } else {
                for (int i = 0; i < otherUri.size(); i++) {
                    if (!EBCARD_TOKEN.equals(uri.get(i)) && !(uri.get(i)).equals(otherUri.get(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Iterator var2 = this.parts.iterator();

        while(var2.hasNext()) {
            List<String> part = (List<String>) var2.next();
            if (buffer.length() > 0) {
                buffer.append(PART_DIVIDER_TOKEN);
            }

            Iterator partIt = part.iterator();

            while(partIt.hasNext()) {
                buffer.append((String)partIt.next());
                if (partIt.hasNext()) {
                    buffer.append(SUBPART_DIVIDER_TOKEN);
                }
            }
        }

        return buffer.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof EBPermission) {
            EBPermission wp = (EBPermission)o;
            return this.parts.equals(wp.parts);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.parts.hashCode();
    }

}
