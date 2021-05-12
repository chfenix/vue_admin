package cn.solwind.admin.controller.auth;

import cn.solwind.admin.common.CacheKey;
import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.jwt.JwtTokenUtils;
import cn.solwind.admin.jwt.TokenValue;
import cn.solwind.admin.pojo.auth.AuthVO;
import cn.solwind.admin.pojo.auth.CaptchaVO;
import cn.solwind.admin.pojo.auth.ChangePwdVO;
import cn.solwind.admin.pojo.auth.LoginVO;
import cn.solwind.admin.service.auth.AuthService;
import cn.solwind.admin.utils.IpUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * 用户登录接口
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Value("${project.login.verifycode}")
    Boolean isVerifyCode;  // 是否校验登录验证码

    @Resource
    AuthService authService;

    @Resource
    JwtTokenUtils jwtTokenUtils;

    @Resource
    @Lazy
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录
     * @param loginVO
     * @return
     */
    @PostMapping("login")
    public Response login(@Validated @RequestBody LoginVO loginVO, HttpServletRequest request) {
        log.info("User[{}] is logging in", loginVO.getUserName());
        try {
            // 验证登录验证码
            String verifyCode = redisTemplate.opsForValue().get(CacheKey.CAPTCHA_PREFIX + loginVO.getVerifyKey());
            if(!loginVO.getVerifyCode().equalsIgnoreCase(verifyCode) && isVerifyCode) {
                // 验证码验证失败
                log.info("User[{}] Login verify code failed! input code[{}] expect[{}]",loginVO.getUserName(),loginVO.getVerifyCode(),verifyCode);
                redisTemplate.delete(CacheKey.CAPTCHA_PREFIX + loginVO.getVerifyKey());
                return ResponseCode.VERIFYCODE_ERROR.build();
            }

            String loginIp = IpUtil.getRealIp(request);

            String jwtToken = authService.login(loginVO.getUserName(), loginVO.getPassword(), loginIp);
            TokenValue tokenValue = new TokenValue();
            tokenValue.setHeader(jwtTokenUtils.getTokenHeader());
            tokenValue.setValue(jwtToken);
            tokenValue.setPrefix(jwtTokenUtils.getTokenHead());
            tokenValue.setExpiration(jwtTokenUtils.getExpiration());

            log.info("用户登录处理完成");
            return ResponseCode.SUCCESS.build(tokenValue);
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return ResponseCode.LOGIN_FAIL.build();
        }

    }

    /**
     * 登出
     * @return
     */
    @PostMapping("logout")
    public Response logout() {
        // TODO 如用户信息保存至缓存中，在此处进行清除
        return ResponseCode.SUCCESS.build();
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Response info() {
        AuthVO authVO = authService.findUserInfo();
        return ResponseCode.SUCCESS.build(authVO);
    }

    /**
     * 修改密码
     * @param changePwdVO
     * @return
     */
    @PostMapping("changePwd")
    public Response changePwd(@Validated @RequestBody ChangePwdVO changePwdVO) {
        log.info("user change password!");
        return authService.changePassword(changePwdVO);
    }

    @Resource
    private Producer captchaProducer;

    /**
     * 生成验证码
     * @return
     * @throws Exception
     */
    @GetMapping("/captcha")
    public Response getKaptchaImage() throws Exception {
        String capText = captchaProducer.createText();
        log.debug("captcha: " + capText );
        BufferedImage bi = captchaProducer.createImage(capText);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", os);
        // 缓存key
        String verifyKey = RandomStringUtils.random(16, true, true);
        redisTemplate.opsForValue().set(CacheKey.CAPTCHA_PREFIX + verifyKey, capText);
        CaptchaVO captchaVO = new CaptchaVO(verifyKey,
                Base64.getEncoder().encodeToString(os.toByteArray()));

        return ResponseCode.SUCCESS.build(captchaVO);
    }


}
