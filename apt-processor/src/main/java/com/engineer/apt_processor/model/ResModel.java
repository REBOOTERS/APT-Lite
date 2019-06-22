package com.engineer.apt_processor.model;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

/**
 * @author: rookie
 * @since: 2019-05-19
 */
public class ResModel {

    public Class<? extends Annotation> className;
    public String idRes;
    public int id;


    public ResModel(String idRes, int id, Class<? extends Annotation> className) {
        this.idRes = idRes;
        this.id = id;
        this.className = className;
    }
}
