package com.xcl.keycloakservice.apply.realm.controller;

import com.xcl.core.bean.GeneralReturn;
import com.xcl.keycloakservice.apply.realm.service.RealmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/25 13:29
 */
@RestController
@RequestMapping("/realm")
public class RealmController {
    @Autowired
    private RealmService realmService;

    @GetMapping("/queryAll")
    public GeneralReturn queryAll(){
        return GeneralReturn.custom("查询所有realm!",200,realmService.queryAll());
    }
}
