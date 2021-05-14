package cn.solwind.admin.pojo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 修改密码VO
 */
@Getter
@Setter
@ApiModel("修改密码参数")
public class ChangePwdVO {

    @NotNull(message = "请输入旧密码")
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;     // 旧密码

    @NotNull(message = "请输入新密码")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;     // 新密码

    @NotNull(message = "请重复输入新密码")
    @ApiModelProperty(value = "重复新密码", required = true)
    private String repeatNewPassword;    // 重复新密码
}
