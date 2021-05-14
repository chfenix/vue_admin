package cn.solwind.admin.jwt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Jwt Token数据")
public class TokenValue implements java.io.Serializable {

    /**
     *  请求头的值
     */
    @ApiModelProperty(value="请求头的值", required = true)
    private String header;

    /**
     * token 值
     */
    @ApiModelProperty(value="值", required = true)
    private String value;

    /**
     * token 前缀
     */
    @ApiModelProperty(value="前缀", required = true)
    private String prefix;

    /**
     * 过期时间（毫秒，这里默认20分钟）
     */
    @ApiModelProperty(value="过期时间", required = true)
    private Long expiration;
}
