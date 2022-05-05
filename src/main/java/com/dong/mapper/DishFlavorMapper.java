package com.dong.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.pojo.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品口味关系表 Mapper 接口
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    void deleteByName(Long id);
}
