package com.kuafu.framework.orika;

/**
 * Created by Lenovo on 2016/3/14.
 */
public class CustomMapperConflictException extends RuntimeException {

    public CustomMapperConflictException(Class<?> a) {
        super(a.getName()+"映射规则冲突");
    }

    public CustomMapperConflictException(Class<?> a,Class<?> b) {
        super(a.getName()+"与"+b.getName()+"映射规则冲突");
    }

}
