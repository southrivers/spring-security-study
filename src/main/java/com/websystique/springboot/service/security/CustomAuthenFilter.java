package com.websystique.springboot.service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomAuthenFilter extends AbstractAuthenticationProcessingFilter {
    public CustomAuthenFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String userName = httpServletRequest.getParameter("userName");
        String passwd = httpServletRequest.getParameter("passwd");
        AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, passwd);
        // TODO 这一步是用来干啥的？？？
        token.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));
        // 拦截并获取用户信息，将这些信息包装成请求进行验证
        return this.getAuthenticationManager().authenticate(token);
    }
}
