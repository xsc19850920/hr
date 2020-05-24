package com.rs.hr.common;

import javax.print.DocFlavor;

public class Constant {
    /**
     * 超级管理员ID
     */
    public static final String SUPER_ADMIN = "1";
    public static final String TOKEN_NAME="SysUserToken:";
    /**
     * X-Token sys请求的token
     */
    public static  final String X_TOKEN="X-Token";
    public static final Byte DEL_FLAG_0 = 0;


    //24小时后过期
    public final static int EXPIRE = 86400;
    public static final String REDIS_SYS_CONFIG = "SysConfig:";
    public static final CharSequence SUFFIX_EXCEL = ".xls";

    /**
     * 用户 状态
     */
    public enum UserStatus {
        /**
         * 禁用
         */
        DISABLE(0),
        /**
         * 正常
         */
        NORMAL(1);

        private int value;

        UserStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
