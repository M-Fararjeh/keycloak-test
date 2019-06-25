package com.xcl.keycloakservice.apply.user.dao;

import com.xcl.keycloakservice.apply.user.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :xiaochanglu
 * @Description :
 * @date :2019/6/24 16:54
 */
@Mapper
public interface UserDao {
    User selectByPrimaryKey(Integer user_id);
}
