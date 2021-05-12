package cn.solwind.admin.pojo.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 修改密码VO
 */
@Getter
@Setter
public class ChangePwdVO {

    @NotNull(message = "Please input Old password!")
    private String oldPassword;     // 旧密码

    @NotNull(message = "Please input new password!")
    private String newPassword;     // 新密码

    @NotNull(message = "Please input repeat new password!")
    private String repeatNewPassword;    // 重复新密码
}
