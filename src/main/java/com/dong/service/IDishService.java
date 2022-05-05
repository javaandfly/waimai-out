package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.Vo.DishVo;
import com.dong.pojo.Dish;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface IDishService extends IService<Dish> {
    //新增菜品,同时插入菜品对应的口味数据,需要同时操作两张表:dish  dish_flavor
    void saveWithFlavor(DishVo dishDto);
    //显示菜品根据id
    DishVo updateWithFlavorById(Long id);
    //修改属性
    void updateStatus(Long[] id,Long status);
    //删除
    void deleteByIds(List<Long> ids);

}
