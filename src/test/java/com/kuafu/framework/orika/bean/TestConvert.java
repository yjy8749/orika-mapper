package com.kuafu.framework.orika.bean;

import com.kuafu.framework.orika.CustomConverter;

/**
 * Created by yangjiayong on 2016/9/14.
 */
public class TestConvert implements CustomConverter<ConvertBean,ConvertBean>{

    @Override
    public void mappedIn(ConvertBean target, ConvertBean self) {
        self.setName("MappedIn");
    }

    @Override
    public void mappedOut(ConvertBean self, ConvertBean target) {
        self.setName("MappedOut");
    }

}