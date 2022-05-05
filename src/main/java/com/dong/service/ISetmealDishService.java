package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.Vo.SetmealVo;
import com.dong.pojo.SetmealDish;

import java.util.List;

/**
 * <p>
 * 套餐菜品关系 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface ISetmealDishService extends IService<SetmealDish> {

    void removename(List<Long> ids);

}
