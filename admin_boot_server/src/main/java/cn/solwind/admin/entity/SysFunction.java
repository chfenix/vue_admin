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
public class SysFunction {

    /**
     * 
     * SYS_FUNCTION.ID
     *
     * @mbg.generated
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 资源code，必须唯一并保持和前端一致
     * SYS_FUNCTION.CODE
     *
     * @mbg.generated
     */
    private String code;

    /**
     * 菜单标题
     * SYS_FUNCTION.TITLE
     *
     * @mbg.generated
     */
    private String title;

    /**
     * 图标类
     * SYS_FUNCTION.ICON
     *
     * @mbg.generated
     */
    private String icon;

    /**
     * 显示顺序
     * SYS_FUNCTION.SORT
     *
     * @mbg.generated
     */
    private Integer sort;

    /**
     * 类型
            1:菜单
            2:按钮
     * SYS_FUNCTION.TYPE
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     * 上级菜单ID，如果无父节点，则为空
     * SYS_FUNCTION.PARENT_ID
     *
     * @mbg.generated
     */
    private Long parentId;

    /**
     * 状态
            1：有效
            0：无效
     * SYS_FUNCTION.STATUS
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 备注
     * SYS_FUNCTION.REMARK
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 创建时间
     * SYS_FUNCTION.CREATE_TIME
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 创建用户
     * SYS_FUNCTION.CREATE_USER
     *
     * @mbg.generated
     */
    private String createUser;

    /**
     * 修改时间
     * SYS_FUNCTION.UPDATE_TIME
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * 修改用户
     * SYS_FUNCTION.UPDATE_USER
     *
     * @mbg.generated
     */
    private String updateUser;

    /**
     * 相应的操作，如地址
     * SYS_FUNCTION.PATH
     *
     * @mbg.generated
     */
    private String path;
}
