package cn.solwind.admin.mapper;

import cn.solwind.admin.pojo.sys.RoleVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import cn.solwind.admin.entity.SysRole;

import java.util.List;

public interface SysRoleMapper extends Mapper<SysRole> {

    /**
     * 根据用户ID查询全部角色CODE
     * @param userId
     * @return
     */
    @Select({"SELECT SR.CODE FROM SYS_ROLE SR,SYS_USER_ROLE SUR ",
            "WHERE SUR.ROLE_ID=SR.ID AND SR.STATUS = 1",
            "AND SUR.USER_ID = #{userId}"})
    String[] findAllRoleCodeByUserId(Long userId);

    /**
     * 查询全部有效角色
     * @return
     */
    @Select({
            "SELECT * FROM SYS_ROLE WHERE STATUS = 1 ORDER BY CREATE_TIME DESC"
    })
    List<RoleVO> selectAllToVO();

    /**
     * 查询是否有CODE或者NAME重复的角色
     * @param code
     * @param name
     * @return
     */
    @Select({
            "SELECT COUNT(ID) FROM SYS_ROLE WHERE STATUS = 1 AND (CODE = #{code} OR NAME = #{name})"
    })
    Integer checkDuplicate(String code,String name);
}
