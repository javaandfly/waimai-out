package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.pojo.User;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface IUserService extends IService<User> {

    User selectByCode(String phone);

}
