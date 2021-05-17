package cn.solwind.admin.controller.sys;

import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.pojo.sys.UserQuery;
import cn.solwind.admin.pojo.sys.UserVO;
import cn.solwind.admin.service.sys.UserService;
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
 * 用户Controller
 */
@RestController
@RequestMapping("/api/sys/user")
@Slf4j
@Api(tags = "用户管理", value = "用户管理")
public class UserController {

    @Resource
    UserService userService;

    /**
     * 查询用户
     *
     * @param userQuery
     * @return
     */
    @Operation(summary = "查询用户", description = "查询用户接口")
    @PostMapping("list")
    public Response<PageInfo<UserVO>> list(@RequestBody UserQuery userQuery) {
        log.info("Query user!");
        PageInfo<UserVO> pageData = userService.listUser(userQuery);
        return ResponseCode.SUCCESS.build(pageData);
    }

    /**
     * 新增用户
     *
     * @param userVO
     * @return
     */
    @Operation(summary = "新增用户", description = "新增用户接口")
    @PostMapping("create")
    public Response<UserVO> create(@Validated @RequestBody UserVO userVO) {
        log.info("Create User [{}]!", userVO);
        return userService.createUser(userVO);
    }

    /**
     * 修改用户
     *
     * @param userVO
     * @return
     */
    @Operation(summary = "修改用户", description = "修改用户接口")
    @PostMapping("update")
    public Response update(@Validated @RequestBody UserVO userVO) {
        log.info("Update User [{}]!", userVO);
        return userService.updateUser(userVO);
    }

    /**
     * 作废用户
     *
     * @param id
     * @return
     */
    @GetMapping("invalid/{id}")
    @Operation(summary = "作废用户", description = "作废用户接口")
    public Response invalidUser(@PathVariable @ApiParam(name = "用户ID", required = true) Long id) {
        log.info("Delete User [{}]!", id);
        return userService.invalidUser(id);
    }

    /**
     * 获取用户角色数据
     *
     * @return
     */
    @GetMapping("userRoles/{userId}")
    @Operation(summary = "获取用户角色数据", description = "获取用户角色数据接口")
    public Response<List<Long>> findUserRoles(@PathVariable @ApiParam(name = "用户ID", required = true) Long userId) {
        log.info("select User[{}] Roles", userId);
        return ResponseCode.SUCCESS.build(userService.selectUserRoles(userId));
    }
}
