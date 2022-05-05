package com.dong.service.impl;


import com.dong.mapper.ShoppingCartMapper;
import com.dong.pojo.ShoppingCart;
import com.dong.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {
        @Autowired
        ShoppingCartMapper shoppingCartMapper;
    @Override
    public void updateByDishId(Long dishId, Integer number) {
        shoppingCartMapper.updateByDishId(dishId,number);
    }

    @Override
    public void updateBySetmealId(Long setmealId, Integer number) {
        shoppingCartMapper.updateBySetmeal(setmealId,number);
    }
}
