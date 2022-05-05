package com.dong.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByCode(String phone);

}
