package cn.solwind.admin.controller.sys;


import cn.solwind.admin.common.PageParameter;
import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.pojo.sys.MenuTreeVO;
import cn.solwind.admin.pojo.sys.RoleVO;
import cn.solwind.admin.service.sys.FunctionService;
import cn.solwind.admin.service.sys.RoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色Controller
 */
@RestController
@RequestMapping("/api/sys/role")
@Slf4j
@Api(tags = "系统管理", value = "角色管理")
public class RoleController {

    @Resource
    RoleService roleService;

    @Resource
    FunctionService functionService;

    /**
     * 查询角色
     *
     * @param pageParameter
     * @return
     */
    @Operation(summary = "查询角色", description = "查询角色接口")
    @PostMapping("list")
    public Response<PageInfo<RoleVO>> list(@RequestBody PageParameter pageParameter) {
        log.info("Query role!");
        PageInfo<RoleVO> pageData = roleService.listRole(pageParameter);
        return ResponseCode.SUCCESS.build(pageData);
    }

    /**
     * 新增角色
     *
     * @param roleVO
     * @return
     */
    @Operation(summary = "新增角色", description = "新增角色接口")
    @PostMapping("create")
    public Response<RoleVO> create(@Validated @RequestBody RoleVO roleVO) {
        log.info("Create Role [{}]!", roleVO);
        return roleService.createRole(roleVO);
    }

    /**
     * 获取菜单树数据
     *
     * @return
     */
    @Operation(summary = "获取菜单树", description = "获取全部菜单树数据")
    @GetMapping("menuTree")
    public Response<List<MenuTreeVO>> menuTree() {
        log.info("Get menu tree!");
        List<MenuTreeVO> listMenus = functionService.findAllMenu();
        return ResponseCode.SUCCESS.build(listMenus);
    }

    /**
     * 获取某角色下的菜单权限ID
     *
     * @param roleId
     * @return
     */
    @Operation(summary = "获取角色菜单", description = "获取某角色下的菜单权限")
    @GetMapping("roleMenus/{roleId}")
    public Response<List<MenuTreeVO>> roleMenus(@PathVariable @ApiParam(name = "角色ID", required = true) Long roleId) {
        log.info("Get menus for Role[{}]", roleId);
        List<MenuTreeVO> listMenus = functionService.findRoleMenus(roleId);
        return ResponseCode.SUCCESS.build(listMenus);
    }

    /**
     * 修改角色
     *
     * @param roleVO
     * @return
     */
    @Operation(summary = "修改角色", description = "修改角色接口")
    @PostMapping("update")
    public Response update(@Validated @RequestBody RoleVO roleVO) {
        log.info("Update Role [{}]!", roleVO);
        return roleService.updateRole(roleVO);
    }

    /**
     * 作废角色
     *
     * @param id
     * @return
     */
    @GetMapping("invalid/{id}")
    @Operation(summary = "作废角色", description = "作废角色接口")
    public Response invalidRole(@PathVariable @ApiParam(name = "角色ID", required = true) Long id) {
        log.info("Delete Role [{}]!", id);
        return roleService.invalidRole(id);
    }
}
