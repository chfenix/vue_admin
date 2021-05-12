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
public class Sysbook {

    /**
     * ID
     * SYSBOOK.ID
     *
     * @mbg.generated
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 类型编号
     * SYSBOOK.TYPE_CODE
     *
     * @mbg.generated
     */
    private String typeCode;

    /**
     * 类型名称
     * SYSBOOK.TYPE_NAME
     *
     * @mbg.generated
     */
    private String typeName;

    /**
     * 明细编号
     * SYSBOOK.LIST_CODE
     *
     * @mbg.generated
     */
    private String listCode;

    /**
     * 明细名称
     * SYSBOOK.LIST_NAME
     *
     * @mbg.generated
     */
    private String listName;

    /**
     * 状态 数据字典条目状态：0:删除，1:有效
     * SYSBOOK.STATUS
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 可修改标志
            0：不可修改
            1：可修改
     * SYSBOOK.MODIFY_FLAG
     *
     * @mbg.generated
     */
    private Boolean modifyFlag;

    /**
     * 显示顺序
     * SYSBOOK.PRI
     *
     * @mbg.generated
     */
    private Integer pri;

    /**
     * 备注
     * SYSBOOK.REMARK
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 创建时间
     * SYSBOOK.CREATE_TIME
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 创建用户
     * SYSBOOK.CREATE_USER
     *
     * @mbg.generated
     */
    private String createUser;

    /**
     * 修改时间
     * SYSBOOK.UPDATE_TIME
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * 修改用户
     * SYSBOOK.UPDATE_USER
     *
     * @mbg.generated
     */
    private String updateUser;
}
