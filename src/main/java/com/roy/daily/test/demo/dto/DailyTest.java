package com.roy.daily.test.demo.dto;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;

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
        test2();

    }
}
