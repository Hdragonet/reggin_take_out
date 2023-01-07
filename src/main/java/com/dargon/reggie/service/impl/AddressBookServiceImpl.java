package com.dargon.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dargon.reggie.domain.AddressBook;
import com.dargon.reggie.mapper.AddressBookMapper;
import com.dargon.reggie.service.AddressBookService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
}
