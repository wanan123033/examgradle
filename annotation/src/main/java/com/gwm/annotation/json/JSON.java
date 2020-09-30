package com.gwm.annotation.json;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2018/3/28 0028.
 */
@Documented
@Target({PARAMETER, METHOD})
@Retention(RUNTIME)
public @interface JSON {
}
