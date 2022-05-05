package com.dong.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.pojo.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 套餐菜品关系 Mapper 接口
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

    void deleteByName(Long id);
}
