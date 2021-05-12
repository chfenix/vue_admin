package cn.solwind.admin.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单VO
 */
@Getter
@Setter
public class MenuVO {

    private Long id;

    private String code;

    private String title;

    private String icon;

    private Integer sort;

    private Long parentId;

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
