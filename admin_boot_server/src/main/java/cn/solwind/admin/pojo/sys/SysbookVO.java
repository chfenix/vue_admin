package cn.solwind.admin.pojo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 数据字典VO
 */
@Getter
@Setter
@ApiModel("数据字典")
public class SysbookVO {

    @ApiModelProperty(value= "ID")
    private Long id;    // ID

    @ApiModelProperty(value= "类型编号")
    private String typeCode;    // 类型编号

    @ApiModelProperty(value= "类型名称")
    private String typeName;    // 类型名称

    @ApiModelProperty(value= "明细编号")
    private String listCode;    // 明细编号

    @ApiModelProperty(value= "明细名称")
    private String listName;    // 明细名称

    @ApiModelProperty(value= "状态 数据字典条目状态：0:删除，1:有效")
    private Integer status; // 状态 数据字典条目状态：0:删除，1:有效

    @ApiModelProperty(value= "状显示顺序")
    private Integer pri;    // 显示顺序

    @ApiModelProperty(value= "创建时间")
    private Date createTime;    // 创建时间

}
