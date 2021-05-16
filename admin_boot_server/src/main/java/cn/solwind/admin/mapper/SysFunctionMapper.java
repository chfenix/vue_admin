package cn.solwind.admin.mapper;

import cn.solwind.admin.entity.SysFunction;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

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

    /**
     * 查询角色下所有类型为菜单且为叶子节点的权限数据
     * @param roleId
     * @return
     */
    @Select({ "SELECT DISTINCT SF.* FROM ",
            "SYS_FUNCTION SF,SYS_ROLE_FUNC SRF",
            "WHERE ",
            "SF.ID = SRF.FUNC_ID",
            "AND SF.STATUS = 1",
            "AND SF.TYPE = 1",
            "AND NOT EXISTS(SELECT 1 FROM SYS_FUNCTION SFP WHERE SFP.PARENT_ID=SF.ID)",
            "AND SRF.ROLE_ID = #{roleId}" })
    List<SysFunction> findAllLeafMenuByRole(Long roleId);

}
