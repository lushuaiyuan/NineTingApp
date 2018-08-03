package com.zzti.lsy.ninetingapp.utils;

public class StringUtil {
    /**
     * 判断字符串是否为null或空
     *
     * @param string
     * @returntrue:为空或null;false:不为空或null
     */
    public static boolean isNullOrEmpty(String string) {
        boolean flag = false;
        if (null == string || string.trim().length() == 0 || "null".equals(string)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isBland(CharSequence input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
