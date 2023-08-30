package com.minis.util;

public abstract class PatternMatchUtils {

    /**
     * 匹配带通配符 "*" 的字符串
     *
     * Match a String against the given pattern, supporting the following simple
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
     * arbitrary number of pattern parts), as well as direct equality.
     * @param pattern the pattern to match against
     * @param str the String to match
     * @return whether the String matches the given pattern
     */
    public static boolean simpleMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }
        int firstIndex = pattern.indexOf('*');
        // pattern 中不带 "*"，需要完全匹配一致
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        // pattern 的首字母是 "*"
        if (firstIndex == 0) {
            // pattern == "*", 则匹配所有字符串
            if (pattern.length() == 1) {
                return true;
            }
            // pattern 中下一个 "*" 的位置
            int nextIndex = pattern.indexOf('*', firstIndex + 1);
            // pattern 没有下一个 "*", 则 pattern 中 "*" 后面的子串要与 str 的后缀完全一致
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            // 截取 pattern 中两个 "*" 中间的子串 part
            String part = pattern.substring(1, nextIndex);
            if ("".equals(part)) {
                return simpleMatch(pattern.substring(nextIndex), str);
            }
            // 在 str 中找与 part 完全一致的子串的位置
            int partIndex = str.indexOf(part);
            // str 中存在与 part 完全一致的子串，继续递归匹配后面的部分
            while (partIndex != -1) {
                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            // str 中没有与 part 完全一致的子串，不匹配
            return false;
        }
        // pattern 的首字母不是 "*"，那么 "*" 前面的子串要完全匹配，"*" 后面的子串重新用递归匹配
        return (str.length() >= firstIndex &&
            pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) &&
            simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
    }

}
