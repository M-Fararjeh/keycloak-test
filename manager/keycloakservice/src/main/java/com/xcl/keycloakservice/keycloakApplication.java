package com.xcl.keycloakservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/25 14:46
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.xcl.core.conf",
    "com.xcl.core.aspect",
    "com.xcl.keycloakservice.apply"
})
public class keycloakApplication {
    public static void main(String[] args) {
        SpringApplication.run(keycloakApplication.class, args);
    }
}
