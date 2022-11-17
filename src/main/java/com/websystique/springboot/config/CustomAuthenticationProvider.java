package com.websystique.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 提供自定义的authenticationProvider来取代默认的provider
 */
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 这个方法是在controller里面主动调用的
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // FIXME 这里我们就直接获取到了用户名密码，这种情况下我们可以直接从数据库中验证是否正确，而不用实现userDetailservice了
        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        // 这里对其认证认证成功就返回token，就不用再controller里面创建并生成token了
        String pwd = userDetailsService.loadUserByUsername(userName).getPassword();
        // TODO 这里比较密码和加密之后的密码的方式可能存在问题，应该用encoder.match来进行比对
//        if (pwd!= null && pwd.equals(passwordEncoder.encode(password))) {
        if (passwordEncoder.matches(password, pwd)) {
            return new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>());
        }
        throw new BadCredentialsException("user not found!");
    }

    /**
     * authenticationProvider会接受用户信息，并进行验证，这里的authentication参数指代了验证的用户
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // provider接受的参数类型
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
