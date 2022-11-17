package com.websystique.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 实现自定义的类用于读取用户的信息，TODO 使用这种token的方式会被usernamepasswordauthenticationfilter拦截请求并进行认证
 */
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    PasswordEncoder encoder;

    /**
     * 此处模拟从数据库中读取用户信息
     *
     * TODO 在这里做完处理之后需要将用户认证的逻辑织入当前的鉴权体系
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> db = new HashMap<>();
        db.put("wes", encoder.encode("123"));
        if (db.containsKey(username)) {
            return new User(username, db.get(username), new ArrayList<>());
        }
        throw new  UsernameNotFoundException("user not founded!");
    }
}
