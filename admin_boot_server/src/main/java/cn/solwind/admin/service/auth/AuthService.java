package cn.solwind.admin.service.auth;

import cn.solwind.admin.common.Constants;
import cn.solwind.admin.entity.SysFunction;
import cn.solwind.admin.entity.SysUser;
import cn.solwind.admin.jwt.JwtTokenUtils;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.mapper.SysFunctionMapper;
import cn.solwind.admin.mapper.SysRoleMapper;
import cn.solwind.admin.mapper.SysUserMapper;
import cn.solwind.admin.pojo.auth.AuthVO;
import cn.solwind.admin.pojo.auth.ButtonVO;
import cn.solwind.admin.pojo.auth.MenuVO;
import javafx.scene.control.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录相关Service
 */
@Service
@Slf4j
public class AuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenUtils jwtTokenUtil;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysFunctionMapper sysFunctionMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * 根据ID获取用户信息
     * @param userId
     * @return
     */
    public AuthVO findUserInfo() {
        AuthVO authVO = new AuthVO();
        // 获取当前登录用户ID
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        log.info("User[{}] get info!", userId);

        // 查询用户基本信息
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        authVO.setName(sysUser.getName());
        authVO.setAvatar(sysUser.getAvatar());

        // 查询用户权限
        List<SysFunction> listFunc = sysFunctionMapper.findAllFuncByUserId(userId);
        // 封装用户权限
        List<ButtonVO> listButtons = new ArrayList<>();
        List<MenuVO> listTmpMenus = new ArrayList<>();

        if(listFunc != null) {
            for (SysFunction objFunc : listFunc) {
                if(objFunc.getType().equals(Constants.FUNC_TYPE_MENU)) {
                    // 功能权限类型为Menu
                    MenuVO objMenu = new MenuVO();
                    BeanUtils.copyProperties(objFunc, objMenu);
                    listTmpMenus.add(objMenu);
                } else if (objFunc.getType().equals(Constants.FUNC_TYPE_BUTTON)) {
                    // 功能权限类型为Button
                    ButtonVO objButton = new ButtonVO();
                    BeanUtils.copyProperties(objFunc, objButton);
                    listButtons.add(objButton);
                }
            }
        }

        // 将MenuList组成树形结构
        List<MenuVO> listMenus = new ArrayList<>();
        // List转为Map
        Map<Long, MenuVO> mapMenus = listTmpMenus.stream().collect(Collectors.toMap(MenuVO::getId, menu -> menu));
        for (MenuVO menu : listTmpMenus) {
            MenuVO parentMenu = mapMenus.get(menu.getParentId());
            if(parentMenu == null) {
                // 根节点
                listMenus.add(menu);
                continue;
            }
            else {
                parentMenu.addChild(menu);
            }
        }
        mapMenus = null;
        listTmpMenus = null;

        authVO.setMenus(listMenus);
        authVO.setButtons(listButtons);

        // 获取用户所有角色
        String[] arrRoles = sysRoleMapper.findAllRoleCodeByUserId(userId);
        authVO.setRoles(arrRoles);

        return authVO;
    }
}
