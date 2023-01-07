package com.dargon.reggie.dto;


import com.dargon.reggie.domain.Setmeal;
import com.dargon.reggie.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
