package cn.solwind.admin.mapper;

import org.apache.ibatis.annotations.Select;
import java.util.List;
import tk.mybatis.mapper.common.Mapper;
import cn.solwind.admin.entity.SysFunction;

public interface SysFunctionMapper extends Mapper<SysFunction> {

    /**
     * 根据用户ID，查询其所拥有的功能权限
     * @param userId
     * @return
     */
    @Select({ "SELECT DISTINCT SF.* FROM ",
            "SYS_FUNCTION SF,SYS_ROLE SR,SYS_ROLE_FUNC SRF,SYS_USER_ROLE SUR",
            "WHERE ",
            "SF.ID = SRF.FUNC_ID AND SRF.ROLE_ID=SR.ID ",
            "AND SUR.ROLE_ID = SR.ID",
            "AND SF.STATUS = 1",
            "AND SR.STATUS = 1",
            "AND SUR.USER_ID = #{userId} ORDER BY SF.SORT" })
    List<SysFunction> findAllFuncByUserId(Long userId);
}
