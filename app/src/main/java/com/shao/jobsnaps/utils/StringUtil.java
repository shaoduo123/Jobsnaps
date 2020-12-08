package com.shao.jobsnaps.utils;

/**
 * Created by shaoduo on 2017-08-23.
 */

public class StringUtil {

    /**
     * 功能：判断一个字符串是否包含特殊字符
     *
     * @param string 要判断的字符串
     * @return false 提供的参数string包含特殊字符
     */
    public static boolean isConSpeCharacters(String string) {
        // TODO Auto-generated method stub
        if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*.*", "").length() == 0) {  //也包括了.
            //如果不包含特殊字符
            return true;
        }
        return false;
    }
}
