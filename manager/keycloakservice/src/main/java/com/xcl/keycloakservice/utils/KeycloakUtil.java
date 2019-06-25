package com.xcl.keycloakservice.utils;

import java.util.ArrayList;
import java.util.List;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * @author :xiaochanglu
 * @Description : Keycloak工具类
 * @date :2019/6/24 18:06
 */
public class KeycloakUtil {
    /**
     * @Description  ：获取  keycloak 实体类
     * @author       : xcl
     * @param        : [serverUrl, realm, username, password, clientId]
     * @return       : org.keycloak.admin.client.Keycloak
     * @date         : 2019/6/24 18:12
     */
    public static Keycloak keycloakInstance(String serverUrl,String realm,String username,String password,String clientId){
        return Keycloak.getInstance(serverUrl,realm,username,password,clientId);
    }
    /**
     * @Description  ：判断 keycloak 是否存在 对应的 realm
     * @author       : xcl
     * @param        : [realmId]
     * @return       : boolean
     * @date         : 2019/6/25 10:29
     */
    public static boolean realmExist(Keycloak keycloak, String realm_id){
        List<RealmRepresentation> list = keycloak.realms().findAll();
        for(RealmRepresentation one:list){
            if(one.getId().equals(realm_id)){
                return true;
            }
        }
        return false;
    }
    /**
     * @Description  ：新建 Realm 作用域
     * 不存在 要创建的  realm 就创建
     * @author       : xcl
     * @param        : [realmId, realmName, keycloak]
     * @return       : org.keycloak.admin.client.resource.RealmResource
     * @date         : 2019/6/25 11:30
     */
    public static RealmResource createRealm(String realmId,String realmName,Keycloak keycloak){
        RealmRepresentation realm = new RealmRepresentation();
        realm.setId(realmId);
        realm.setRealm(realmName);
        realm.setEnabled(true);
        //不存在就创建一个  realm
        if (!KeycloakUtil.realmExist(keycloak, realmId)) {
            RealmsResource realmsResource = keycloak.realms();
            realmsResource.create(realm);
        }
        //进入 具体的 realm
        RealmResource realmResource = keycloak.realm(realmName);
        return realmResource;
    }
    /**
     * @Description  ：判断 某个 realm 是否存在 对应的 client
     * @author       : xcl
     * @param        : [realmResource, client_id]
     * @return       : boolean
     * @date         : 2019/6/25 11:30
     */
    public static boolean clientExist(RealmResource realmResource, String client_id){
        List<ClientRepresentation> list = realmResource.clients().findAll();
        for(ClientRepresentation one:list){
            if(one.getId().equals(client_id)){
                return true;
            }
        }
        return false;
    }
    /**
     * @Description  ：创建客户端
     * @author       : xcl
     * @param        : [clientId, clientName, clientType, baseUrl, adminUrl, realmResource]
     * @return       : void
     * @date         : 2019/6/25 11:30
     */
    public static void creatClient(
        String clientId,String clientName,int clientType,
        String baseUrl,String adminUrl,RealmResource realmResource){

        ClientRepresentation client = new ClientRepresentation();
        client.setId(clientId);
        client.setName(clientName);
        client.setEnabled(true);
        client.setBaseUrl(baseUrl);
        client.setAdminUrl(adminUrl);
        if(clientType == 1){
            //为true keycloak 可以在不提供任何秘密的情况下启动登录
            client.setPublicClient(true);
            //为true keycloak 能够使用用户名和密码直接登录
            client.setDirectAccessGrantsEnabled(true);
        }else if(clientType == 2){
            //这告诉Keycloak客户端永远不会启动登录过程，
            //但是当它收到Bearer令牌时，它将检查所述令牌的有效性
            client.setBearerOnly(true);
        }
        //不存在  客户端 就创建一个  客户端
        if(!KeycloakUtil.clientExist(realmResource,clientId)){
            realmResource.clients().create(client);
        }
    }

