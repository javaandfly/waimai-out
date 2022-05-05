package com.dong.service.impl;


import com.dong.mapper.OrderDetailMapper;
import com.dong.pojo.OrderDetail;
import com.dong.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
