package com.dargon.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dargon.reggie.domain.ShoppingCart;
import com.dargon.reggie.mapper.ShoppingCartMapper;
import com.dargon.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {

    /*
     * 购物车商品数量--
     * */
    @Override
    @Transactional
    public void sub(Long userid, ShoppingCart shoppingCart) {

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userid);

        if (shoppingCart.getDishId()!=null){
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            ShoppingCart one = this.getOne(wrapper);

            if (one!=null&&one.getNumber()==1){
                this.removeById(one.getId());
            }else if (one!=null&&one.getNumber()>1){
                one.setNumber(one.getNumber()-1);
                this.updateById(one);
            }

        }else if (shoppingCart.getSetmealId()!=null){

            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart setMeal = this.getOne(wrapper);

            if (setMeal!=null&&setMeal.getNumber()==1){
                this.removeById(setMeal.getId());
            }else if (setMeal!=null&&setMeal.getNumber()>1){
                setMeal.setNumber(setMeal.getNumber()-1);
               this.updateById(setMeal);
            }

        }
    }
}
