package com.dargon.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dargon.reggie.domain.Setmeal;
import com.dargon.reggie.dto.SetmealDto;

public interface SetMealService extends IService<Setmeal> {


    public void saveWihtDish(SetmealDto setmealDto);

    public Page<SetmealDto> pageWihtCategory(int page,int pageSize,String name);

}
