package cn.solwind.admin.mapper;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import cn.solwind.admin.entity.SysRole;

public interface SysRoleMapper extends Mapper<SysRole> {

    @Select({ "SELECT SR.CODE FROM SYS_ROLE SR,SYS_USER_ROLE SUR ", "WHERE SUR.ROLE_ID=SR.ID AND SR.STATUS = 1", "AND SUR.USER_ID = #{userId}" })
    String[] findAllRoleCodeByUserId(Long userId);
}
