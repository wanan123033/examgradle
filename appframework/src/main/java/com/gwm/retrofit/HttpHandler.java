package com.gwm.retrofit;

import com.gwm.annotation.http.FileUpload;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.Header;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.Path;
import com.gwm.annotation.http.Query;
import com.gwm.annotation.http.QueryUrl;
import com.gwm.annotation.http.RequestBody;
import com.gwm.annotation.http.Url;
import com.gwm.annotation.json.JSON;
import com.gwm.http.HttpParams;
import com.gwm.http.NetWorkParams;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/11/3.
 */
public class HttpHandler extends HttpInvocationHandler {

    public NetWorkParams addNetworkParams(Method method, Object[] args) {
        HttpParams params = new HttpParams();
        if(method.isAnnotationPresent(HTTP.class)) {
            HTTP annotation = method.getAnnotation(HTTP.class);
            params.url = annotation.url();
            params.way = annotation.way();
            params.isJson = annotation.isJson();
            params.isRequestBody = annotation.isRequestBody();
            Annotation[][] annotations = method.getParameterAnnotations();
            params.params = new HashMap();
            params.files = new HashMap();
            //添加header头解析
            Header header = method.getAnnotation(Header.class);
            if (header != null){
                String[] strings = header.value();
                if (params.headers == null){
                    params.headers = new HashMap();
                }
                for (String string : strings){
                    String keyvalue[] = string.split(":");
                    params.headers.put(keyvalue[0],keyvalue[1]);
                }
            }
            for (int i = 0; i < annotations.length; i++) {     //i:第几个参数的注解
                if (annotations[i].length > 0) {
                    for (int j = 0; j < annotations[i].length; j++) {  //j: 第i个参数上的第j个注解
                        if(annotations[i][j] instanceof Query) {
                            Query query = (Query) annotations[i][j];
                            String key = query.value();
                            Object value = args[i];
                            params.params.put(key,value);
                        }else if(annotations[i][j] instanceof FileUpload){
                            FileUpload fileUpload = (FileUpload) annotations[i][j];
                            String key = fileUpload.value();
                            File file = (File) args[i];
                            params.files.put(key,file);
                        }else if(annotations[i][j] instanceof Url){
                            if(args[i] instanceof String){
                                params.url = (String) args[i];
                            }
                        }else if (annotations[i][j] instanceof RequestBody){
                            params.body = (okhttp3.RequestBody) args[i];
                            params.isRequestBody = true;
                        }else if (annotations[i][j] instanceof JSON){
                            params.json = (String) args[i];
                            params.isJson = true;
                        }else if (annotations[i][j] instanceof QueryUrl){
                            String key = ((QueryUrl)annotations[i][j]).value();
                            Object value = args[i];
                            if (params.url != null){
                                if (params.url.contains("?")){
                                    params.url += ("&&" + key + "=" + value);
                                }else {
                                    params.url += ("?" + key + "=" + value);
                                }
                            }
                        }else if (annotations[i][j] instanceof Path){
                            String value = ((Path)annotations[i][j]).value();
                            params.url = params.url.replaceAll("\\{"+value+"\\}", args[i]+"");
                        }else if (annotations[i][j] instanceof HeaderString){
                            String key = ((HeaderString)annotations[i][j]).value();
                            String value = (String) args[i];
                            if (params.headers == null){
                                params.headers = new HashMap<>();
                            }
                            params.headers.put(key,value);
                        }
                    }
                }
            }
        }else {
            throw new IllegalArgumentException("Unknown method annotation");
        }
        return params;
    }

}
