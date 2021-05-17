package cn.solwind.admin.service.sys;

import cn.solwind.admin.common.Constants;
import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.entity.Sysbook;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.mapper.SysbookMapper;
import cn.solwind.admin.pojo.sys.SysbookQuery;
import cn.solwind.admin.pojo.sys.SysbookVO;
import cn.solwind.common.BeanOperateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 数据字典Service
 */
@Slf4j
@Service
public class SysbookService {

    @Resource
    SysbookMapper sysbookMapper;

    /**
     * 分页查询数据字典
     * @param sysbookQuery
     * @return
     */
    public PageInfo<SysbookVO> listSysbook(SysbookQuery sysbookQuery) {
        PageHelper.startPage(sysbookQuery.getPageNum(), sysbookQuery.getPageSize());    // 分页查询

        // 将空字符串属性设置为null
        BeanOperateUtil.emptyStrToNull(sysbookQuery);

        // 查询全部有效的sysbook
        List<SysbookVO> listSysbook = sysbookMapper.selectByQuery(sysbookQuery);
        PageInfo<SysbookVO> pageData = new PageInfo<>(listSysbook);

        return pageData;
    }

    /**
     * 创建Sysbook
     * @param sysbookVO
     * @return
     */
    @Transactional
    public Response<SysbookVO> createSysbook(SysbookVO sysbookVO) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Sysbook sysbook = new Sysbook();
            // 复制非空属性到sysbook中
            BeanUtils.copyProperties(sysbookVO,sysbook, BeanOperateUtil.getNullPropertyNames(sysbookVO));
            // 保存
            sysbook.setStatus(Constants.COMMON_VALID);  // 默认状态有效
            sysbook.setModifyFlag(true);  // 默认可修改
            sysbook.setCreateTime(new Date());
            sysbook.setCreateUser(userDetails.getUsername());
            sysbook.setUpdateTime(new Date());
            sysbook.setUpdateUser(userDetails.getUsername());
            sysbookMapper.insert(sysbook);

            BeanUtils.copyProperties(sysbook,sysbookVO);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException) {
                e.printStackTrace();
                return ResponseCode.SYSBOOK_DUPLICATE.build();
            }
            else {
                throw e;
            }
        }

        return ResponseCode.SUCCESS.build(sysbookVO);
    }

    /**
     * 更新sysbook
     * @param sysbookVO
     * @return
     */
    @Transactional
    public Response updateSysbook(SysbookVO sysbookVO) {
        JwtUser userDetails = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sysbook sysbook = sysbookMapper.selectByPrimaryKey(sysbookVO.getId());
        if(sysbook == null) {
            // 未查找到sysbook，返回异常
            return ResponseCode.PARAMS_ERROR.build();
        }

        if(sysbook.getModifyFlag() != null && !sysbook.getModifyFlag()) {
            log.error("Sysbook[{}]正在被用户[{}]非法编辑！", sysbookVO.getId(), userDetails.getUsername());
            return ResponseCode.SYSBOOK_CANNOT_MODIFY.build();
        }

        // 复制非空属性到sysbook中
        BeanUtils.copyProperties(sysbookVO,sysbook, BeanOperateUtil.getNullPropertyNames(sysbookVO));
        // 保存
        sysbook.setUpdateTime(new Date());
        sysbook.setUpdateUser(userDetails.getUsername());
        sysbookMapper.updateByPrimaryKey(sysbook);

        return ResponseCode.SUCCESS.build();
    }

    /**
     * 物理删除Sysbook
     * @param id
     * @return
     */
    @Transactional
    public Response deleteSysbook(Long id) {
        if(id != null) {
            sysbookMapper.deleteByPrimaryKey(id);
            return ResponseCode.SUCCESS.build();
        }
        else {
            return ResponseCode.PARAMS_ERROR.build();
        }
    }
}
