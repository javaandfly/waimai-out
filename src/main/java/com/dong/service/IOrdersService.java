package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.pojo.Orders;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface IOrdersService extends IService<Orders> {

    void submit(Orders orders);

}
