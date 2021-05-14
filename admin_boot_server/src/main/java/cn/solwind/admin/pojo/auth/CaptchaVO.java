package cn.solwind.admin.pojo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录界面验证码VO
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("验证码信息")
public class CaptchaVO {

    /**
     * 随机验证码
     */
    @ApiModelProperty(value="随机Key")
    private String randomKey;

    /**
     * BASE64后的图片
     */
    @ApiModelProperty(value="验证码图片（BASE64）")
    private String base64Image;
}
