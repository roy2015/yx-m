package cn.zj.easynet.util;

/**
 * 类Dto2.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2016年3月9日 下午3:24:21
 */
public class Dto2 {

    private String name;
    private String age;
    private String sex;
    private String phone;

    public Dto2() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Dto2 [name=" + name + ", age=" + age + ", sex=" + sex + ", phone=" + phone + "]";
    }

}
