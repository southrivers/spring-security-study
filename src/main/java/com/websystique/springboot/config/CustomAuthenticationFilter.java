package com.websystique.springboot.config;

import com.websystique.springboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 自定义过滤器，用于拦截请求并验证请求头携带的信息是否是合法的
 *
 * 在完成filter的定义之后，我们还需要在spring security中合适的地方将其注入
 */
@Service
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头的
        String authenticationHeader = request.getHeader("Authentication");
        if (authenticationHeader == null || authenticationHeader.isEmpty() || !authenticationHeader.startsWith("at")) {
            // 非法请求的话就继续使用下一个过滤器，并且直接return，TODO 待确认 这里应该不会再使用UsernamePasswordAuthenticationFilter了
            filterChain.doFilter(request,response);
            return;
        }
        String token = authenticationHeader.split(" ")[1].trim();
        if (!jwtUtil.valid(token)) {
            filterChain.doFilter(request,response);
            return;
        }
        // 请求有效，则进入这一步进行处理, FIXME 这里的含义待理解， 貌似是用来记录用户信息到上下文中，为后续请求的处理获取当前用户信息提供了机制
        String username = jwtUtil.getUsername(token);
        // 这里需要通过构造函数确认使用那个构造函数，传一个空的权限选项代表了该token是已经认证了得
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // 继续过滤，在过滤链中如果同一种类型的过滤器被使用过一次，就直接会调到下一个类型的filter了，就不会继续使用当前的这个filter了
        filterChain.doFilter(request, response);
    }
}
