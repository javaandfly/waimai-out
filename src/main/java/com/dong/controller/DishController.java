package com.dong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.Vo.DishVo;
import com.dong.common.CustomException;
import com.dong.common.R;
import com.dong.pojo.Category;
import com.dong.pojo.Dish;
import com.dong.pojo.DishFlavor;
import com.dong.service.ICategoryService;
import com.dong.service.IDishFlavorService;
import com.dong.service.IDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
   private IDishService iDishService;
    @Autowired
   private ICategoryService categoryService;
    @Autowired
   private IDishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 添加菜品
     * @param dishVo
     * @return
     */
    @PostMapping
    @ResponseBody
    public R<String> save(@RequestBody DishVo dishVo){
        iDishService.saveWithFlavor(dishVo);
        return R.success("添加成功");
    }
    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造一个分页构造器对象
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishVo> dishDtoPage = new Page<>(page,pageSize);
        //上面对dish泛型的数据已经赋值了，这里对DishDto我们可以把之前的数据拷贝过来进行赋值

        //构造一个条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件 注意判断是否为空  使用对name的模糊查询
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件  根据更新时间降序排
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //去数据库处理分页 和 查询
        iDishService.page(dishPage,queryWrapper);

        //获取到dish的所有数据 records属性是分页插件中表示分页中所有的数据的一个集合
        List<Dish> records = dishPage.getRecords();

        List<DishVo> list = records.stream().map((item) ->{
            //对实体类DishDto进行categoryName的设值

            DishVo dishDto = new DishVo();
            //这里的item相当于Dish  对dishDto进行除categoryName属性的拷贝
            BeanUtils.copyProperties(item,dishDto);
            //获取分类的id
            Long categoryId = item.getCategoryId();
            //通过分类id获取分类对象
            Category category = categoryService.getById(categoryId);
            if ( category != null){
                //设置实体类DishDto的categoryName属性值
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        //对象拷贝  使用框架自带的工具类，第三个参数是不拷贝到属性
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        dishDtoPage.setRecords(list);
        //因为上面处理的数据没有分类的id,这样直接返回R.success(dishPage)虽然不会报错，但是前端展示的时候这个菜品分类这一数据就为空
        //所以进行了上面的一系列操作
        return R.success(dishDtoPage);
    }

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ResponseBody
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        //根据传过来的id批 量或者是单个的删除菜品
        try {
            iDishService.deleteByIds(ids);
        }catch (CustomException e){
            return R.error(e.getMessage());
        }

        dishFlavorService.deleteByName(ids);
        return R.success("删除成功");
    }

    /**
     * 根据id来查询菜品信息和对应的口味信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    @ResponseBody
    public R<Dish> update(@PathVariable Long id){
        DishVo dishVo = iDishService.updateWithFlavorById(id);

        return  R.success(dishVo);
    }
    @PostMapping("status"+"/{status}")
    @ResponseBody
    public R<String> updateStatus(@PathVariable Long status,Long[] ids){

            iDishService.updateStatus(ids,status);
        return R.success("修改成功");
    }
    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){ //会自动映射的
//        //这里可以传categoryId,但是为了代码通用性更强,这里直接使用dish类来接受（因为dish里面是有categoryId的）,以后传dish的其他属性这里也可以使用
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
//        //添加条件，查询状态为1（起售状态）的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = iDishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishVo>> list(Dish dish){ //会自动映射的
        //这里可以传categoryId,但是为了代码通用性更强,这里直接使用dish类来接受（因为dish里面是有categoryId的）,以后传dish的其他属性这里也可以使用
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = iDishService.list(queryWrapper);
        List<DishVo> dishVo1 = (List<DishVo>) redisTemplate.opsForValue().get("dishVo"+dish.getCategoryId());
        if (dishVo1!=null){
            return R.success(dishVo1);
        }
        List<DishVo> dishVos=list.stream().map((item)->{
            DishVo dishVo=new DishVo();
            BeanUtils.copyProperties(item,dishVo);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String name = category.getName();
                dishVo.setCategoryName(name);
            }
            //菜品id
            Long id = item.getId();
           LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper= new LambdaQueryWrapper<>();
           dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            //SQL: select * from dish_flavor where id=#{id}
            dishVo.setFlavors(dishFlavors);
            return dishVo;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set("dishVo"+dish.getCategoryId(),dishVos,60, TimeUnit.SECONDS);
        return R.success(dishVos);
    }

}
