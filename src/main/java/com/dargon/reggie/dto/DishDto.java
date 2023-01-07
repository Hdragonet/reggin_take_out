package com.dargon.reggie.dto;

import com.dargon.reggie.domain.Dish;
import com.dargon.reggie.domain.DishFlavor;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //菜品口味列表
    private List<DishFlavor> flavors = new ArrayList<>();
    //分类名
    private String categoryName;

    private Integer copies;
}
