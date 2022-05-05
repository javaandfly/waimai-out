package com.dong.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dong.Vo.DishVo;

import com.dong.common.CustomException;
import com.dong.mapper.DishMapper;
import com.dong.pojo.Dish;
import com.dong.pojo.DishFlavor;
import com.dong.service.IDishFlavorService;
import com.dong.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Autowired
    private DishMapper dishMapper;
    /**
     * 新增菜品同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional //涉及到对多张表的数据进行操作,需要加事务，需要事务生效,需要在启动类加上事务注解生效
    public void saveWithFlavor(DishVo dishDto) {
        //保存菜品的基本信息到菜品表dish中
        this.save(dishDto);
        Long dishId = dishDto.getId();

        //为了把dishId  set进flavors表中
        //拿到菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //这里对集合进行赋值 可以使用循环或者是stream流
        flavors = flavors.stream().map((item) ->{
            //拿到的这个item就是这个DishFlavor集合
            item.setDishId(dishId);
            return item; //记得把数据返回去
        }).collect(Collectors.toList()); //把返回的集合搜集起来,用来被接收

        //把菜品口味的数据到口味表 dish_flavor  注意dish_flavor只是封装了name value 并没有封装dishId(从前端传过来的数据发现的,然而数据库又需要这个数据)
        dishFlavorService.saveBatch(dishDto.getFlavors()); //这个方法是批量保存
    }

    @Override
    @Transactional
    public DishVo updateWithFlavorById(Long id) {
        Dish dish = this.getById(id);
        //查询当前菜品对应的口味信息,从dish_flavor查询  条件查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        //然后把查询出来的flavors数据set进行 DishDto对象
        DishVo dishVo = new DishVo();
        //把dish表中的基本信息copy到dishDto对象，因为才创建的dishDto里面的属性全是空
        BeanUtils.copyProperties(dish,dishVo);

        dishVo.setFlavors(flavors);

        return dishVo;
    }

    @Override
    public void updateStatus(Long[] id,Long status) {
        for (int i = 0; i < id.length; i++) {
            dishMapper.updateStatus(id[i],status);
        }

    }

    /**
     *套餐批量删除和单个删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {

        //构造条件查询器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //先查询该菜品是否在售卖，如果是则抛出业务异常
        queryWrapper.in(ids!=null,Dish::getId,ids);
        List<Dish> list = this.list(queryWrapper);
        for (Dish dish : list) {
            Long status = dish.getStatus();
            //如果不是在售卖,则可以删除
            if (status == 0){
                this.removeById(dish.getId());
            }else {
                //此时应该回滚,因为可能前面的删除了，但是后面的是正在售卖
                throw new CustomException("删除菜品中有正在售卖菜品,无法全部删除");
            }
        }

    }

}
