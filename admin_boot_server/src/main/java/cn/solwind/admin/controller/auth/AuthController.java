package cn.solwind.admin.controller.auth;

import cn.solwind.admin.common.Response;
import cn.solwind.admin.common.ResponseCode;
import cn.solwind.admin.entity.SysUser;
import cn.solwind.admin.jwt.JwtTokenUtils;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.jwt.TokenValue;
import cn.solwind.admin.pojo.auth.AuthVO;
import cn.solwind.admin.pojo.auth.LoginVO;
import cn.solwind.admin.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户登录接口
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Resource
    AuthService authService;

    @Resource
    JwtTokenUtils jwtTokenUtils;

    /**
     * 用户登录
     * @param loginVO
     * @return
     */
    @PostMapping("login")
    public Response login(@Validated @RequestBody LoginVO loginVO) {
        log.info("接收到用户登录请求");
        try {
            String jwtToken = authService.login(loginVO.getUserName(),loginVO.getPassword());
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

    @PostMapping("logout")
    public Response logout() {
        // FIXME 增加logout代码
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

}
