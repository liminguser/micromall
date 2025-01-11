package com.example.micromall.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.UUID;

public class StringUtils extends StrUtil {

    /**
     * 生成UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 将集合转换为字符串，使用逗号分隔
     */
    public static String join(Collection<?> collection) {
        return join(collection, ",");
    }

    /**
     * 将集合转换为字符串，使用指定分隔符
     */
    public static String join(Collection<?> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object item : collection) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否为空或空白字符
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空且不为空白字符
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 截取字符串
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    /**
     * 格式化文本
     */
    public static String format(String template, Object... params) {
        if (isEmpty(template) || params == null || params.length == 0) {
            return template;
        }
        return String.format(template, params);
    }
} 