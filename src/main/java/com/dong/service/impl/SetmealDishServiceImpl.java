package com.dong.service.impl;


import com.dong.Vo.SetmealVo;
import com.dong.mapper.SetmealDishMapper;
import com.dong.pojo.SetmealDish;
import com.dong.service.ISetmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐菜品关系 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {
        @Autowired
        SetmealDishMapper setmealDishMapper;

    @Override
    public void removename(List<Long> ids) {
        if (ids.size()!=0){
            for (int i = 0; i < ids.size(); i++) {
                setmealDishMapper.deleteByName(ids.get(i));
            }
        }

    }
}
