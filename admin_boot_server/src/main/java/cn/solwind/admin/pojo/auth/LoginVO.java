package cn.solwind.admin.pojo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 登录请求VO
 */
@Getter
@Setter
@ApiModel("登录参数")
public class LoginVO {

    /**
     * 用户名
     */
    @NotNull(message = "请输入用户名!")
    @ApiModelProperty(value="用户名", required = true)
    private String userName;

    /**
     * 密码
     */
    @NotNull(message = "请输入密码!")
    @ApiModelProperty(value="密码", required = true)
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "请输入验证码!")
    @ApiModelProperty(value="验证码", required = true)
    private String verifyCode;

    /**
     * 验证码KEY
     */
    @ApiModelProperty(value="验证码随机key", required = true)
    private String verifyKey;
}
