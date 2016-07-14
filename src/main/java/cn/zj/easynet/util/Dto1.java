package cn.zj.easynet.util;

public class Dto1 implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8660710049317427861L;
    private String            name;
    private Integer           age;
    private Boolean           sex;

    public Dto1() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Dto1(String name, Integer age, Boolean sex) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
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

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Dto1 [name: " + name + ",age: " + age + ", sex:" + sex + "]\n";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Dto1 other = (Dto1) obj;
        if (age == null) {
            if (other.age != null) return false;
        } else if (!age.equals(other.age)) return false;
        return true;
    }
    
    public <T> T temp(Class<T> cls) throws InstantiationException, IllegalAccessException{
    	return cls.newInstance();
    }
    

}
