package com.kuafu.framework.orika.annotation;

import com.kuafu.framework.orika.CustomConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lenovo on 2016/3/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Converter {

    /**
     * 自定义类转换器
     * @return
     */
    Class<? extends CustomConverter>[] value() default {};

    /**
     * 生效类型
     * @return
     */
    Class<?> effectClass();

    /**
     * 冲突时是否作为主转换器
     * @return
     */
    boolean master() default true;

    /**
     * 是否根据类型从Spring上下文查找
     */
    boolean springIoc() default false;

}
