package com.dong.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dong.Vo.SetmealVo;
import com.dong.common.CustomException;
import com.dong.mapper.SetmealMapper;
import com.dong.pojo.Setmeal;
import com.dong.pojo.SetmealDish;
import com.dong.service.ISetmealDishService;
import com.dong.service.ISetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {
    @Autowired
    ISetmealDishService setmealDishService;
    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealVo setmealDto) {

        //保存套餐的基本信息，操作setmeal,执行insert
        this.save(setmealDto);
        log.info(setmealDto.toString()); //查看一下这个套餐的基本信息是什么

        //保存套餐和菜品的关联信息，操作setmeal_dish ,执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealsDishes();
        //注意上面拿到的setmealDishes是没有setmeanlId这个的值的，通过debug可以发现
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item; //这里返回的就是集合的泛型
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes); //批量保存
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //sql语句应该是这样的:select count(*) setmeal where id in () and status = 1;
        //查询套餐的状态，看是否可以删除
        for (int i = 0; i < ids.size(); i++) {
            Setmeal setmeal = this.getById(ids.get(i));
            if (setmeal.getStatus()==0){
                this.removeById(ids.get(i));
            }else {
               throw  new CustomException("有商品正在售卖，无法删除");
            }
        }

    }

    @Override
    public void setSetMeal(List<Long> ids,Long status) {
        if (ids.size()!=0){
            for (int i = 0; i < ids.size(); i++) {
                setmealMapper.updateStatus(ids.get(i),status);
            }
        }

    }
}
