package com.xcl.keycloakservice.apply.user.model;

import com.xcl.core.bean.BaseModel;
import java.io.Serializable;
import lombok.Data;

/**
 * @author :xiaochanglu
 * @Description :用户实体类
 * @date :2019/6/24 16:46
 */
@Data
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = 7773478495123362262L;

    private int user_id;
    private String user_name;
    private String password;
    private int age;

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public User() {
    }
}
