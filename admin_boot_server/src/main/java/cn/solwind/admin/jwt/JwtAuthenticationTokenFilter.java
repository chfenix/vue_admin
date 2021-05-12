package cn.solwind.admin.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获取 Request 中的请求头为 “ Authorization ” 的 token 值
        String completeToken = request.getHeader(this.jwtTokenUtils.getTokenHeader());
        // 验证 值是否以"Bearer "开头
        if (completeToken != null && completeToken.startsWith(this.jwtTokenUtils.getTokenHead())) {
            // 截取token中"Bearer "后面的值，
            final String tokenValue = jwtTokenUtils.interceptCompleteToken(completeToken);
            // 根据 token值，获取 用户的 username
            String username = jwtTokenUtils.getUsernameFromToken(tokenValue);
            log.debug("当前登录的用户是 : {} ", username);
            // 验证用户账号是否合法
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 通过用户名 获取用户的信息
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 验证token和用户是否匹配
                if (jwtTokenUtils.validateToken(tokenValue, userDetails)) {
                    // 然后把构造UsernamePasswordAuthenticationToken对象
                    // 最后绑定到当前request中，在后面的请求中就可以获取用户信息
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将用户信息，设置到 SecurityContext 中，可以在任何地方 使用 下面语句获取 获取 当前用户登录信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
