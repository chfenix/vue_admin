package cn.solwind.admin.pojo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单树展示VO
 */
@Getter
@Setter
@ApiModel("菜单树")
public class MenuTreeVO {

    public MenuTreeVO() {}

    public MenuTreeVO(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value="菜单ID")
    private Long id;

    @ApiModelProperty(value="菜单标题")
    private String title;

    @ApiModelProperty(value="父菜单ID")
    private Long parentId;

    @ApiModelProperty(value="子菜单列表")
    private List<MenuTreeVO> children;

    /**
     * 增加子节点
     * @param menu
     */
    public void addChild(MenuTreeVO menu) {
        if(children == null) {
            children = new ArrayList<>();
        }
        children.add(menu);
    }

}
