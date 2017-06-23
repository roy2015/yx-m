package com.roy.daily.test.demo.dto;

import cn.zj.easynet.common.service.ILiteUserService2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BG244210 on 23/06/2017.
 */
public class Person {
    private int id;
    private String name;
    private Integer age;
    private Date  birthDay;

    private Person father;

    public Person(int id, String name, Integer age, Date birthDay) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthDay = birthDay;
    }

    public Person() {
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthDay=" + birthDay +
                ", father=" + father +
                '}';
    }
}
