package cn.solwind.admin.pojo.sys;

import cn.solwind.admin.common.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("用户查询参数")
public class UserQuery extends PageParameter {

    @ApiModelProperty(value="用户名")
    private String userName;    // 用户名

    @ApiModelProperty(value="姓名")
    private String name;    // 姓名

    @ApiModelProperty(value="Email")
    private String email;   // Email

    @ApiModelProperty(value="用户角色")
    private String role;    // 用户角色
}
