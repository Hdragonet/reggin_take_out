package com.dargon.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dargon.reggie.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
