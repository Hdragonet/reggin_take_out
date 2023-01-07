package com.dargon.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.ShoppingCart;
import com.dargon.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
       //获取当前用户id
        Long userid = (Long)session.getAttribute("user");
        shoppingCart.setUserId(userid);
      //
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper();
        wrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        wrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        wrapper.eq(ShoppingCart::getUserId,userid);

        ShoppingCart one = shoppingCartService.getOne(wrapper);

        if (one!=null){
            wrapper = new LambdaQueryWrapper<>();
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);

          }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
         }

         return R.success(one);


    }


    @GetMapping("/list")
    public R<List> getAll(HttpSession session){
        //获取当前用户id
            Long userid = (Long)session.getAttribute("user");
           if (userid==null){
                 return R.error("查询错误");
           }
           LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper();
           wrapper.eq(ShoppingCart::getUserId,userid);
           wrapper.orderByDesc(ShoppingCart::getCreateTime);

           List<ShoppingCart> list = shoppingCartService.list(wrapper);

           return R.success(list);

    }

    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session){
        //获取当前用户id
        Long userid = (Long)session.getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ShoppingCart::getUserId,userid);
        shoppingCartService.remove(wrapper);


          return R.success("成功");
    }


    /*
    * 菜品数量减一
    * */
    @PostMapping("/sub")
    public R<String> sub(HttpSession session,@RequestBody ShoppingCart shoppingCart){
        Long userid = (Long)session.getAttribute("user");

         shoppingCartService.sub(userid,shoppingCart);


        return R.success("成功");
    }



}
