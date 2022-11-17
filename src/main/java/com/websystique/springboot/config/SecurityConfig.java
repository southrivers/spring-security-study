package com.websystique.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 当我们使用了自定义的securityconfigeradapter的时候，就意味着我们需要重写所有的认证、验证的逻辑
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomAuthenticationFilter customAuthenticationFilter;

    /**
     * 由于我们需要主动调用** 用户自定义的 **认证的逻辑，因此这里需要再springsecurity给我们提供的配置类中完成manager的装配
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置上一个方法返回的AuthenticationManager
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 当我们请求认证用户名密码的时候，authenticationManager会接收密码并通过我们提供的加密的方式对其进行加密
        // 该过程是不被外部知晓的
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //3、 除了上述验证的过程之外，接下来需要做的是配置请求是否拦截等信息，这样说的话login应该是不会被拦截的(已确认确实不会被拦截)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁止csrf是因为我们并不会使用formlogin的方式，而是使用rest的方式进行验证的，因此这里是不必要配置csrf的
        http.csrf().disable()
                // 只允许login在没有认证的情况下访问，并不允许其他请求的访问
                .authorizeRequests().antMatchers("/login").permitAll().anyRequest().authenticated()
                // 由于我们并不使用form+session的方式进行认证，因此这里永远不去创建session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 这个bean跟spring security无关，只是装配这个bean需要一个地方，暂时放到这里来了而已
     * @return
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
