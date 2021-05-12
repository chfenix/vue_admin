package cn.solwind.admin.mapper;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import cn.solwind.admin.entity.SysUser;

public interface SysUserMapper extends Mapper<SysUser> {

    /**
     * 用户登录
     * @param userName
     * @return
     */
    @Select({ "SELECT * FROM SYS_USER WHERE (USER_NAME=#{userName}) AND STATUS=1 " })
    SysUser login(String userName);
}
