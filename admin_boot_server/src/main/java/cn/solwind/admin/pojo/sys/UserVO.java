package cn.solwind.admin.pojo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户VO
 */
@Getter
@Setter
@ToString
@ApiModel("系统用户")
public class UserVO {

    @ApiModelProperty(value = "ID")
    private Long id;    // ID

    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    private String userName;    // 用户名

    @ApiModelProperty(value = "姓名")
    @NotNull(message = "姓名不能为空")
    private String name;    // 姓名

    @ApiModelProperty(value = "邮箱", required = true)
    @NotNull(message = "邮箱地址不能为空")
    private String email;   // EMAIL

    @ApiModelProperty(value = "联系电话")
    private String phone;   // 电话

    @ApiModelProperty(value = "状态")
    private Integer status; // 状态

    @ApiModelProperty(value = "备注")
    private String remark;  // 备注

    @ApiModelProperty(value = "角色")
    @NotNull(message = "必须至少选择一个角色")
    private List<Long> roles;   // 角色
}
