package cn.solwind.admin.mapper;

import cn.solwind.admin.pojo.sys.UserQuery;
import cn.solwind.admin.pojo.sys.UserVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import cn.solwind.admin.entity.SysUser;

import java.util.List;

public interface SysUserMapper extends Mapper<SysUser> {

    /**
     * 用户登录
     * @param userName
     * @return
     */
    @Select({ "SELECT * FROM SYS_USER WHERE (USER_NAME=#{userName}) AND STATUS=1 " })
    SysUser login(String userName);

    @Select({"<script>",
            "SELECT * FROM SYS_USER WHERE 1 = 1",
            "AND STATUS != 0",
            // 用户名
            " <if test='userName != null'>AND USER_NAME = #{userName}</if>",
            // 姓名
            " <if test='name != null'>AND NAME like #{userName}</if>",
            // EMAIL
            " <if test='email != null'>AND EMAIL = #{email}</if>",
            "ORDER BY UPDATE_TIME DESC",
            "</script>"
    })
    List<UserVO> selectByQuery(UserQuery userQuery);
}
