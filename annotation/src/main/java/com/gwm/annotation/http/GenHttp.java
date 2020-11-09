package com.gwm.annotation.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 采用自动生成代码的方式做HTTP请求  该注解需要与HttpModel注解连用
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface GenHttp {
}
