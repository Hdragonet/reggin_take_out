package com.dargon.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.Orders;

public interface OrdersService extends IService<Orders> {

    /*
    * 用户下单
    */
    public void submit(Long userid,Orders orders);

    public Page pageInfoDetail(int page,int pageSize,Long userid);
}
