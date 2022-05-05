package com.dong.controller;


import com.dong.common.CustomException;
import com.dong.common.R;
import com.dong.pojo.Orders;
import com.dong.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
