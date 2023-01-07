package com.dargon.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dargon.reggie.domain.*;
import com.dargon.reggie.dto.OrdersDto;
import com.dargon.reggie.exception.CustomException;
import com.dargon.reggie.mapper.OrdersMapper;
import com.dargon.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    /*
     *
     * 用户下单
     *
     */
    @Override
    @Transactional
    public void submit(Long userid, Orders orders) {


        //查询购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ShoppingCart::getUserId, userid);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        if (list == null || list.size() == 0) {
            throw new CustomException("购物车没有商品");
        }



        //用户的数据
        User user = userService.getById(userid);

        //地址数据
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("地址信息有误，不能下单");
        }
        long id = IdWorker.getId();//订单id

        //总金额

        AtomicInteger num = new AtomicInteger(0);
        //订单明细的数据
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (ShoppingCart shoppingCart :list) {

            num.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());

            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrderId(id);
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetailList.add(orderDetail);


        }


        //订单表中插入数据
        orders.setNumber(String.valueOf(id));//订单id
        orders.setOrderTime(LocalDateTime.now());//时间
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(num.get())); //总金额
        orders.setStatus(2);//状态  待派送
        orders.setUserId(userid);//用户id
        orders.setUserName(user.getName());//用户名
        orders.setPhone(addressBook.getPhone());//插入电话
        orders.setConsignee(addressBook.getConsignee());//收货人姓名
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                          + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                          + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                            + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));//插入地址信息
         //订单表插入数据
        this.save(orders);


        //订单明细表中插入数据
        orderDetailService.saveBatch(orderDetailList);

        //清空购物车数据
        shoppingCartService.remove(wrapper);

    }


    /*

    * 订单分页查询
    * status 0表视用户 ，1表视管理员
    */
     public Page pageInfoDetail(int page,int pageSize,Long userId){
         Page<Orders> pageInfo = new Page<>(page,pageSize);
         Page<OrdersDto> ordersDtoPage = new Page<>();
         LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper();
         wrapper.eq(userId!=null,Orders::getUserId,userId);

         wrapper.orderByDesc(Orders::getCheckoutTime);

         this.page(pageInfo,wrapper);
         BeanUtils.copyProperties(pageInfo,ordersDtoPage);

         List<Orders> records = pageInfo.getRecords();
         List<OrdersDto> ordersDtoList = new ArrayList<>();
         ordersDtoPage.setRecords(ordersDtoList);
         for (Orders orders : records) {
             OrdersDto ordersDto = new OrdersDto();

             BeanUtils.copyProperties(orders,ordersDto);
             //根据订单号查询 订单所有菜品
            LambdaQueryWrapper<OrderDetail> wrapperD = new LambdaQueryWrapper<>();
            wrapperD.eq(OrderDetail::getOrderId,orders.getNumber());

             List<OrderDetail> list = orderDetailService.list(wrapperD);

             ordersDto.setOrderDetails(list);

             ordersDtoList.add(ordersDto);

         }
         pageInfo=null;

         return ordersDtoPage;
     }

}
