package com.kuafu.framework.orika;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.kuafu.framework.orika.annotation.ClassMapping;
import com.kuafu.framework.orika.annotation.Converter;
import com.kuafu.framework.orika.annotation.FieldMapping;
import com.kuafu.framework.orika.annotation.Mapping;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lenovo on 2016/3/12.
 */
public class MappingBuilder {

    private MapperFactory mapperFactory;

    private Type<?> sourceType;

    private Type<?> destinationType;

    private Class<?> sourceClass;

    private Class<?> destinationClass;

    private Map<String,MappingBean> sourceFieldMap = new HashMap<String,MappingBean>();

    private Map<String,MappingBean> destinationFieldMap = new HashMap<String,MappingBean>();

    private Set<String> sourceFields = new HashSet<String>();

    private Set<String> destinationFields = new HashSet<String>();

    private Set<String> exclude = new HashSet<String>();
    private Map<String,Set<String>> fieldMap = new HashMap<String, Set<String>>();
    private Map<String,Set<String>> fieldAToB = new HashMap<String, Set<String>>();
    private Map<String,Set<String>> fieldBToA = new HashMap<String, Set<String>>();

    private Converter sourceConverter = null;
    private Converter destinationConverter = null;

    private ApplicationContext applicationContext;

    public MappingBuilder(MapperFactory mapperFactory, Type<?> sourceType, Type<?> destinationType, ApplicationContext applicationContext) {
        this.sourceType = sourceType;
        this.destinationType = destinationType;
        this.mapperFactory = mapperFactory;
        this.sourceClass = this.sourceType.getRawType();
        this.destinationClass = this.destinationType.getRawType();
        this.buildMapping();
        this.buildConverts();
        this.applicationContext = applicationContext;
    }

    private void buildConverts(){

        Converter sourceConverter = this.getConverter(this.sourceClass,this.destinationClass);
        Converter destinationConverter = this.getConverter(this.destinationClass,this.sourceClass);

        if(sourceConverter!=null&&destinationConverter!=null){
            if(sourceConverter == destinationConverter){
                this.sourceConverter = sourceConverter;
                this.destinationConverter = destinationConverter;
            } else if(sourceConverter.master() == destinationConverter.master()){
                throw new CustomMapperConflictException(this.sourceClass,this.destinationClass);
            }else if(sourceConverter.master()){
                this.sourceConverter = sourceConverter;
            }else if(destinationConverter.master()){
                this.destinationConverter = destinationConverter;
            }else {
                this.sourceConverter = null;
                this.destinationConverter = null;
            }
        }else if(sourceConverter != null){
            this.sourceConverter = sourceConverter;
        }else if(destinationConverter !=null){
            this.destinationConverter = destinationConverter;
        }else{
            this.sourceConverter = null;
            this.destinationConverter = null;
        }
    }

    private Converter getConverter(Class<?> c,Class<?> destinationClass){
        Converter converter = c.getAnnotation(Converter.class);
        if(converter!=null&&this.isEffect(converter,destinationClass)){
            return converter;
        }
        converter = null;
        ClassMapping classMapping = c.getAnnotation(ClassMapping.class);
        if(classMapping !=null){
            for(Converter converter1:classMapping.value()){
                if(this.isEffect(converter1,this.destinationClass)) {
                    if(converter == null){
                        converter = converter1;
                    }else{
                        throw new CustomMapperConflictException(c);
                    }
                }
            }
        }
        return converter;
    }

