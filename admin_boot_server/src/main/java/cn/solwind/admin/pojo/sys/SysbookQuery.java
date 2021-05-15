package cn.solwind.admin.pojo.sys;

import cn.solwind.admin.common.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("数据字典查询参数")
public class SysbookQuery extends PageParameter {

    @ApiModelProperty(value = "Type Code")
    private String typeCode;    // Type Code
}
