package com.kuafu.framework.orika.annotation;

import java.lang.annotation.*;

/**
 * Created by Lenovo on 2016/3/12.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Mapping {

    FieldMapping[] value();

}
