package cn.solwind.admin.pojo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单VO
 */
@Getter
@Setter
@ApiModel("菜单信息")
public class MenuVO {

    @ApiModelProperty(value="菜单ID")
    private Long id;

    @ApiModelProperty(value="菜单CODE，与前端rote一致")
    private String code;

    @ApiModelProperty(value="菜单标题")
    private String title;

    @ApiModelProperty(value="图标")
    private String icon;

    @ApiModelProperty(value="排序")
    private Integer sort;

    @ApiModelProperty(value="父菜单ID")
    private Long parentId;

    @ApiModelProperty(value="子菜单列表")
    private List<MenuVO> children;

    /**
     * 增加子节点
     * @param menu
     */
    public void addChild(MenuVO menu) {
        if(children == null) {
            children = new ArrayList<>();
        }
        children.add(menu);
    }

}