    /**
     * @Description  ：获取某个指定的客户端
     * @author       : xcl
     * @param        : [realmResource, client_id]
     * @return       : org.keycloak.admin.client.resource.ClientResource
     * @date         : 2019/6/25 11:47
     */
    public static ClientResource getClient(RealmResource realmResource,String client_id){
        if(clientExist(realmResource,client_id)){
            return realmResource.clients().get(client_id);
        }
        return null;
    }
    /**
     * @Description  ：判断 某个 角色 是否存在
     * @author       : xcl
     * @param        : [realmResource, role_name]
     * @return       : boolean
     * @date         : 2019/6/25 11:35
     */
    public static boolean roleExist(RolesResource rolesResource,String role_name){
        List<RoleRepresentation> list = rolesResource.list();
        for(RoleRepresentation one:list){
            if(one.getName().equals(role_name)){
                return true;
            }
        }
        return false;
    }
    /**
     * @Description  ：创建角色
     * @author       : xcl
     * @param        : [name, description]
     * @return       : org.keycloak.representations.idm.RoleRepresentation
     * @date         : 2019/6/24 18:08
     */
    public static void creatRole(
        String role_name,String role_description,
        RealmResource realmResource,String client_id){
        RoleRepresentation admin = new RoleRepresentation();
        admin.setName(role_name);
        admin.setDescription(role_description);
        RolesResource rolesResource ;
        if(client_id==null||client_id.equals("")){
            //创建在 realm 下的角色
            rolesResource = realmResource.roles();
        }else{
            //创建在 客户端 下的角色
            rolesResource = realmResource.clients().get(client_id).roles();
        }
        if(!roleExist(rolesResource,role_name)){
            rolesResource.create(admin);
        }
    }
    /**
     * @Description  ：用户是否存在
     * @author       : xcl
     * @param        : [realmResource, username]
     * @return       : boolean
     * @date         : 2019/6/25 12:07
     */
    public static boolean userExist(RealmResource realmResource,String username){
        List<UserRepresentation> list = realmResource.users().list();
        for(UserRepresentation one:list){
            if(one.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
    /**
     * @Description  ：创建用户
     * @author       : xcl
     * @param        : [username, password]
     * @return       : org.keycloak.representations.idm.UserRepresentation
     * @date         : 2019/6/24 18:09
     */
    public static UserRepresentation createUser(String username,String password,RealmResource realmResource){
        // 新建用户 admin
        UserRepresentation clientUser = new UserRepresentation();
        // 设置登录账号
        clientUser.setUsername(username);
        // 设置账号“启用”
        clientUser.setEnabled(true);
        // 设置密码
        List<CredentialRepresentation> credentials = new ArrayList<CredentialRepresentation>();
        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setType(CredentialRepresentation.PASSWORD);
        cr.setValue(password);
        cr.setTemporary(false);
        credentials.add(cr);
        clientUser.setCredentials(credentials);
        if(!userExist(realmResource,username)){
            realmResource.users().create(clientUser);
        }
        return clientUser;
    }
    /**
     * @Description  ：将 某个 用户 添加到 某个 客户端的角色中
     * @author       : xcl
     * @param        : [realmResource, cliengId, roleName]
     * @return       : void
     * @date         : 2019/6/25 12:18
     */
    public static boolean clientAddUser(RealmResource realmResource,String cliengId,String roleName){
        List<RoleRepresentation> clientRolesToAdd = new ArrayList<RoleRepresentation>();
        RoleRepresentation clientRole_admin = realmResource.clients().get(cliengId).roles().get(roleName).toRepresentation();
        return clientRolesToAdd.add(clientRole_admin);
    }
    /**
     * @Description  ：获取指定用户的 token
     * @author       : xcl
     * @param        : [serverUrl, realm, username, password, clientId]
     * @return       : java.lang.String
     * @date         : 2019/6/24 18:13
     */
    public static String getToken(String serverUrl,String realm,String username,String password,String clientId){
        //获取  keycloak 实体类
        Keycloak keycloak_user = keycloakInstance(serverUrl,realm,username,password,clientId);
        // 取得accesstoken
        return keycloak_user.tokenManager().getAccessTokenString();
    }
}
