package cn.solwind.admin.pojo.auth;

import cn.solwind.admin.entity.SysFunction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 登录用户信息VO
 */
@Getter
@Setter
public class AuthVO {

    private String name;    // 姓名

    private String avatar;  // 头像

    private String[] roles; // 角色

    private List<MenuVO> menus;

    private List<ButtonVO> buttons;
}
