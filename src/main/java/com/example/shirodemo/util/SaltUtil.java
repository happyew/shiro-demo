package com.example.shirodemo.util;

import cn.hutool.core.util.RandomUtil;

/**
 * 盐工具类
 */
public class SaltUtil {
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~%^*()_-+0123456789".toCharArray();

    public static String getSalt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(CHARS[RandomUtil.randomInt(CHARS.length)]);
        }
        return sb.toString();
    }
}
