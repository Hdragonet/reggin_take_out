package com.dargon.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.Orders;
import com.dargon.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /*
    * 用户下单
    * */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders, HttpSession session){
        //下单用户id
        Long userid = (Long)session.getAttribute("user");

        ordersService.submit(userid,orders);

        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize,HttpSession session){
        //下单用户id
        Long userid = (Long)session.getAttribute("user");

        if (userid==null){
            return null;
        }

        Page orderPage = ordersService.pageInfoDetail(page, pageSize, userid);

        return R.success(orderPage);
    }

    @GetMapping("/page")
    public R<Page> pageAdmin(int page, int pageSize, LocalDateTime beginTime,LocalDateTime endTime){


        Page orderPage = ordersService.pageInfoDetail(page, pageSize, null);

        return R.success(orderPage);
    }

    @PutMapping
    public R<String> status(@RequestBody Orders orders){

        ordersService.updateById(orders);

        return R.success("修改成功");


    }

}
