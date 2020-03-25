package com.me.springboot.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.me.springboot.mapper.UserMapper;
import com.me.springboot.model.User;
import com.me.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 22.28 2020/3/20
 */

@Component //spring注解
@Service(interfaceClass = UserService.class)
//@Service(interfaceClass = UserService.class) //dubbo的注解
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public List<User> getUserByPage(Map<String, Object> paramMap) {
        return userMapper.selectUserByPage(paramMap);
    }

    @Override
    public int getUserByTotal() {

        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);

        Integer totalRows = (Integer)redisTemplate.opsForValue().get("totalRows");
        if (totalRows == null) {
            synchronized (this) {
                totalRows = (Integer)redisTemplate.opsForValue().get("totalRows");
                if (totalRows == null) {
                    totalRows = userMapper.selectUserByTotal();
                    redisTemplate.opsForValue().set("totalRows", totalRows);
                }
            }
        }
        return totalRows;
    }

    @Override
    public User selectByPrimaryKey(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addUser(User user) {
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        int add = userMapper.insertSelective(user);
        if (add > 0) {
            int totalRows = userMapper.selectUserByTotal();
            redisTemplate.opsForValue().set("totalRows", totalRows);
        }
        return add;
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int deleteUser(Integer id) {
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        int delete = userMapper.deleteByPrimaryKey(id);
        if (delete > 0) {
            int totalRows = userMapper.selectUserByTotal();
            redisTemplate.opsForValue().set("totalRows", totalRows);
        }
        return delete;
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
