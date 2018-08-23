package com.engineer.apt_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version V1.0
 * @author: zhuyongging
 * @date: 2018-08-22 17:55
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface BindOnClick {
    int[] value();
}
