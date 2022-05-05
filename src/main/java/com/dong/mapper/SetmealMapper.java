package com.dong.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dong.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 套餐 Mapper 接口
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    void updateStatus(Long id, Long status);

}
