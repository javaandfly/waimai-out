package com.dong.Vo;

import com.dong.pojo.Dish;
import com.dong.pojo.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishVo extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;  //后面要用的

    private Integer copies;  //后面要用的

}
