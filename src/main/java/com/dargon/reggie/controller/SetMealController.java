package com.dargon.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.Setmeal;
import com.dargon.reggie.domain.SetmealDish;
import com.dargon.reggie.dto.SetmealDto;
import com.dargon.reggie.service.SetMealDishService;
import com.dargon.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/*
* 套餐管理控制器
*/
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetMealDishService setMealDishService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setMealService.saveWihtDish(setmealDto);

        return R.success("添加成功");
    }

    /*
    * 分页显示
    */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        if (name!=null){
            name = name.trim();
        }

        Page<SetmealDto> dtoPage = setMealService.pageWihtCategory(page, pageSize, name);

        return R.success(dtoPage);
    }

    @DeleteMapping()
    @Transactional
    public R<String> delete(@RequestParam List<Long> ids){

        for (Long aLong : ids) {
            Setmeal setmeal = setMealService.getById(aLong);
            if (setmeal.getStatus()==1){
                return R.error("套餐正在启售，不可删除");
            }
        }

        setMealService.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper();

        for (Long aLong :ids) {

            wrapper.eq(SetmealDish::getSetmealId,aLong);
            setMealDishService.remove(wrapper);
        }


        return R.success("删除成功");
    }

    /*
    *修改商品状态
    */
    @GetMapping("/status")
    public R<String> status(@RequestParam List<Long> ids,Integer status){

        List<Setmeal> list = new ArrayList<>();
        for (Long aLong :ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(aLong);
            setmeal.setStatus(status);
            list.add(setmeal);
        }
        setMealService.updateBatchById(list);


        return R.success("修改成功");
    }

    /*
    *  根据id返回
    */
    @GetMapping("/{id}")
    public R<Setmeal> getById(@PathVariable Long id){

        Setmeal byId = setMealService.getById(id);


        return R.success(byId);
    }

    /*
    * 根据  id返回所有套餐
    */
    @GetMapping("/list")
    public R<List> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        wrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = setMealService.list(wrapper);

        return R.success(list);

    }

    /*
     返回套餐相关的
    */
    @GetMapping("/dish/{id}")
    public R<List> dishList(@PathVariable Long id){

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SetmealDish::getSetmealId,id);

        List<SetmealDish> list = setMealDishService.list(wrapper);



        return R.success(list);
    }

}
