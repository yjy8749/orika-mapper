package com.kuafu.framework.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingStrategy;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.*;

/**
 * Created by Lenovo on 2016/3/11.
 */
public class Mapper implements MapperFacade {

    private MapperFacade mapper;
    private MapperFactory mapperFactory;
    private Object mapperBuildLock = new Object();

    public Mapper(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
        this.mapper = this.mapperFactory.getMapperFacade();
    }

    private void addMappingIfNotRegistered(Class<?> sourceClass, Class<?> destinationClass) {
        addMappingIfNotRegistered(TypeFactory.valueOf(sourceClass),TypeFactory.valueOf(destinationClass));
    }

    private void addMappingIfNotRegistered(Type<?> sourceType, Type<?> destinationType){

        if(!this.mapperFactory.existsRegisteredMapper(sourceType,destinationType,true)){
            synchronized (mapperBuildLock){
                if(!this.mapperFactory.existsRegisteredMapper(sourceType,destinationType,true)) {
                    new MappingBuilder(this.mapperFactory, sourceType, destinationType).build();
                }
            }
        }

    }

    private <D> Class<D> getElementClass(D[] arrays){
        Class<D> dClass = null;
        if(arrays!=null&&arrays.length>0){
            D el = arrays[0];
            dClass = (Class<D>) el.getClass();
        }
        return dClass;
    }

    private <D> Class<D> getElementClass(Iterable<D> iterable){
        Class<D> dClass = null;
        Iterator<D> iterator = iterable!=null?iterable.iterator():null;
        if(iterator!=null&&iterator.hasNext()){
            D el = iterator.next();
            dClass = (Class<D>) el.getClass();
        }
        return dClass;
    }

    @Override
    public <S, D> D map(S sourceObject, Class<D> destinationClass) {
        if(sourceObject==null){
            return null;
        }
        addMappingIfNotRegistered(sourceObject.getClass(),destinationClass);
        return this.mapper.map(sourceObject,destinationClass);
    }

    @Override
    public <S, D> D map(S sourceObject, Class<D> destinationClass, MappingContext context) {
        if(sourceObject==null){
            return null;
        }addMappingIfNotRegistered(sourceObject.getClass(),destinationClass);
        return this.mapper.map(sourceObject,destinationClass,context);
    }

    @Override
    public <S, D> void map(S sourceObject, D destinationObject) {
        if(sourceObject==null){
            return;
        }
        addMappingIfNotRegistered(sourceObject.getClass(),destinationObject.getClass());
        this.mapper.map(sourceObject,destinationObject);
    }

    @Override
    public <S, D> void map(S sourceObject, D destinationObject, MappingContext context) {
        if(sourceObject==null){
            return;
        }
        addMappingIfNotRegistered(sourceObject.getClass(),destinationObject.getClass());
        this.mapper.map(sourceObject,destinationObject,context);
    }

    @Override
    public <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        if(sourceObject==null){
            return;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        this.mapper.map(sourceObject,destinationObject,sourceType,destinationType);
    }

    @Override
    public <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        if(sourceObject==null){
            return;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        this.mapper.map(sourceObject,destinationObject,sourceType,destinationType,context);
    }

