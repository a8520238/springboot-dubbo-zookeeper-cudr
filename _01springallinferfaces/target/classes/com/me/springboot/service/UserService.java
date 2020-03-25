package com.me.springboot.service;

import com.me.springboot.model.User;

import java.util.List;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 22.09 2020/3/20
 */
public interface UserService {
    /**
     * 分页查询
     * @param paramMap
     * @return
     */
    List<User> getUserByPage(Map<String, Object> paramMap);

    /**
     * 分页查询数据总数
     * @return
     */
    int getUserByTotal();
    User selectByPrimaryKey(int id);

    int addUser (User user);
    int updateUser (User user);
    int deleteUser (Integer id);
    User getUserById(Integer id);
}
