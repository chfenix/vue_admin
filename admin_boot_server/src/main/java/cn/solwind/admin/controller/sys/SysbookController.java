package cn.solwind.admin.controller.sys;

import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.pojo.sys.SysbookQuery;
import cn.solwind.admin.pojo.sys.SysbookVO;
import cn.solwind.admin.service.sys.SysbookService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据字典Controller
 */
@RestController
@RequestMapping("/api/sys/sysbook")
@Slf4j
@Api(tags="数据字典", value="数据字典维护")
public class SysbookController {

    @Resource
    SysbookService sysbookService;

    /**
     * 查询所有有效的Sysbook数据
     * @return
     */
    @PostMapping("list")
    @Operation(summary = "查询数据字典", description = "查询所有有效的数据字典数据")
    public Response<PageInfo<SysbookVO>> list(@RequestBody SysbookQuery sysbookQuery) {
        log.info("Search Sysbook!");
        PageInfo<SysbookVO> pageData = sysbookService.listSysbook(sysbookQuery);
        return ResponseCode.SUCCESS.build(pageData);
    }

    /**
     * 创建Sysbook
     * @param sysbookVO
     * @return
     */
    @PostMapping("create")
    @Operation(summary = "新增数据字典", description = "新增数据字典")
    public Response<SysbookVO> create(@RequestBody SysbookVO sysbookVO) {
        log.info("Create Sysbook!");
        return sysbookService.createSysbook(sysbookVO);
    }

    /**
     * 修改sysbook
     * @param sysbookVO
     * @return
     */
    @PostMapping("update")
    @Operation(summary = "修改数据字典", description = "修改数据字典")
    public Response update(@RequestBody SysbookVO sysbookVO) {
        log.info("modify sysbook [{}]",sysbookVO.getId());
        return sysbookService.updateSysbook(sysbookVO);
    }

    /**
     * 物理删除Sysbook
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除数据字典", description = "删除数据字典")
    public Response delete(@PathVariable @ApiParam(name="数据字典主键",required = true) Long id) {
        log.info("delete sysbook [{}]",id);
        if(id == null) {
            return ResponseCode.PASSWORD_ERROR.build();
        }
        return sysbookService.deleteSysbook(id);
    }
}
