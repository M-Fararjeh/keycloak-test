package com.xcl.test.apply.test.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/25 17:47
 */

@RestController
public class TestController {
    @GetMapping(value = "/test1")
    public String test1(){
        return "test1";
    }

    @GetMapping(value = "/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "Admin";
    }

    @GetMapping("/user")
    @Secured("ROLE_USER")
    public String user() {
        return "User";
    }

}
