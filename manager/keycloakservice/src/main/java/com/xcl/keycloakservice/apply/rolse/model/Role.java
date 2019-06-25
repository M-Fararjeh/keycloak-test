package com.xcl.keycloakservice.apply.rolse.model;

import com.xcl.core.bean.BaseModel;
import java.io.Serializable;
import lombok.Data;

/**
 * @author :xiaochanglu
 * @Description :角色实体类
 * @date :2019/6/24 16:47
 */
@Data
public class Role extends BaseModel implements Serializable {

    private static final long serialVersionUID = 3604243430512644275L;

    private int role_id;
    private String role_name;
    private String role_description;

    public Role(String role_name, String role_description) {
        this.role_name = role_name;
        this.role_description = role_description;
    }

    public Role() {
    }
}
