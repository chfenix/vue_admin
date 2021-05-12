package cn.solwind.admin.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录界面验证码VO
 */
@Getter
@Setter
@AllArgsConstructor
public class CaptchaVO {

    /**
     * 随机验证码
     */
    private String randomKey;

    /**
     * BASE64后的图片
     */
    private String base64Image;
}
