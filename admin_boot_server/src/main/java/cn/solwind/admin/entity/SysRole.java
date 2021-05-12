package cn.solwind.admin.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import lombok.AllArgsConstructor;
import java.util.Date;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import lombok.Builder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysRole {

    /**
     * 
     * SYS_ROLE.ID
     *
     * @mbg.generated
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 角色编码
     * SYS_ROLE.CODE
     *
     * @mbg.generated
     */
    private String code;

    /**
     * 角色名称
     * SYS_ROLE.NAME
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 备注
     * SYS_ROLE.REMARK
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 状态
            1：有效
            0：无效
     * SYS_ROLE.STATUS
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 创建时间
     * SYS_ROLE.CREATE_TIME
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 创建用户
     * SYS_ROLE.CREATE_USER
     *
     * @mbg.generated
     */
    private String createUser;

    /**
     * 修改时间
     * SYS_ROLE.UPDATE_TIME
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * 修改用户
     * SYS_ROLE.UPDATE_USER
     *
     * @mbg.generated
     */
    private String updateUser;
}
