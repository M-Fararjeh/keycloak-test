package com.xcl.permission.apply.login.controller;

import com.xcl.permission.core.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/25 16:24
 */
@RestController
@Slf4j
public class LoginController {
    @GetMapping("/test1")
    public String test1(){
        return "test1";
    }

    @GetMapping(value = "/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        log.info("=================="+System.currentTimeMillis());
        return "Admin";
    }

    @GetMapping("/user")
    @Secured("ROLE_USER")
    public String user() {
        log.info("++++++++++++++++++"+System.currentTimeMillis());
        return "User";
    }

    @GetMapping("/login")
    public String getToken(String username,String password){
        String serverUrl = "http://192.168.52.170:9001/auth";
        return KeycloakUtil.getToken(serverUrl,"realmTest",username,password,"curl");
    }

    public void cs(String token){
        //1、设置client配置信息
        AdapterConfig adapterConfig = new AdapterConfig();
        //realm name
        adapterConfig.setRealm("realmTest");
        //client_id
        adapterConfig.setResource("curl");
        //认证中心keycloak地址
        adapterConfig.setAuthServerUrl("https://192.168.52.170:9001/auth");
        //访问https接口时，禁用证书检查。
        adapterConfig.setDisableTrustManager(true);
        //2、根据 client 配置信息构建 KeycloakDeployment 对象
        KeycloakDeployment deployment = KeycloakDeploymentBuilder.build(adapterConfig);
        System.out.println("*****"+deployment);

        //3、执行token签名验证和有效性检查（不通过会抛异常）
        try {
            AccessToken accesToken = AdapterTokenVerifier.verifyToken(token, deployment);

            System.out.println("======"+accesToken.toString());
        } catch (VerificationException e) {
            e.printStackTrace();
        }
        System.out.println("验证通过");
    }
}
