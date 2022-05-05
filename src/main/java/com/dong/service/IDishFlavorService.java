package com.dong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.pojo.DishFlavor;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 服务类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
public interface IDishFlavorService extends IService<DishFlavor> {
    void deleteByName(List<Long> ids);
}
