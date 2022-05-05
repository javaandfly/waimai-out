package com.dong.service.impl;


import com.dong.mapper.DishFlavorMapper;
import com.dong.pojo.DishFlavor;
import com.dong.service.IDishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements IDishFlavorService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Override
    @Transactional
    public void deleteByName(List<Long> ids) {

        for (int i = 0; i < ids.size(); i++) {
            dishFlavorMapper.deleteByName(ids.get(i));
        }
    }
}
