package com.kuafu.framework.orika;

import com.kuafu.framework.orika.bean.ConvertBean;
import com.kuafu.framework.orika.bean.FieldBean;
import com.kuafu.framework.orika.bean.ParentBean;
import com.kuafu.framework.orika.bean.SimpleBean;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by Lenovo on 2016/3/2.
 */
public class OrikaMapperTest extends OrikaTestBase {


    @Resource
    Mapper mapper;

    @Test
    public void simpleMapperTest(){

        SimpleBean aBean = new SimpleBean();
        aBean.setName("Test");

        SimpleBean bBean = this.mapper.map(aBean,SimpleBean.class);

        Assert.isTrue("Test".equals(bBean.getName()));
    }

    @Test
    public void fieldMapperTest(){

        FieldBean aBean = new FieldBean();
        aBean.setName("Test");
        aBean.setAge(20);

        SimpleBean bBean = this.mapper.map(aBean,SimpleBean.class);

        Assert.isTrue("Test".equals(bBean.getName()));

        FieldBean cBean = this.mapper.map(aBean,FieldBean.class);

        Assert.isNull(cBean.getName());
        Assert.isTrue(cBean.getAge() == 20);
    }

    @Test
    public void parentMapperTest(){
        SimpleBean aBean = new SimpleBean();
        aBean.setName("Test");

        ParentBean parentBean = new ParentBean();
        parentBean.setSubbean(aBean);

        ParentBean mappedBean = this.mapper.map(parentBean,ParentBean.class);

        Assert.notNull(mappedBean.getSubbean());
        Assert.isTrue("Test".equals(mappedBean.getSubbean().getName()));

    }

    @Test
    public void convertMapperTest(){
        ConvertBean aBean = new ConvertBean();
        aBean.setName("Test");

        ConvertBean bBean = this.mapper.map(aBean,ConvertBean.class);
        Assert.isTrue("MappedOut".equals(aBean.getName()));
        Assert.isTrue("MappedIn".equals(bBean.getName()));
    }


}