    public void build() {

        ClassMapBuilder<?, ?> mapper = this.mapperFactory.classMap(this.sourceClass, this.destinationClass);

        for (String field : exclude) {
            mapper.exclude(field);
        }
        for (String field : fieldAToB.keySet()) {
            for (String mappedField : fieldAToB.get(field)) {
                mapper.fieldAToB(field, mappedField);
            }
        }
        for (String field : fieldBToA.keySet()) {
            for (String mappedField : fieldBToA.get(field)) {
                mapper.fieldBToA(field, mappedField);
            }
        }
        for (String field : fieldMap.keySet()) {
            for (String mappedField : fieldMap.get(field)) {
                mapper.field(field, mappedField);
            }
        }
        if(this.sourceConverter!=null||this.destinationConverter!=null) {
            mapper.customize(new ma.glasnost.orika.CustomMapper() {
                @Override
                public void mapAtoB(Object source, Object destination, MappingContext context) {
                    //映射Source To Destination
                    try {
                        if (sourceConverter != null) {
                            //SourceClass上的自定义转换器
                            for (Class<? extends CustomConverter> converterClass : sourceConverter.value()) {
                                CustomConverter converter = sourceConverter.springIoc()?applicationContext.getBean(converterClass):converterClass.newInstance();
                                converter.mappedOut(source, destination);
                            }
                        }
                        if (destinationConverter != null) {
                            //DestinationClass上的自定义转换器
                            for (Class<? extends CustomConverter> converterClass : destinationConverter.value()) {
                                CustomConverter converter = sourceConverter.springIoc()?applicationContext.getBean(converterClass):converterClass.newInstance();
                                converter.mappedIn(source, destination);
                            }
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void mapBtoA(Object destination, Object source, MappingContext context) {
                    // 映射Destination To Source
                    try {
                        if (sourceConverter != null) {
                            //SourceClass上的自定义转换器
                            for (Class<? extends CustomConverter> converterClass : sourceConverter.value()) {
                                CustomConverter converter = converterClass.newInstance();
                                converter.mappedIn(destination, source);
                            }
                        }
                        if (destinationConverter != null) {
                            //DestinationClass上的自定义转换器
                            for (Class<? extends CustomConverter> converterClass : destinationConverter.value()) {
                                CustomConverter converter = converterClass.newInstance();
                                converter.mappedOut(destination, source);
                            }
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        mapper.byDefault().register();
    }

    private void buildMapping(){

        this.buildFieldMap(this.sourceFieldMap,this.sourceFields,this.sourceClass,this.destinationClass);
        this.buildFieldMap(this.destinationFieldMap,this.destinationFields,this.destinationClass,this.sourceClass);

        MappingBean mappingBean = null;
        //源类映射目标类配置
        for(String field:this.sourceFieldMap.keySet()) {
            //源字段映射配置
            MappingBean sourceField = this.sourceFieldMap.get(field);
            //映射字段在目标类中若不存在则不添加映射配置
            if (this.destinationFields.contains(sourceField.getMappedField())) {
                mappingBean = new MappingBean();//映射配置
                mappingBean.setSourceField(sourceField.getSourceField());//源字段
                mappingBean.setMappedField(sourceField.getMappedField());//映射字段
                //获取映射字段在目标类中的映射配置
                MappingBean destinationField = this.destinationFieldMap.get(sourceField.getMappedField());
                //映射字段在目标类中的没有映射配置着使用源字段映射配置
                if(destinationField != null){//有配置映射关系
                    //任意一方设置ignore后均设置为ignore
                    mappingBean.setIgnore(sourceField.isIgnore()||destinationField.isIgnore());
                    //源字段允许映射出去并且映射字段允许被映射 mappedOut:源->目标
                    mappingBean.setMappedOut(sourceField.isMappedOut()&&destinationField.isMappedIn());
                    //源字段允许被映射并且映射字段允许映射出去,映射字段名称相同 mappedIn:目标->源
                    mappingBean.setMappedIn(
                            sourceField.isMappedIn()&&destinationField.isMappedOut()
                            &&sourceField.getSourceField().equals(destinationField.getMappedField())
                    );
                }else{//没有配置映射关系,使用源类映射关系
                    mappingBean.setIgnore(sourceField.isIgnore());
                    mappingBean.setMappedIn(sourceField.isMappedIn());
                    mappingBean.setMappedOut(sourceField.isMappedOut());
                }
                //添加映射关系
                this.addMapping(mappingBean);
            }
        }

        //目标类映射源类配置
        for(String field:this.destinationFieldMap.keySet()) {
            //映射字段映射配置
            MappingBean destinationField = this.destinationFieldMap.get(field);
            //若映射字段在源类中不存在则不添加映射配置
            if (this.sourceFields.contains(destinationField.getMappedField())) {
                mappingBean = new MappingBean();//源类到映射类的映射配置
                mappingBean.setSourceField(destinationField.getMappedField());//源类中的源字段
                mappingBean.setMappedField(destinationField.getSourceField());//目标类中的映射字段
                //映射字段在源类中的映射配置
                MappingBean sourceField = this.sourceFieldMap.get(destinationField.getMappedField());
                if(sourceField != null){//有配置映射关系
                    //任意一方设置ignore后均设置为ignore
                    mappingBean.setIgnore(sourceField.isIgnore()||destinationField.isIgnore());
                    ///若源映射配置允许被映射并且映射字段允许映射出去 mappedOut:源->目标
                    mappingBean.setMappedOut(sourceField.isMappedOut()&&destinationField.isMappedIn());
                    //若源映射配置允许映射出去并且映射字段允许被映射，映射字段名称相同  mappedIn:目标->源
                    mappingBean.setMappedIn(
                            destinationField.isMappedOut()&&sourceField.isMappedIn()
                            &&sourceField.getMappedField().equals(destinationField.getSourceField())
                    );
                }else{//没有配置映射关系，使用目标类映射关系
                    mappingBean.setIgnore(destinationField.isIgnore());
                    //映射字段允许映射出去  mappedIn:目标->源
                    mappingBean.setMappedIn(destinationField.isMappedOut());
                    //映射字段允许被映射 mappedOut:源->目标
                    mappingBean.setMappedOut(destinationField.isMappedIn());
                }
                this.addMapping(mappingBean);
            }
        }
    }

    public void addMapping(MappingBean bean){
        if(bean.isIgnore()){
            this.exclude.add(bean.getSourceField());
        }else{
            if(bean.isMappedIn()&&bean.isMappedOut()){
                if(!bean.getSourceField().equals(bean.getMappedField())){
                    this.addToMap(bean.getSourceField(),bean.getMappedField(),this.fieldMap);
                }
            }else if(bean.isMappedOut()){
                this.addToMap(bean.getSourceField(),bean.getMappedField(),this.fieldAToB);
            }else if(bean.isMappedIn()){
                this.addToMap(bean.getSourceField(),bean.getMappedField(),this.fieldBToA);
            }else{
                this.exclude.add(bean.getSourceField());
            }
        }
    }

    public void addToMap(String key,String value,Map<String,Set<String>> map){
        if(map.containsKey(key)){
            map.get(key).add(value);
        }else{
            Set<String> set = new HashSet<String>();
            set.add(value);
            map.put(key,set);
        }
    }

    private void buildFieldMap(Map<String, MappingBean> fieldMap, Set<String> fields, Class<?> sourceClass, Class<?> destinationClass){
        for(Field field: FieldUtils.getAllFields(sourceClass)){
            fields.add(field.getName());
            Optional<FieldMapping> optional = this.getFieldMapping(field, this.destinationClass);
            if(optional.isPresent()){
                FieldMapping fieldMapping = optional.get();
                MappingBean bean = new MappingBean();
                bean.setSourceField(field.getName());
                bean.setMappedField(Strings.isNullOrEmpty(fieldMapping.value())?field.getName():fieldMapping.value());
                bean.setMappedIn(fieldMapping.mappedIn());
                bean.setMappedOut(fieldMapping.mappedOut());
                bean.setIgnore(fieldMapping.ignore());
                fieldMap.put(field.getName(),bean);
            }
        }
    }

    private Optional<FieldMapping> getFieldMapping(Field field, Class<?> c){
        FieldMapping fieldMaping = field.getAnnotation(FieldMapping.class);
        if(fieldMaping!=null&&this.isEffect(fieldMaping,c)){
            return Optional.of(fieldMaping);
        }
        Mapping mapping = field.getAnnotation(Mapping.class);
        if (mapping != null) {
            for (FieldMapping value : mapping.value()) {
                if (this.isEffect(value, c)) {
                    return Optional.of(value);
                }
            }
        }
        return Optional.absent();
    }

    private boolean isEffect(FieldMapping fieldMapping,Class<?> c){
        return (fieldMapping.effectClass().length ==0 || ArrayUtils.contains(fieldMapping.effectClass(),c));
    }

    private boolean isEffect(Converter converter,Class<?> c){
        return converter.effectClass().equals(c);
    }

    private static class MappingBean{

        private String sourceField;

        private String mappedField;

        private boolean mappedIn;

        private boolean mappedOut;

        private boolean ignore;

        public String getSourceField() {
            return sourceField;
        }

        public void setSourceField(String sourceField) {
            this.sourceField = sourceField;
        }

        public String getMappedField() {
            return mappedField;
        }

        public void setMappedField(String mappedField) {
            this.mappedField = mappedField;
        }

        public boolean isMappedIn() {
            return mappedIn;
        }

        public void setMappedIn(boolean mappedIn) {
            this.mappedIn = mappedIn;
        }

        public boolean isMappedOut() {
            return mappedOut;
        }

        public void setMappedOut(boolean mappedOut) {
            this.mappedOut = mappedOut;
        }

        public boolean isIgnore() {
            return ignore;
        }

        public void setIgnore(boolean ignore) {
            this.ignore = ignore;
        }
    }

}
