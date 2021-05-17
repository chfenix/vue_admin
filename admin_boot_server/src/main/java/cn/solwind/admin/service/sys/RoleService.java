package cn.solwind.admin.service.sys;

import cn.solwind.admin.common.Constants;
import cn.solwind.admin.common.PageParameter;
import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.entity.SysRole;
import cn.solwind.admin.entity.SysRoleFunc;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.mapper.SysRoleFuncMapper;
import cn.solwind.admin.mapper.SysRoleMapper;
import cn.solwind.admin.pojo.sys.RoleVO;
import cn.solwind.common.BeanOperateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 角色Service
 */
@Service
@Slf4j
public class RoleService {

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysRoleFuncMapper sysRoleFuncMapper;

    /**
     * 查询所有有效角色
     * @param pageParameter
     * @return
     */
    public PageInfo<RoleVO> listRole(PageParameter pageParameter) {
        PageHelper.startPage(pageParameter.getPageNum(), pageParameter.getPageSize());    // 分页查询
        // 查询全部有效的角色
        List<RoleVO> listRole = sysRoleMapper.selectAllRole();
        PageInfo<RoleVO> pageData = new PageInfo<>(listRole);
        return pageData;
    }

    /**
     * 查询全部有效的角色信息
     * @return
     */
    public List<RoleVO> listAllRole() {
        return sysRoleMapper.selectAllRole();
    }

    /**
     * 新增Role
     * @param roleVO
     * @return
     */
    @Transactional
    public Response<RoleVO> createRole(RoleVO roleVO) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 检查CODE或NAME是否有重复
        int intDuplicate = sysRoleMapper.checkDuplicate(roleVO.getCode(), roleVO.getName());

        if(intDuplicate > 0) {
            // 存在有效的角色CODE或者NAME重复，返回错误
            log.info("ROLE [{}:{}] is duplicate data!", roleVO.getCode(), roleVO.getName());
            return ResponseCode.ROLE_DUPLICATE.build();
        }

        SysRole sysRole = new SysRole();
        // 复制非空属性到sysbook中
        BeanUtils.copyProperties(roleVO,sysRole);
        // 保存
        sysRole.setStatus(Constants.COMMON_VALID);  // 默认状态有效
        sysRole.setCreateTime(new Date());
        sysRole.setCreateUser(userDetails.getUsername());
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateUser(userDetails.getUsername());
        sysRoleMapper.insert(sysRole);

        // 保存角色权限
        if(CollectionUtils.isNotEmpty(roleVO.getCheckedMenus())) {
            roleVO.getCheckedMenus().forEach((functionId) -> {
                // 保存角色功能关联关系
                SysRoleFunc sysRoleFunc = new SysRoleFunc();
                sysRoleFunc.setRoleId(sysRole.getId());
                sysRoleFunc.setFuncId(functionId);
                sysRoleFuncMapper.insert(sysRoleFunc);
            });
        }

        BeanUtils.copyProperties(sysRole,roleVO);

        return ResponseCode.SUCCESS.build(roleVO);
    }

    /**
     * 更新角色
     * @param roleVO
     * @return
     */
    @Transactional
    public Response updateRole(RoleVO roleVO) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleVO.getId());
        if(sysRole == null) {
            // 未查找到role，返回异常
            return ResponseCode.PARAMS_ERROR.build();
        }

        // 复制非空属性到sysRole中
        BeanUtils.copyProperties(roleVO,sysRole, BeanOperateUtil.getNullPropertyNames(roleVO));
        // 保存
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateUser(userDetails.getUsername());
        sysRoleMapper.updateByPrimaryKey(sysRole);

        // 删除原角色权限
        Example example = new Example(SysRoleFunc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", sysRole.getId());
        sysRoleFuncMapper.deleteByExample(example);
        // 保存角色权限
        if(CollectionUtils.isNotEmpty(roleVO.getCheckedMenus())) {
            roleVO.getCheckedMenus().forEach((functionId) -> {
                // 保存角色功能关联关系
                SysRoleFunc sysRoleFunc = new SysRoleFunc();
                sysRoleFunc.setRoleId(sysRole.getId());
                sysRoleFunc.setFuncId(functionId);
                sysRoleFuncMapper.insert(sysRoleFunc);
            });
        }

        return ResponseCode.SUCCESS.build();
    }

    /**
     * 作废角色
     * @param id
     * @return
     */
    @Transactional
    public Response invalidRole(Long id) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);
        if(sysRole == null) {
            // 未查找到role，返回异常
            return ResponseCode.PARAMS_ERROR.build();
        }

        // 保存
        sysRole.setStatus(Constants.COMMON_INVALID);    // 状态设置为无效
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateUser(userDetails.getUsername());
        sysRoleMapper.updateByPrimaryKey(sysRole);

        return ResponseCode.SUCCESS.build();
    }

}
