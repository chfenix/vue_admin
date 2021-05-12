package cn.solwind.admin.pojo.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 登录请求VO
 */
@Getter
@Setter
public class LoginVO {

    /**
     * 用户名
     */
    @NotNull(message = "请输入用户名!")
    private String userName;

    /**
     * 密码
     */
    @NotNull(message = "请输入密码!")
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "请输入验证码!")
    private String verifyCode;

    /**
     * 验证码KEY
     */
    private String verifyKey;
}
