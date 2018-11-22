package com.fanxquer.qblog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentMatcher {
    public static int matcherId(String text) {
        // 按指定模式在字符串查找
        String pattern = "(?<=<comment-id>)\\d+(?=</comment-id>)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 创建 matcher 对象
        Matcher m = r.matcher(text);
        if (m.find()) {
            return Integer.parseInt(m.group(0));
        } else {
           return 0;
        }
    }

    public static String getText(String text) {
        // 按指定模式在字符串查找
        System.out.println("<s>"+text+"</s>");
        String pattern = "(?<=</comment-id>).*";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 创建 matcher 对象
        Matcher m = r.matcher(text);
        if (m.find()) {
            text = m.group(0);
            return text;
        }
        else {
            return text;
        }
    }
}
