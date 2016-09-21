package com.kuafu.framework.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Lenovo on 2016/3/11.
 */
@Component
public class MapperFactoryBean implements FactoryBean<Mapper>,ApplicationContextAware {

    private static final Object lock = new Object();

    private MapperFactory factory = null;

    private ApplicationContext applicationContext;

    /**
     * 构建Factory
     * @return
     */
    private ma.glasnost.orika.MapperFactory getFactory(){
        if(factory==null) {
            synchronized (lock) {
                if(factory == null) {
                    factory = new DefaultMapperFactory.Builder().mapNulls(false).build();
                    /**
                     * 自定义转换器
                     */
                    ConverterFactory converterFactory = factory.getConverterFactory();
                    converterFactory.registerConverter(new BidirectionalConverter<DateTime, Long>() {
                        @Override
                        public Long convertTo(DateTime source, Type<Long> destinationType) {
                            return source.getMillis();
                        }

                        @Override
                        public DateTime convertFrom(Long source, Type<DateTime> destinationType) {
                            return new DateTime(source);
                        }
                    });
                    converterFactory.registerConverter(new BidirectionalConverter<DateTime, DateTime>() {
                        @Override
                        public DateTime convertTo(DateTime source, Type<DateTime> destinationType) {
                            return source;
                        }

                        @Override
                        public DateTime convertFrom(DateTime source, Type<DateTime> destinationType) {
                            return source;
                        }
                    });
                }
            }
        }
        return factory;
    }

    @Override
    public Mapper getObject() throws Exception {
        return new Mapper(this.getFactory(),applicationContext);
    }

    @Override
    public Class<?> getObjectType() {
        return Mapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
