package com.dong.Vo;

import com.dong.pojo.Setmeal;
import com.dong.pojo.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealVo extends Setmeal {

      private List<SetmealDish> setmealsDishes=new ArrayList<>();
      private String categoryName;
}
