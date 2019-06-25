package com.xcl.keycloakservice.apply.user.controller;

import com.xcl.core.bean.GeneralReturn;
import com.xcl.keycloakservice.apply.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/24 17:43
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/selectByPrimaryKey")
    public GeneralReturn selectByPrimaryKey(int user_id){
        return GeneralReturn.custom("查询客户成功!",200,userService.selectByPrimaryKey(user_id));
    }
}
