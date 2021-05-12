package cn.solwind.admin.common;

/**
 * 常量类
 */
public class Constants {

    public static final int GlobalSessionTimeout = 1000 * 60 * 30;
    public static final int SessionValidationInterval = 1000 * 60 * 30;

    /**
     * 通用有效，无效
     */
    public static final Integer COMMON_VALID = 1;       // 有效
    public static final Integer COMMON_INVALID = 0;     // 无效

    /**
     * 权限功能类型
     */
    public static final Integer FUNC_TYPE_MENU = 1;     // 菜单
    public static final Integer FUNC_TYPE_BUTTON = 2;   // 按钮

}
