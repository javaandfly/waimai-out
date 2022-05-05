package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.pojo.ShoppingCart;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface IShoppingCartService extends IService<ShoppingCart> {
        void updateByDishId(Long dishId,Integer number);

        void updateBySetmealId(Long setmealId, Integer number);

}
