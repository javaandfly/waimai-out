package com.dong.service.impl;


import com.dong.mapper.UserMapper;
import com.dong.pojo.User;
import com.dong.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
  @Autowired
  private  UserMapper userMapper;
    @Override
    public User selectByCode(String phone) {
        return userMapper.selectByCode(phone);
    }
}
