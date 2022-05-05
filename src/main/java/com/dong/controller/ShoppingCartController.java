package com.dong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dong.common.BaseContext;
import com.dong.common.R;
import com.dong.pojo.Dish;
import com.dong.pojo.ShoppingCart;
import com.dong.service.IShoppingCartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private IShoppingCartService shoppingCartService;
    /**
     * 添加商品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public R<ShoppingCart> addShop(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        String dishFlavor = shoppingCart.getDishFlavor();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null ){
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            //菜皮口味应该也要
//            queryWrapper.eq(ShoppingCart::getDishFlavor,dishFlavor);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //SQL select * from shopping_cart where id=? and (dish_id=? and dish_flavor=?)/setmeal_id=?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if(cartServiceOne!=null ){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cartServiceOne=shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**\
     * 购物车商品展示
     * @param
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> listR(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);

    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("clean")
    @ResponseBody
    public R<String> clear(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

    /**
     * 删除单个菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("sub")
    @ResponseBody
    public  R<String> delete(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if (dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
            Integer number = cartServiceOne.getNumber();
            number--;
            if (number==0){
                shoppingCartService.remove(queryWrapper);
            }else {
                shoppingCartService.updateByDishId(dishId,number);
            }
//            queryWrapper.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
            ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
            Integer number = cartServiceOne.getNumber();
            number--;
            if (number==0){
                shoppingCartService.remove(queryWrapper);
            }else {
                shoppingCartService.updateBySetmealId(setmealId,number);
            }
        }

        return R.success("删除成功");
    }
}
