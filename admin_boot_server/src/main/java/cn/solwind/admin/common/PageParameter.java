package cn.solwind.admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 分页查询参数基类
 */


@Getter
@Setter
@ToString
@ApiModel("分页查询参数")
public class PageParameter implements java.io.Serializable {

    @ApiModelProperty(value = "当前页码")
    private int pageNum = 1;    // 当前页码

    @ApiModelProperty(value = "每页条数")
    private int pageSize = 10;  // 每页条数

}
