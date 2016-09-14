package com.kuafu.framework.orika.annotation;

import java.lang.annotation.*;

/**
 * Created by Lenovo on 2016/3/11.
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FieldMapping {

    /**
     * 映射字段名
     */
    String value() default "";

    /**
     * 映射的类，Null表示所有
     */
    Class<?>[] effectClass() default {};

    boolean mappedIn() default true;

    boolean mappedOut() default true;

    boolean ignore() default false;

}
