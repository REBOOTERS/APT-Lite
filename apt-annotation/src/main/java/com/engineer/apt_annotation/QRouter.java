package com.engineer.apt_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhuyongging
 * @since: 2019-05-26
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface QRouter {
    String value();
}
