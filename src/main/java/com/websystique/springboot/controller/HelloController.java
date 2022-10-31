package com.websystique.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/")
	String login() {
		return "login";
	}

	/**
	 * 按理说这里没有登录的时候应该会被拦截
	 * @return
	 */
	@RequestMapping("/home")
	String home() {
		return "home";
	}
}
