package com.kuafu.framework.orika.bean;

import com.kuafu.framework.orika.annotation.FieldMapping;

/**
 * Created by yangjiayong on 2016/9/14.
 */
public class FieldBean {

    private Integer age;

    @FieldMapping(mappedIn = false)
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
