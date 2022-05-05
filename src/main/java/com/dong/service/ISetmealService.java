package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.Vo.SetmealVo;
import com.dong.pojo.Setmeal;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface ISetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealVo setmealDto);
    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 批量禁止
     * @param ids
     */
    void setSetMeal(List<Long> ids,Long status);

}
