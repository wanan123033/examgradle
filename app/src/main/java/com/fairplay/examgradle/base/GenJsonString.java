package com.fairplay.examgradle.base;

import com.blankj.utilcode.util.GsonUtils;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GenJsonString implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(JSON.class)){
            Map<String,Object> map = new HashMap<>();
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {     //i:第几个参数的注解
                if (annotations[i].length > 0) {
                    for (int j = 0; j < annotations[i].length; j++) {  //j: 第i个参数上的第j个注解
                        if (annotations[i][j] instanceof Param) {
                            Param query = (Param) annotations[i][j];
                            String key = query.value();
                            Object value = args[i];
                            if (value instanceof String){
                                if(((String) value).startsWith("[")){
                                    value = new JSONArray(value.toString());
                                }else if (((String) value).startsWith("{")){
                                    value = new JSONObject(value.toString());
                                }
                            }
                            map.put(key,value);
                        }
                    }
                }
            }
            return GsonUtils.toJson(map);
        }else {
            throw new IllegalArgumentException("This method does not use JSON annotation");
        }
    }
}
