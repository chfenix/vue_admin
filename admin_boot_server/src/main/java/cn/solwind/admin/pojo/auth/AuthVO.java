package cn.solwind.admin.pojo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 登录用户信息VO
 */
@Getter
@Setter
@ApiModel("登录用户信息")
public class AuthVO {

    @ApiModelProperty(value="姓名")
    private String name;    // 姓名

    @ApiModelProperty(value="头像")
    private String avatar;  // 头像

    @ApiModelProperty(value="角色数组")
    private String[] roles; // 角色

    @ApiModelProperty(value="菜单List")
    private List<MenuVO> menus;

    @ApiModelProperty(value="按钮List")
    private List<ButtonVO> buttons;

    @ApiModelProperty(value = "数据字典内容")
    private Map<String, String> sysbook;
}
