package com.babacit.alarm.update2;

import java.util.ArrayList;

/**
 * Created by hekd on 2018/1/8.
 */

public class StringUtils {
    /**
     * 把集合用,分割,转换为字符串
     *
     * @param stringList
     * @return
     */
    public static String listToString(ArrayList<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(","); // 分隔符
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
