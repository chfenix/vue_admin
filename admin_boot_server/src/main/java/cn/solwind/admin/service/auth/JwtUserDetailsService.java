package cn.solwind.admin.service.auth;

import cn.solwind.admin.entity.SysUser;
import cn.solwind.admin.jwt.JwtUser;
import cn.solwind.admin.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService{

    @Resource
    SysUserMapper sysUserMapper;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名获取数据库的用户信息
        log.info("User[{}] is sign in!", username);
        SysUser objUser = sysUserMapper.login(username);
        if (objUser == null) {
            log.info("Cannot find User[{}]", username);
            throw new UsernameNotFoundException(String.format("'%s'.用户名或密码错误!", username));
        } else {
            // 根据数据库中的用户信息，构建JwtUser对象

            List<SimpleGrantedAuthority> collect = null;
            // 基于Role名称的权限控制可以使用此方式
            /*collect = new ArrayList<>();
            if(objUser.getIsAdmin()) {
                // admin用户手工写入权限
                collect.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            else {
                collect.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            }*/

            return new JwtUser(objUser.getId(),objUser.getUserName(), objUser.getPassword(), objUser.getStatus(), collect);
        }
    }
}
