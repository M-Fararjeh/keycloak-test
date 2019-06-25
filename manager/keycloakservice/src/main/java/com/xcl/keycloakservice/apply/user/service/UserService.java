package com.xcl.keycloakservice.apply.user.service;

import com.xcl.keycloakservice.apply.user.dao.UserDao;
import com.xcl.keycloakservice.apply.user.model.User;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author :xiaochanglu
 * @Description :用户服务
 * @date :2019/6/24 17:41
 */
@Service
public class UserService {
    @Resource
    private UserDao userDao;

    public User selectByPrimaryKey(int user_id){
        return userDao.selectByPrimaryKey(user_id);
    }
}
