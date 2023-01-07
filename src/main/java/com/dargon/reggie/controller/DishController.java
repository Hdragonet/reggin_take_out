package com.dargon.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.Category;
import com.dargon.reggie.domain.Dish;
import com.dargon.reggie.domain.DishFlavor;
import com.dargon.reggie.dto.DishDto;
import com.dargon.reggie.service.CategoryService;
import com.dargon.reggie.service.DishFlavorService;
import com.dargon.reggie.service.DishService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /*
     * 新增菜品
     * */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {


        dishService.saveWithFlavor(dishDto);

        //更新缓存
        String key = "dish_"+dishDto.getCategoryId()+"_"+dishDto.getStatus();

        redisTemplate.delete(key);

        return R.success("添加成功");
    }

    /*
     *分页显示
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<Dish> pageInfo = new Page<>(page, pageSize);

        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();

        wrapper.like(name != null, Dish::getName, name);
        wrapper.orderByAsc(Dish::getSort);
        dishService.page(pageInfo);
        //对象值拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();
        dishDtoPage.setRecords(list);
        for (Dish dish : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Category category = categoryService.getById(dish.getCategoryId());

            if (category != null) {
                dishDto.setCategoryName(category.getName());

            }

            list.add(dishDto);
        }
        records = null;

        return R.success(dishDtoPage);
    }

    /*
     * 根据id返回菜品信息
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {

        DishDto dishDto = dishService.getDishDto(id);

        return R.success(dishDto);
    }


    /*
     *更新菜品信息
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {


        dishService.updateWhitDish(dishDto);

        //缓存失效
        String key = "dish_"+dishDto.getCategoryId()+"_"+dishDto.getStatus();

        //删除key ,更新缓存
        redisTemplate.delete(key);

        return R.success("修改成功");
    }


//    /*
//    * 返回菜品集合
//    * */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//         LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();
//         wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//         wrapper.eq(Dish::getStatus,1);
//         wrapper.orderByAsc(Dish::getSort);
//
//         List<Dish> list = dishService.list(wrapper);
//
//         return R.success(list);
//    }
//

    /*
     * 返回菜品集合
     * */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) throws JsonProcessingException {

        List<DishDto> dishDtoList = null;



        //查看当前菜品分类的所有口味
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        wrapper.eq(Dish::getStatus,1);
        wrapper.orderByAsc(Dish::getSort);

        List<Dish> list = dishService.list(wrapper);
        LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper();

         dishDtoList = new ArrayList<>();
        for (Dish dish1 :list) { //查询菜品的所有口味

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            wrapper1.eq(DishFlavor::getDishId,dishDto.getId());
            List<DishFlavor> listF = dishFlavorService.list(wrapper1);
            dishDto.setFlavors(listF);

            dishDtoList.add(dishDto);
        }

        return  R.success(dishDtoList);
    }


}
