package com.dargon.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dargon.reggie.domain.Dish;
import com.dargon.reggie.dto.DishDto;

public interface DishService extends IService<Dish> {


    //新增菜品，同时插入口味对应的口味数据
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getDishDto(Long id);

    public void updateWhitDish(DishDto dishDto);
}
