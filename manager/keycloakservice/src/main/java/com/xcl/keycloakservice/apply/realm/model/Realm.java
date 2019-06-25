package com.xcl.keycloakservice.apply.realm.model;

import com.xcl.core.bean.BaseModel;
import java.io.Serializable;
import lombok.Data;

/**
 * @author :xiaochanglu
 * @Description :作用域 实体类
 * @date :2019/6/24 16:48
 */
@Data
public class Realm extends BaseModel implements Serializable {

    private static final long serialVersionUID = -1119326423408757308L;

    private String realm_id;
    private String realm_name;
    private String realm_description;

    public Realm(String realm_id, String realm_name) {
        this.realm_id = realm_id;
        this.realm_name = realm_name;
    }

    public Realm() {
    }
}
