package com.rs.hr.common;

import cn.hutool.core.util.StrUtil;

/**
 * @author sxia
 * @Description: TODO(SQL过滤)
 * @date 2017-6-23 15:07
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StrUtil.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StrUtil.replace(str, "'", "");
        str = StrUtil.replace(str, "\"", "");
        str = StrUtil.replace(str, ";", "");
        str = StrUtil.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new RuntimeException("包含非法字符");
            }
        }

        return str;
    }
}
