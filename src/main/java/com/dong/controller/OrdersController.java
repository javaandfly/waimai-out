package com.dong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.common.BaseContext;
import com.dong.common.CustomException;
import com.dong.common.R;
import com.dong.pojo.Employee;
import com.dong.pojo.OrderDetail;
import com.dong.pojo.Orders;
import com.dong.pojo.User;
import com.dong.service.IOrderDetailService;
import com.dong.service.IOrdersService;
import com.dong.service.IUserService;
import com.dong.service.impl.OrdersServiceImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IOrderDetailService orderDetailService;
    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("submit")
    @ResponseBody
    public R<String> submit(@RequestBody Orders orders){
        try {
            ordersService.submit(orders);
        }catch (CustomException e){
            return R.error(e.getMessage());
        }

        return R.success("下单成功");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param number
     * @return
     */
    @GetMapping("page")
    @ResponseBody
    public R<Page> pageR(int page, int pageSize, String number){
        Page pageOrder = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageOrder,queryWrapper);
        return R.success(pageOrder);
    }

    /**
     * 改变订单状态
     * @param orders
     * @return
     */
    @PutMapping
    @ResponseBody
    public R<String> update(@RequestBody Orders orders){
        Orders order = ordersService.getById(orders.getId());
        order.setStatus(orders.getStatus());
        ordersService.updateById(order);
        return R.success("更改成功");
    }

    /**
     * 用户私人页面展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    @ResponseBody
    public R<Page> userPage(int page,int pageSize){
        Page pageUser = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageUser,queryWrapper);
        return R.success(pageUser);
    }

    /**
     * 再次购买
     * @param orders
     * @return
     */
    @PostMapping("again")
    @ResponseBody
    public R<String> again(@RequestBody Orders orders){
        return R.success("添加成功");
    }
}
