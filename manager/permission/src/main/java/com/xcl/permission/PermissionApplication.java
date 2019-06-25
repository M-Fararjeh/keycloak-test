package com.xcl.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/24 16:30
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.xcl.core.conf",
    "com.xcl.core.aspect",
    "com.xcl.permission.apply"
})
public class PermissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }
}
