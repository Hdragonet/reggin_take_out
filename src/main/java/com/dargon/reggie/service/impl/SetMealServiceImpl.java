package com.dargon.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dargon.reggie.domain.Category;
import com.dargon.reggie.domain.Setmeal;
import com.dargon.reggie.domain.SetmealDish;
import com.dargon.reggie.dto.SetmealDto;
import com.dargon.reggie.mapper.SetMealMapper;
import com.dargon.reggie.service.CategoryService;
import com.dargon.reggie.service.SetMealDishService;
import com.dargon.reggie.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional
    public void saveWihtDish(SetmealDto setmealDto) {

        this.save(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        for (SetmealDish setmealDish : setmealDishList) {
            setmealDish.setSetmealId(setmealDto.getId());
        }

        setMealDishService.saveBatch(setmealDishList);

    }

    /*
    *分页显示
    */
    @Override
    public Page<SetmealDto> pageWihtCategory(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        wrapper.like(name != null, Setmeal::getName, name);

        this.page(pageInfo, wrapper);

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();

        for (Setmeal setmeal : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category byId = categoryService.getById(setmealDto.getCategoryId());
            if (byId!=null){
                setmealDto.setCategoryName(byId.getName());
            }
            setmealDtoList.add(setmealDto);
        }


          Page<SetmealDto> dtoPage = new Page<>();
          BeanUtils.copyProperties(pageInfo,dtoPage);
          dtoPage.setRecords(setmealDtoList);


          return dtoPage;
    }
}
