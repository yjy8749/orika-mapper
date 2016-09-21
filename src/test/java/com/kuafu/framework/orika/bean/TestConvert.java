package com.kuafu.framework.orika.bean;

import com.kuafu.framework.orika.CustomConverter;
import org.springframework.stereotype.Component;

/**
 * Created by yangjiayong on 2016/9/14.
 */
@Component
public class TestConvert implements CustomConverter<ConvertBean,ConvertBean>{

    public TestConvert() {
        System.out.println();
        System.out.println("Convert 实例化");
        System.out.println();
    }

    @Override
    public void mappedIn(ConvertBean target, ConvertBean self) {
        self.setName("MappedIn");
    }

    @Override
    public void mappedOut(ConvertBean self, ConvertBean target) {
        self.setName("MappedOut");
    }

}