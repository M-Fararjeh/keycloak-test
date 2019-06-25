package com.xcl.keycloakservice.option;

import com.xcl.keycloakservice.apply.client.model.Client;
import com.xcl.keycloakservice.apply.realm.model.Realm;
import com.xcl.keycloakservice.utils.KeycloakUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;

/**
 * @author :xiaochanglu
 * @Description :课程权限
 * @date :2019/6/25 12:56
 */
public class LessonService {
    public void option() {
        String serverUrl = "http://192.168.52.170:9001/auth";
        String login_realm = "master";
        String login_username = "admin";
        String login_password = "admin";
        String login_clientId = "admin-cli";

        //通过  admin  登陆  获取  keycloak 实体类
        Keycloak keycloak = KeycloakUtil.keycloakInstance(
            serverUrl, login_realm, login_username, login_password, login_clientId
        );

        // 新建 Realm 作用域
        Realm realmModel = new Realm("lessonTest", "lessonTest");
        RealmResource realmResource = KeycloakUtil
            .createRealm(realmModel.getRealm_id(), realmModel.getRealm_name(),keycloak);

        Client client_lesson_curl = new Client("lesson_curl","lesson_curl",1);
        Client client_lesson_client = new Client("lesson_client","lesson_client",2);
        String base_url = "http://192.168.52.170:9001";
        //创建一个  客户端  curl
        KeycloakUtil.creatClient(client_lesson_curl.getClient_id(),client_lesson_curl.getClient_name(),client_lesson_curl.getClient_type(),base_url,base_url,realmResource);
        //创建一个  客户端  realmTestClient
        KeycloakUtil.creatClient(client_lesson_client.getClient_id(),client_lesson_client.getClient_name(),client_lesson_client.getClient_type(),base_url,base_url,realmResource);


    }
}
