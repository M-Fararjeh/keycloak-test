package com.xcl.keycloakservice.option;

import com.xcl.keycloakservice.apply.client.model.Client;
import com.xcl.keycloakservice.apply.realm.model.Realm;
import com.xcl.keycloakservice.apply.rolse.model.Role;
import com.xcl.keycloakservice.apply.user.model.User;
import com.xcl.keycloakservice.utils.KeycloakUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/24 18:48
 */
public class keycloakService {

    public void option() {
        String serverUrl = "http://192.168.52.170:9001/auth";
        String master_realm = "master";
        String admin_username = "admin";
        String admin_password = "admin";
        String admin_clientId = "admin-cli";
        //通过  admin  登陆  获取  keycloak 实体类
        Keycloak keycloak = KeycloakUtil.keycloakInstance(
            serverUrl, master_realm, admin_username, admin_password, admin_clientId
        );

        // 新建 Realm 作用域
        Realm realmModel = new Realm("realmTest", "realmTest");
        RealmResource realmResource = KeycloakUtil.createRealm(realmModel.getRealm_id(), realmModel.getRealm_name(),keycloak);


        Client client_curl = new Client("curl","curl",1);
        Client client_realmTestClient = new Client("realmTestClient","realmTestClient",2);
        String base_url = "http://192.168.52.170:9001";
        //创建一个  客户端  curl
        KeycloakUtil.creatClient(client_curl.getClient_id(),client_curl.getClient_name(),client_curl.getClient_type(),base_url,base_url,realmResource);
        //创建一个  客户端  realmTestClient
        KeycloakUtil.creatClient(client_realmTestClient.getClient_id(),client_realmTestClient.getClient_name(),client_realmTestClient.getClient_type(),base_url,base_url,realmResource);


        Role role_admin = new Role("admin","admin 角色");
        Role role_user = new Role("user","user 角色");
        //创建一个  角色  admin
        KeycloakUtil.creatRole(role_admin.getRole_name(),role_admin.getRole_description(),realmResource,"");
        //创建一个  角色  user
        KeycloakUtil.creatRole(role_user.getRole_name(),role_user.getRole_description(),realmResource,"");


        User user_admin = new User("admin","admin");
        User user_user = new User("user","user");
        //创建一个  用户  admin
        KeycloakUtil.createUser(user_admin.getUser_name(),user_admin.getPassword(),realmResource);
        //创建一个  用户  user
        KeycloakUtil.createUser(user_user.getUser_name(),user_user.getPassword(),realmResource);

//        System.out.println(KeycloakUtil.clientAddUser(realmResource,"realmTestClient","admin"));
//        System.out.println(KeycloakUtil.clientAddUser(realmResource,"realmTestClient","user"));

        //获取  keycloak 实体类
        System.out.println("admin_accessToken:======"+KeycloakUtil.getToken(serverUrl,"realmTest","admin","admin","curl"));
        //获取  keycloak 实体类
        System.out.println("user_accessToken:======"+KeycloakUtil.getToken(serverUrl,"realmTest","user","user","curl"));

    }


    public static void main(String[] args) {
        keycloakService keycloakService = new keycloakService();
        keycloakService.option();
    }

}
