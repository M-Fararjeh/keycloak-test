package com.xcl.keycloakservice.apply.realm.dao;

import com.xcl.keycloakservice.apply.realm.model.Realm;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/24 18:44
 */
@Mapper
public interface RealmDao {
    List<Realm> queryAll();
}
