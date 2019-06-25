package com.xcl.keycloakservice.option;

import com.xcl.keycloakservice.apply.client.model.Client;
import com.xcl.keycloakservice.apply.realm.model.Realm;
import com.xcl.keycloakservice.utils.KeycloakUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;

/**
 * @author :xiaochanglu
 * @Description :会议权限
 * @date :2019/6/25 12:55
 */
public class MeetingService {
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
        Realm realmModel = new Realm("meetingTest", "meetingTest");
        RealmResource realmResource = KeycloakUtil
            .createRealm(realmModel.getRealm_id(), realmModel.getRealm_name(),keycloak);

        Client client_meeting_curl = new Client("meeting_curl","meeting_curl",1);
        Client client_meeting_client = new Client("meeting_client","meeting_client",2);
        String base_url = "http://192.168.52.170:9001";
        //创建一个  客户端  curl
        KeycloakUtil.creatClient(client_meeting_curl.getClient_id(),client_meeting_curl.getClient_name(),client_meeting_curl.getClient_type(),base_url,base_url,realmResource);
        //创建一个  客户端  realmTestClient
        KeycloakUtil.creatClient(client_meeting_client.getClient_id(),client_meeting_client.getClient_name(),client_meeting_client.getClient_type(),base_url,base_url,realmResource);


    }
}
