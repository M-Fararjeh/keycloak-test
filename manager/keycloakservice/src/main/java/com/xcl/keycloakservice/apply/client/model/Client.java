package com.xcl.keycloakservice.apply.client.model;

import com.xcl.core.bean.BaseModel;
import java.io.Serializable;
import lombok.Data;

/**
 * @author :xiaochanglu
 * @Description :客户端实体类
 * @date :2019/6/24 16:49
 */
@Data
public class Client extends BaseModel implements Serializable {

    private static final long serialVersionUID = -1019911321836845723L;

    private String client_id;
    private String client_name;
    private int client_type;
    private String client__description;

    public Client(String client_id, String client_name, int client_type) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_type = client_type;
    }

    public Client() {
    }
}
