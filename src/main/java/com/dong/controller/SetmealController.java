package com.dong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.Vo.SetmealVo;
import com.dong.common.CustomException;
import com.dong.common.R;
import com.dong.pojo.Category;
import com.dong.pojo.Dish;
import com.dong.pojo.Setmeal;
import com.dong.service.ICategoryService;
import com.dong.service.IDishService;
import com.dong.service.ISetmealDishService;
import com.dong.service.ISetmealService;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author DongZhi
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
       @Autowired
       private   IDishService iDishService;
       @Autowired
       private ISetmealService  iSetmealService;
       @Autowired
       private ICategoryService categoryService;
       @Autowired
       private ISetmealDishService setmealDishService;
       @Autowired
       private RedisTemplate redisTemplate;

    /**
     * 新增套餐
     * 涉及两张表的操作：套餐表和菜品表；
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealVo setmealDto){

        iSetmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }
    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealVo> dtoPage = new Page<>(page,pageSize);

        //构造条件查询对象
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        iSetmealService.page(pageInfo,queryWrapper);

        //对象的拷贝  注意这里要把分页数据的全集合records给忽略掉
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        //对records对象进行处理然后封装好赋值给list
        List<SetmealVo> list = records.stream().map((item)->{
            SetmealVo setmealDto = new SetmealVo();

            //对setmealDto进行除categoryName的属性进行拷贝(因为item里面没有categoryName)
            BeanUtils.copyProperties(item,setmealDto);

            //获取分类id  通过分类id获取分类对象  然后再通过分类对象获取分类名
            Long categoryId = item.getCategoryId();

            //根据分类id获取分类对象  判断是否为null
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ResponseBody
    public R<String> delete(@RequestParam("ids")  List<Long> ids){
        try {
            iSetmealService.removeWithDish(ids);
        }catch (CustomException e){
            return R.error(e.getMessage());
        }
        //对应的菜品也要删除
        setmealDishService.removename(ids);

        return R.success("删除成功");
    }
    @PostMapping("status"+"/{status}")
    @ResponseBody
    public R<String> updateStatus(@PathVariable Long status ,@RequestParam("ids") List<Long> ids) {

            iSetmealService.setSetMeal(ids,status);


        return R.success("更改成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list( Setmeal setmeal){
        List<Setmeal> setmeals = (List<Setmeal>) redisTemplate.opsForValue().get("setmeal" + setmeal.getCategoryId());
        if (setmeals!=null){
            return R.success(setmeals);
        }
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = iSetmealService.list(lambdaQueryWrapper);
        redisTemplate.opsForValue().set("setmeal"+setmeal.getCategoryId(),list,60, TimeUnit.SECONDS);
        return R.success(list);
    }
}
