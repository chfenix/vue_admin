package cn.solwind.admin.service.sys;

import cn.solwind.admin.common.Constants;
import cn.solwind.admin.entity.SysFunction;
import cn.solwind.admin.mapper.SysFunctionMapper;
import cn.solwind.admin.pojo.sys.MenuTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能Service
 */
@Service
public class FunctionService {

    @Resource
    SysFunctionMapper sysFunctionMapper;

    /**
     * 根据用户ID查询其所有权限的功能
     * @param userId
     * @return
     */
    public List<SysFunction> findAllByUserId(Long userId) {
        return sysFunctionMapper.findAllFuncByUserId(userId);
    }

    /**
     * 查询全部菜单
     * @return
     */
    public List<MenuTreeVO> findAllMenu() {
        // 查询全部功能权限
        Example example = new Example(SysFunction.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", Constants.COMMON_VALID);
        example.orderBy("parentId").orderBy("sort");
        List<SysFunction> listFunc = sysFunctionMapper.selectByExample(example);

        // 封装权限树数据
        List<MenuTreeVO> listTmpMenus = new ArrayList<>();
        if(listFunc != null) {
            for (SysFunction objFunc : listFunc) {
                MenuTreeVO objMenu = new MenuTreeVO();
                BeanUtils.copyProperties(objFunc, objMenu);
                listTmpMenus.add(objMenu);
            }
        }

        // 将MenuList组成树形结构
        List<MenuTreeVO> listMenus = new ArrayList<>();
        // List转为Map
        Map<Long, MenuTreeVO> mapMenus = listTmpMenus.stream().collect(Collectors.toMap(MenuTreeVO::getId, menu -> menu));
        for (MenuTreeVO menu : listTmpMenus) {
            MenuTreeVO parentMenu = mapMenus.get(menu.getParentId());
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

        return listMenus;
    }

    /**
     * 查询某角色下所有菜单的ID
     * @param id
     * @return
     */
    public List<MenuTreeVO> findRoleMenus(Long id) {
        // 获取用户所具有的权限
        List<SysFunction> listFunction = sysFunctionMapper.findAllLeafMenuByRole(id);
        // 结果转为List，用于设置是否选中
        List<MenuTreeVO> listMenus = new ArrayList<>();
        listFunction.forEach((sysFunction -> {
            listMenus.add(new MenuTreeVO(sysFunction.getId()));
        }));
        return listMenus;
    }
}
