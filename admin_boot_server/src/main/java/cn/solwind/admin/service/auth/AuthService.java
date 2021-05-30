package cn.solwind.admin.service.auth;

import cn.solwind.admin.common.Constants;
import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.entity.SysFunction;
import cn.solwind.admin.entity.SysUser;
import cn.solwind.admin.entity.Sysbook;
import cn.solwind.admin.jwt.JwtTokenUtils;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.mapper.SysFunctionMapper;
import cn.solwind.admin.mapper.SysRoleMapper;
import cn.solwind.admin.mapper.SysUserMapper;
import cn.solwind.admin.mapper.SysbookMapper;
import cn.solwind.admin.pojo.auth.AuthVO;
import cn.solwind.admin.pojo.auth.ButtonVO;
import cn.solwind.admin.pojo.auth.ChangePwdVO;
import cn.solwind.admin.pojo.auth.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
    AuthenticationManager authenticationManager;

    @Resource
    UserDetailsService userDetailsService;

    @Resource
    JwtTokenUtils jwtTokenUtil;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysFunctionMapper sysFunctionMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysbookMapper sysbookMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param ip
     * @return
     */
    public String login(String username, String password, String ip) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(username);
        // 更新用户登录信息
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userDetails.getId());
        sysUser.setLastLoginIp(ip);
        sysUser.setLastLoginTime(new Date());
        sysUserMapper.updateByPrimaryKey(sysUser);

        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * 根据ID获取用户信息
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
        List<SysFunction> listFunc = sysFunctionMapper.selectAllFuncByUserId(userId);
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
        String[] arrRoles = sysRoleMapper.selectAllRoleCodeByUserId(userId);
        authVO.setRoles(arrRoles);

        // FIXME 此处以后修改为从缓存获取数据，增加启动及定时更新缓存逻辑
        // 获取数据字典内容
        Example example = new Example(Sysbook.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", Constants.COMMON_VALID);
        List<Sysbook> listSysbook = sysbookMapper.selectByExample(example);
        // 组成Map
        Map<String, String> mapSysbook = new HashedMap<>();
        listSysbook.forEach((sysbook -> {
            mapSysbook.put(sysbook.getTypeCode() + "|" + sysbook.getListCode(), sysbook.getListName());
        }));
        authVO.setSysbook(mapSysbook);

        return authVO;
    }

    /**
     * 修改密码
     *
     * @param changePwdVO
     * @return
     */
    public Response changePassword(ChangePwdVO changePwdVO) {
        // 查询用户
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);

        if(sysUser == null) {
            // 未找到用户
            log.error("未找到当前用户!Id{}", userId);
            return ResponseCode.PASSWORD_ERROR.build();
        }

        // 验证原密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(changePwdVO.getOldPassword(),sysUser.getPassword())) {
            // 密码验证失败
            log.info("用户原密码错误! Id{}", userId);
            return ResponseCode.PASSWORD_ERROR.build();
        }

        // 验证新密码两次输入是否一致
        if(!changePwdVO.getNewPassword().equals(changePwdVO.getRepeatNewPassword())) {
            // 两次输入密码不一致
            log.info("两次输入密码不一致! Id{}", userId);
            return ResponseCode.REPEAT_PASSWORD_ERROR.build();
        }

        // 更新新密码
        sysUser.setPassword(encoder.encode(changePwdVO.getNewPassword()));
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKey(sysUser);

        return ResponseCode.SUCCESS.build();
    }
}
