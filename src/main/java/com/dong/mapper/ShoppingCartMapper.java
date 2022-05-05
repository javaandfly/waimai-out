package com.dong.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    void updateByDishId(Long dishId, Integer number);

    void updateBySetmeal(Long setmealId, Integer number);
}
