package com.dargon.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dargon.reggie.domain.Dish;
import com.dargon.reggie.domain.DishFlavor;
import com.dargon.reggie.dto.DishDto;
import com.dargon.reggie.mapper.DishMapper;
import com.dargon.reggie.service.DishFlavorService;
import com.dargon.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /*
    * 添加菜品 and 添加口味
    * */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor dishFlavor : flavors) {
             dishFlavor.setDishId(dishId);
        }

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getDishDto(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper();

        wrapper.eq(DishFlavor::getDishId,id);

        List<DishFlavor> list = dishFlavorService.list(wrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWhitDish(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper();
        //查询菜品相关的口味
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //根据菜品id清理菜品的口味
        dishFlavorService.remove(wrapper);


        //传输过来的菜品的口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor :flavors) {
            dishFlavor.setDishId(dishDto.getId());
        }

        //添加传输过来的菜品口味
        dishFlavorService.saveBatch(flavors);


    }


}
