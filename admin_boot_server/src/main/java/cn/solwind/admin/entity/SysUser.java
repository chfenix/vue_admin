package cn.solwind.admin.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.Version;
import lombok.AllArgsConstructor;
import java.util.Date;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import lombok.Builder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUser {

    /**
     * 
     * SYS_USER.ID
     *
     * @mbg.generated
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 用户名
     * SYS_USER.USER_NAME
     *
     * @mbg.generated
     */
    private String userName;

    /**
     * 密码
     * SYS_USER.PASSWORD
     *
     * @mbg.generated
     */
    private String password;

    /**
     * 头像
     * SYS_USER.AVATAR
     *
     * @mbg.generated
     */
    private String avatar;

    /**
     * 姓名
     * SYS_USER.NAME
     *
     * @mbg.generated
     */
    private String name;

    /**
     * EMAIL
     * SYS_USER.EMAIL
     *
     * @mbg.generated
     */
    private String email;

    /**
     * 联系电话
     * SYS_USER.PHONE
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 最后登录时间
     * SYS_USER.LAST_LOGIN_TIME
     *
     * @mbg.generated
     */
    private Date lastLoginTime;

    /**
     * 最后登录IP
     * SYS_USER.LAST_LOGIN_IP
     *
     * @mbg.generated
     */
    private String lastLoginIp;

    /**
     * 状态
            1：有效
            0：无效
     * SYS_USER.STATUS
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 备注
     * SYS_USER.REMARK
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 创建时间
     * SYS_USER.CREATE_TIME
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 创建用户
     * SYS_USER.CREATE_USER
     *
     * @mbg.generated
     */
    private String createUser;

    /**
     * 修改时间
     * SYS_USER.UPDATE_TIME
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * 修改用户
     * SYS_USER.UPDATE_USER
     *
     * @mbg.generated
     */
    private String updateUser;

    /**
     * 
     * SYS_USER.VERSION
     *
     * @mbg.generated
     */
    @Version
    private Long version;
}
