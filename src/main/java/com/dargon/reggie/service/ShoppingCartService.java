package com.dargon.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dargon.reggie.domain.ShoppingCart;


public interface ShoppingCartService extends IService<ShoppingCart> {


    /*
    * 购物车商品数量--
    * */
    public void sub(Long userid,ShoppingCart shoppingCart);
}