    @Override
    public <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsSet(source,destinationClass);
    }

    @Override
    public <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsSet(source,destinationClass,context);
    }

    @Override
    public <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsSet(source,destinationClass);
    }

    @Override
    public <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsSet(source,destinationClass,context);
    }

    @Override
    public <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsList(source,destinationClass);
    }

    @Override
    public <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsList(source,destinationClass,context);
    }

    @Override
    public <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsList(source,destinationClass);
    }

    @Override
    public <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>(0);
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsList(source,destinationClass,context);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsArray(destination,source,destinationClass);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsArray(destination,source,destinationClass);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsArray(destination,source,destinationClass,context);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        return this.mapper.mapAsArray(destination,source,destinationClass,context);
    }

    @Override
    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        this.mapper.mapAsCollection(source,destination,destinationClass);
    }

    @Override
    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        this.mapper.mapAsCollection(source,destination,destinationClass,context);
    }

    @Override
    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        this.mapper.mapAsCollection(source,destination,destinationClass);
    }

    @Override
    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(elclass),TypeFactory.valueOf(destinationClass));
        this.mapper.mapAsCollection(source,destination,destinationClass,context);
    }

    @Override
    public <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType) {
        if(sourceObject==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.map(sourceObject,sourceType,destinationType);
    }

    @Override
    public <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        if(sourceObject==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.map(sourceObject,sourceType,destinationType,context);
    }

    @Override
    public <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsSet(source,sourceType,destinationType);
    }

    @Override
    public <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsSet(source,sourceType,destinationType,context);
    }

    @Override
    public <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsSet(source,sourceType,destinationType);
    }

    @Override
    public <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new HashSet<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsSet(source,sourceType,destinationType);
    }

    @Override
    public <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsList(source,sourceType,destinationType);
    }

    @Override
    public <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsList(source,sourceType,destinationType,context);
    }

    @Override
    public <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsList(source,sourceType,destinationType);
    }

    @Override
    public <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return new ArrayList<D>();
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsList(source,sourceType,destinationType,context);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsArray(destination,source,sourceType,destinationType);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsArray(destination,source,sourceType,destinationType);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsArray(destination,source,sourceType,destinationType,context);
    }

    @Override
    public <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return (D[]) new Object[1];
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsArray(destination,source,sourceType,destinationType,context);
    }

    @Override
    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        this.mapper.mapAsCollection(source,destination,sourceType,destinationType);
    }

    @Override
    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }addMappingIfNotRegistered(sourceType,destinationType);
        this.mapper.mapAsCollection(source,destination,sourceType,destinationType,context);
    }

    @Override
    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        }addMappingIfNotRegistered(sourceType,destinationType);
        this.mapper.mapAsCollection(source,destination,sourceType,destinationType);
    }

    @Override
    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        Class<S> elclass = this.getElementClass(source);
        if(elclass==null){
            return;
        } addMappingIfNotRegistered(sourceType,destinationType);
        this.mapper.mapAsCollection(source,destination,sourceType,destinationType,context);
    }

    @Override
    public <S, D> D convert(S source, Class<D> destinationClass, String converterId) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(source.getClass(),destinationClass);
        return this.mapper.convert(source,destinationClass,converterId);
    }

    @Override
    public <S, D> D convert(S source, Type<S> sourceType, Type<D> destinationType, String converterId) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.convert(source,sourceType,destinationType,converterId);
    }

    @Override
    public <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsMap(source,sourceType,destinationType);
    }

    @Override
    public <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsMap(source,sourceType,destinationType,context);
    }

    @Override
    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsMap(source,sourceType,destinationType);
    }

    @Override
    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsMap(source,sourceType,destinationType,context);
    }

    @Override
    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsMap(source,sourceType,destinationType);
    }

    @Override
    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsMap(source,sourceType,destinationType,context);
    }

    @Override
    public <Sk, Sv, D> List<D> mapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsList(source,sourceType,destinationType);
    }

    @Override
    public <Sk, Sv, D> List<D> mapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsList(source,sourceType,destinationType,context);
    }

    @Override
    public <Sk, Sv, D> Set<D> mapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsSet(source,sourceType,destinationType);
    }

    @Override
    public <Sk, Sv, D> Set<D> mapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsSet(source,sourceType,destinationType,context);
    }

    @Override
    public <Sk, Sv, D> D[] mapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsArray(destination,source,sourceType,destinationType);
    }

    @Override
    public <Sk, Sv, D> D[] mapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(sourceType,destinationType);
        return this.mapper.mapAsArray(destination,source,sourceType,destinationType,context);
    }

    @Override
    public <S, D> D newObject(S source, Type<? extends D> destinationType, MappingContext context) {
        if(source==null){
            return null;
        }
        addMappingIfNotRegistered(TypeFactory.valueOf(source.getClass()),destinationType);
        return this.mapper.newObject(source,destinationType,context);
    }

    @Override
    public <S, D> MappingStrategy resolveMappingStrategy(S sourceObject, java.lang.reflect.Type sourceType, java.lang.reflect.Type destinationType, boolean mapInPlace, MappingContext context) {
        return this.mapper.resolveMappingStrategy(sourceObject,sourceType,destinationType,mapInPlace,context);
    }

    @Override
    public void factoryModified(MapperFactory factory) {
        this.factoryModified(factory);
    }

}
