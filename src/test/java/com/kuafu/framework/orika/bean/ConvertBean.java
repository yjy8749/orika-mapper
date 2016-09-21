package com.kuafu.framework.orika.bean;

import com.kuafu.framework.orika.annotation.ClassMapping;
import com.kuafu.framework.orika.annotation.Converter;

/**
 * Created by yangjiayong on 2016/9/14.
 */
@ClassMapping(
        @Converter(value = TestConvert.class,effectClass = ConvertBean.class,springIoc = true)
)
public class ConvertBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
