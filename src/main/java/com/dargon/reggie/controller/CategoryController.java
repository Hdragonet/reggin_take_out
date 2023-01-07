package com.dargon.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.Category;
import com.dargon.reggie.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
* 新整分类
*
* */
@RestController
@RequestMapping("/category")

public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    /*
    * 新增分类
    * */
    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /*
    * 分页显示
    * */
    @GetMapping("/page")
    public R<Page> page( Long page,Long pageSize ){

        Page<Category> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(page1,wrapper);

        return R.success(page1);
    }

    /*
    * 更新菜品分类
    * */
    @PutMapping
    public R<String> update(@RequestBody Category category){

        categoryService.updateById(category);

        return R.success("修改成功");
    }

    /*
    * 删除
    * */
    @DeleteMapping()
    public R<String> delete(Long id){

        categoryService.remove(id);

        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        wrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(wrapper);

        return R.success(list);
    }


}
