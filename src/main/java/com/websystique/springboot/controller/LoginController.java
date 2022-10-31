package com.websystique.springboot.controller;

import com.websystique.springboot.model.UserInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {


//    @PostMapping("/login")
    public boolean auth(@RequestBody UserInfo userInfo) {
        return false;
    }
}
