package cn.solwind.admin.pojo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 按钮VO
 */
@Getter
@Setter
@ApiModel("按钮（操作）信息")
public class ButtonVO {

    @ApiModelProperty(value="按钮ID")
    private Long id;

    @ApiModelProperty(value="按钮名称")
    private String name;

    @ApiModelProperty(value="按钮url")
    private String action;
}
