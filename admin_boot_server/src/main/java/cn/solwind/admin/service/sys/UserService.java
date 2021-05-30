package cn.solwind.admin.service.sys;

import cn.solwind.admin.common.Constants;
import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.entity.SysUser;
import cn.solwind.admin.entity.SysUserRole;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.mapper.SysUserMapper;
import cn.solwind.admin.mapper.SysUserRoleMapper;
import cn.solwind.admin.pojo.sys.UserQuery;
import cn.solwind.admin.pojo.sys.UserRoleVO;
import cn.solwind.admin.pojo.sys.UserVO;
import cn.solwind.common.BeanOperateUtil;
import cn.solwind.common.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统用户Service
 */
@Slf4j
@Service
public class UserService {

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    /**
     * 分页查询数据字典
     *
     * @param userQuery
     * @return
     */
    public PageInfo<UserVO> listUser(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPageNum(), userQuery.getPageSize());    // 分页查询

        // 将空字符串属性设置为null
        BeanOperateUtil.emptyStrToNull(userQuery);

        userQuery.setName(SqlUtil.getLikeParam(userQuery.getName()));   // name模糊查询

        // 查询全部有效的用户信息
        List<UserVO> listUser = sysUserMapper.selectByQuery(userQuery);

        // 查询用户角色信息
        // 获取全部用户ID
        List<Long> listUserIds = new ArrayList<>();
        listUser.forEach((userVo) -> {
            listUserIds.add(userVo.getId());
        });
        List<UserRoleVO> listUserRoles = sysUserRoleMapper.selectUserRoles(listUserIds);
        // 根据用户ID组成MapList，用于给用户赋值
        Map<Long, List<UserRoleVO>> mapUserRoles = listUserRoles.stream().collect(Collectors.groupingBy(UserRoleVO::getUserId));

        // 将用户角色赋值至返回List中
        listUser.forEach((user) -> {
            user.setHadRoles(mapUserRoles.get(user.getId()));
        });
        PageInfo<UserVO> pageData = new PageInfo<>(listUser);

        return pageData;
    }

    /**
     * 创建用户
     *
     * @param userVO
     * @return
     */
    @Transactional
    public Response<UserVO> createUser(@Validated @RequestBody UserVO userVO) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            // 检查是否有重复
            Example example = new Example(SysUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userName", userVO.getUserName());
            criteria.andNotEqualTo("status", Constants.USER_STATUS_INVALID);
            List<SysUser> duplicateUser = sysUserMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(duplicateUser)) {
                // 用户名重复，返回错误
                log.info("UserName[{}] duplicated!", userVO.getUserName());
                return ResponseCode.USER_DUPLICATE.build();
            }

            SysUser sysUser = new SysUser();
            // 复制非空属性到sysUser中
            BeanUtils.copyProperties(userVO, sysUser, BeanOperateUtil.getNullPropertyNames(userVO));
            // 保存
            sysUser.setStatus(Constants.USER_STATUS_VALID); // 有效
            sysUser.setCreateTime(new Date());
            sysUser.setCreateUser(userDetails.getUsername());
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateUser(userDetails.getUsername());
            // 生成初始密码
            String initPassword = RandomStringUtils.random(8, true, true);
            // FIXME 增加发送初始密码邮件给客户
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            sysUser.setPassword(encoder.encode(initPassword));
            sysUser.setVersion(1L);
            sysUserMapper.insert(sysUser);

            // 保存角色
            userVO.getRoles().forEach((roleId) -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(sysUser.getId());
                sysUserRoleMapper.insert(sysUserRole);
            });

            BeanUtils.copyProperties(sysUser, userVO);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                e.printStackTrace();
                return ResponseCode.USER_DUPLICATE.build();
            } else {
                throw e;
            }
        }
        return ResponseCode.SUCCESS.build(userVO);
    }

    /**
     * 更新用户
     *
     * @param userVO
     * @return
     */
    @Transactional
    public Response updateUser(UserVO userVO) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userVO.getId());
        if (sysUser == null) {
            // 未查找到用户，返回异常
            return ResponseCode.PARAMS_ERROR.build();
        }

        // 复制非空属性到sysbook中
        BeanUtils.copyProperties(userVO, sysUser, BeanOperateUtil.getNullPropertyNames(userVO));
        // 保存
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUser(userDetails.getUsername());
        sysUserMapper.updateByPrimaryKey(sysUser);

        // 删除原用户角色关系
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", sysUser.getId());
        sysUserRoleMapper.deleteByExample(example);
        // 保存新用户角色关系
        userVO.getRoles().forEach((roleId) -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(sysUser.getId());
            sysUserRoleMapper.insert(sysUserRole);
        });

        return ResponseCode.SUCCESS.build();
    }


    /**
     * 作废用户
     *
     * @param id
     * @return
     */
    @Transactional
    public Response invalidUser(Long id) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (sysUser == null) {
            // 未查找到用户，返回异常
            return ResponseCode.PARAMS_ERROR.build();
        }

        // 保存
        sysUser.setStatus(Constants.USER_STATUS_INVALID);    // 状态设置为无效
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUser(userDetails.getUsername());
        sysUserMapper.updateByPrimaryKey(sysUser);

        return ResponseCode.SUCCESS.build();
    }

    /**
     * 查询用户所具有的权限id
     *
     * @param userId
     * @return
     */
    public List<Long> selectUserRoles(Long userId) {
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<SysUserRole> listSysUserRole = sysUserRoleMapper.selectByExample(example);

        List<Long> listReturn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(listSysUserRole)) {
            listSysUserRole.forEach((sysuserRole) -> {
                listReturn.add(sysuserRole.getRoleId());
            });
        }

        return listReturn;
    }
}
