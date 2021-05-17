package cn.solwind.admin.common;

/**
 * 返回码定义类
 */
public enum ResponseCode {
    /**
     * 通用返回码
     */
    SUCCESS(0000, "成功"),            // 成功
    UNKNOW_ERROR(9999, "系统繁忙，请重试"),     // 未知错误
    PARAMS_ERROR(9998, "参数错误，请确认输入内容"),     // 参数错误
    INVALID_TOKEN(9997, "登录信息已失效，请重新登录"), // 无效TOKEN
    TOKEN_EXPIRED(9996, "登录信息已失效，请重新登录"),   // TOKEN已过期

    /**
     * 用户登录相关
     */
    LOGIN_FAIL(1001, "无效用户名，密码"),
    VERIFYCODE_ERROR(1002, "验证码错误，请重新输入"),
    OTHER_LOGIN(1003, "已经在其他客户端登录，请重新登录"),
    PASSWORD_ERROR(1004, "旧密码错误"),
    REPEAT_PASSWORD_ERROR(1005, "新密码和重复新密码不一致"),
    /**
     * ######################
     * 系统管理
     * ######################
     */
    SYSBOOK_CANNOT_MODIFY(2001,"此数据字典不允许修改"),
    SYSBOOK_DUPLICATE(2002,"TypeCode和ListCode组合重复"),
    ROLE_DUPLICATE(2001,"角色CODE或角色名称重复"),
    USER_DUPLICATE(2003,"用户名不能重复")
    ;


    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Response build() {
        Response response = new Response();
        response.setCode(code);
        response.setMessage(msg);
        return response;
    }

    public <T> Response build(T t) {
        Response response = new Response();
        response.setCode(code);
        response.setMessage(msg);
        response.setData(t);
        return response;
    }
}
