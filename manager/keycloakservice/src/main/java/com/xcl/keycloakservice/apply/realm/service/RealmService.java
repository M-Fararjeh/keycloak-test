package com.xcl.keycloakservice.apply.realm.service;

import com.xcl.keycloakservice.apply.realm.dao.RealmDao;
import com.xcl.keycloakservice.apply.realm.model.Realm;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/24 18:45
 */
@Service
public class RealmService {
    @Resource
    private RealmDao realmDao;
    /**
     * @Description  ：增加一个作用域 数据
     * @author       : xcl
     * @param        : [realm]
     * @return       : int
     * @date         : 2019/6/24 18:46
     */
    public List<Realm> queryAll(){
        return realmDao.queryAll();
    }
}
