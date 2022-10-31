package com.websystique.springboot.config;

import com.websystique.springboot.service.security.CustomAuthenFilter;
import com.websystique.springboot.service.security.UsernamePasswordAuthenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 用于实现验证逻辑
    @Autowired
    UsernamePasswordAuthenProvider usernamePasswordAuthenProvider;

    // 和方法生成的bean关联或者
    @Autowired
    @Qualifier("authenticationManagerBean")
    AuthenticationManager authenticationManager;

    /**
     * 请求拦截之后的验证逻辑
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 这里可以提供多个验证器，默认为用户名密码验证
        auth.authenticationProvider(usernamePasswordAuthenProvider);
    }

    /**
     * 配置http请求的拦截设置
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                // 这一步是创建了customAuthenFilter来替换掉UsernamePasswordAuthenticationFilter，实现自定义拦截的逻辑
                .addFilterAt(customAuthenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/home", "/contract", "profile").permitAll()
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/").permitAll()
                .and().logout().permitAll();
    }



    @Bean
    public CustomAuthenFilter customAuthenFilter() {
        CustomAuthenFilter filter = new CustomAuthenFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    // 方法装配bean，默认情况下方法名就是bean的名称
    /**
     * 作用未知
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
