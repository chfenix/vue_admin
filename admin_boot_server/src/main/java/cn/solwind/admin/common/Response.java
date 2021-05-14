package cn.solwind.admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口调用反馈类
 * @param <T>
 */
@Getter
@Setter
@ApiModel("公共反馈信息")
public class Response<T> {

    @ApiModelProperty(value = "返回码,0表示成功，非0表示出错")
    private Integer code;       // 返回码

    @ApiModelProperty(value = "返回信息")
    private String message;     // 返回信息

    @ApiModelProperty(value = "业务数据")
    private T data;

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
