package com.autonavi.poi.core;

import com.autonavi.poi.domain.casex.TestCase;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by qingshan.wqs on 2016/11/10.
 */

@Component
public class InvokeApi {


    public  static  Object invokeApi(TestCase apiCase) throws Exception{

//        BaseHttpSender httpSender=new BaseHttpSender();
//
//        Class clazz = apiObject.getClass();
//
//        Method[] methods=clazz.getDeclaredMethods();
//        Class[] paramTypes=null;
//        Method apiMethod=null;
//        for (Method method:methods){
//            if(method.getName().equals(apiMethodName)){
//                paramTypes=method.getParameterTypes();
//                apiMethod=method;
//                break;
//            }
//        }
//        final ParameterNameDiscoverer parameterNameDiscoverer
//                = new LocalVariableTableParameterNameDiscoverer();
//        String[] paraNames = parameterNameDiscoverer.getParameterNames(apiMethod);
//
//        Object[] paras = new Object[paraData.size()];
//        int count=0;
//        try {
//            for (String key : paraNames) {
//                paras[count++] = paraData.get(key);
//            }
//        }catch (Exception e){
//            throw  new IllegalArgumentException("API参数数据与接口不符");
//        }
//        return  apiMethod.invoke(apiObject, paras);
        return  null;
    }


    public  static  Object invokeApi(Object apiObject, String apiMethodName, Map<String,Object> paraData) throws Exception{
        Class clazz = apiObject.getClass();

        Method[] methods=clazz.getDeclaredMethods();
        Class[] paramTypes=null;
        Method apiMethod=null;
        for (Method method:methods){
            if(method.getName().equals(apiMethodName)){
                paramTypes=method.getParameterTypes();
                apiMethod=method;
                break;
            }
        }
        final ParameterNameDiscoverer parameterNameDiscoverer
                = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNames = parameterNameDiscoverer.getParameterNames(apiMethod);

        Object[] paras = new Object[paraData.size()];
        int count=0;
        try {
            for (String key : paraNames) {
                paras[count++] = paraData.get(key);
            }
        }catch (Exception e){
            throw  new IllegalArgumentException("API参数数据与接口不符");
        }
        return  apiMethod.invoke(apiObject, paras);
    }
}
