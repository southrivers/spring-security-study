package com.websystique.springboot.controller;

import com.websystique.springboot.model.UserInfo;
import com.websystique.springboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {


	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	AuthenticationManager authenticationManager;
	/**
	 * 按理说这里没有登录的时候应该会被拦截
	 * @return
	 */
	@GetMapping("/home")
	String home() {
		return "home";
	}

	@PostMapping("/login")
	public String login(@RequestBody UserInfo userInfo) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userInfo.getUserName(), userInfo.getPasswd());
		// 这里获取的用户名密码将会传递到AuthenticationManager，认证失败会抛出异常，这里会调用自己实现的userdetailservice实现类，返回从
		// FIXME 数据库中查询到的信息，对用户信息进行比对，比对是发生在这里，只是省略了比对的过程。
		authenticationManager.authenticate(token);
		// 程序运行到这里说明用户名、密码验证通过
		String jwtToken = jwtUtil.generate(userInfo.getUserName());
		return jwtToken;
	}
}
