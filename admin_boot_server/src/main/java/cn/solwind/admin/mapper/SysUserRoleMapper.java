package cn.solwind.admin.mapper;

import cn.solwind.admin.entity.SysUserRole;
import cn.solwind.admin.pojo.sys.UserRoleVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysUserRoleMapper extends Mapper<SysUserRole> {

    /**
     * 查询用户下的所有角色
     *
     * @param userIds
     * @return
     */
    @Select({"<script>",
            "SELECT SUR.USER_ID,SR.ID ROLE_ID,SR.NAME ROLE_NAME FROM SYS_ROLE SR,SYS_USER_ROLE SUR WHERE SUR.ROLE_ID=SR.ID AND SUR.USER_ID IN ",
            "<foreach collection='userIds' item='userId' index='userId' open='(' separator=',' close=')'>#{userId}</foreach> ",
            "</script>"
    })
    List<UserRoleVO> selectUserRoles(List<Long> userIds);
}
