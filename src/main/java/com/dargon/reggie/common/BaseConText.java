package com.dargon.reggie.common;


/*
* 用于保存当前的登录的用户id
* */
public class BaseConText {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void set(Long id){
        threadLocal.set(id);
    }

    public static Long get(){

        return threadLocal.get();
    }
}
