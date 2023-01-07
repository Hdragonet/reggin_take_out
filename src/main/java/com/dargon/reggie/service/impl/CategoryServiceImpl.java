package com.dargon.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dargon.reggie.domain.Category;
import com.dargon.reggie.domain.Dish;
import com.dargon.reggie.domain.Setmeal;
import com.dargon.reggie.exception.CustomException;
import com.dargon.reggie.mapper.CategoryMapper;
import com.dargon.reggie.service.CategoryService;
import com.dargon.reggie.service.DishService;
import com.dargon.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    /*
     *
     * 根据id删除分类，删除前先判断
     *
     * */
    public void remove(Long id) {

        //如果分类关联了菜品 或者 套餐抛出业务异常
        LambdaQueryWrapper<Dish> dishWrapper =new LambdaQueryWrapper();
        dishWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishWrapper);
        if (count>0){
           throw new CustomException("当下分类关联了菜品,删除失败");
        }
        LambdaQueryWrapper<Setmeal> setMeal =new LambdaQueryWrapper();
        setMeal.eq(Setmeal::getCategoryId,id);
        int count1 = setMealService.count(setMeal);
        if (count1>0){
            throw new CustomException("当下分类关联了套餐,删除失败");
        }
        //正常删除分类
        super.removeById(id);
    }

}
