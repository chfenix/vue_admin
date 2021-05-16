package cn.solwind.admin.pojo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色VO
 */
@Getter
@Setter
@ToString
@ApiModel("角色")
public class RoleVO {

    @ApiModelProperty(value = "角色主键")
    private Long id;    // 角色主键

    @ApiModelProperty(value = "角色Code", required = true)
    @NotNull(message = "请输入角色CODE")
    private String code;    // Code

    @ApiModelProperty(value = "角色名称", required = true)
    @NotNull(message = "请输入角色名称")
    private String name;    // 名称

    @ApiModelProperty(value = "角色描述")
    private String remark;  // 描述

    @ApiModelProperty(value = "选中的权限ID")
    private List<Long> checkedMenus;    // 选中的权限ID
}
