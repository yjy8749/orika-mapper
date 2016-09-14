package com.kuafu.framework.orika;

/**
 * Created by Lenovo on 2016/3/14.
 */
public interface CustomConverter<Self,Target> {

    public void mappedIn(Target target, Self self);

    public void mappedOut(Self self, Target target);

}
