package com.dargon.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dargon.reggie.domain.Employee;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
*自定义元数据处理器
*
* */
@Component
public class MyMetaObject implements MetaObjectHandler {

    /*
    *插入时自动填充
    * */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long id = BaseConText.get();
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",id);
        metaObject.setValue("updateUser",id);

    }
    /*
    * 更新时自动填充
    * */
    @Override
    public void updateFill(MetaObject metaObject) {

        Long id = BaseConText.get();
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",id);


    }


}
