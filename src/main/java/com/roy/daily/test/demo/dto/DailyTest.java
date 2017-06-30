package com.roy.daily.test.demo.dto;

import cn.zj.easynet.util.ThreadLocalUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 2017/6/3.
 */
public class DailyTest {

    public static void testGeric() {
        ObjectDto obj = new ObjectDto();
        obj.printGericType();
    }

    public static void test1() throws Exception {
        Person p0 = new Person(0, "roy", 500, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1997-03-28 20:00:01"));

        Person p1 = new Person(1, "roy1", 200, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-03-28 20:00:01"));
        p1.setFather(p0);
        Person p2 = new Person();
        BeanUtils.copyProperties(p1, p2);

        p2.setAge(300);
        p2.getFather().setAge(499);

        System.out.println(p1);
        System.out.println(p2);
    }

    //test threadLocal
    public static void test3(){
        HashMap<String , Object> localMap =  ThreadLocalUtil.THREAD_LOCAL.get();
        System.out.println(localMap.size());
    }

    // check
    public static void test4(){
        ArrayList al=new ArrayList();
        al.add("1");
        al.add("2");
        al.add("3");
        al.add("4");
        al.add("xxx");
        al.add("n-1");
        al.add("n");
        for(Object o:al){
            if("n".equals(o)){
                al.remove(o);
            }
        }
    }

    public static void test2() {
        Integer d1 = Integer.valueOf(103);
        Integer d2 = Integer.valueOf(103);
        System.out.println(d1 == d2);

        Integer d3 = Integer.valueOf(303);
        Integer d4 = Integer.valueOf(303);
        System.out.println(d3 == d4);


        System.out.println(Double.valueOf(3000.2) > Double.valueOf(2000.2));
    }

    public static void main(String[] args) throws Exception {
//        test1();
//        testGeric();
//        test2();
//        test3();

        test4();
    }
}
