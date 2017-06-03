package com.roy.daily.test.demo.dto;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by apple on 2017/6/3.
 */
public class GericObjectDto<T> {
    private T filed1;

    public void setFiled1( T a){
        this.filed1 = a;
    }

    public void  printGericType()  {
        System.out.println(getClass());
        System.out.println(getClass().getTypeParameters());
        Type type = getClass().getGenericSuperclass();
        System.out.println(type);
        Class cls = (Class) ((ParameterizedType)type).getActualTypeArguments()[0];
        System.out.println();
    }


    public static void main(String[] args) {
        GericObjectDto<String> kk = new GericObjectDto<String>();
        kk.setFiled1("123");

        System.out.println(kk.getClass().getTypeParameters());
    }

}


