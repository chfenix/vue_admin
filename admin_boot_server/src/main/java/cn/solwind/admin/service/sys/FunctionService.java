package cn.solwind.admin.service.sys;

import cn.solwind.admin.entity.SysFunction;
import cn.solwind.admin.mapper.SysFunctionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}
