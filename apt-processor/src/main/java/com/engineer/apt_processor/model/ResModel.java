package com.engineer.apt_processor.model;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.AnnotatedType;

/**
 * @author: zhuyongging
 * @since: 2019-05-19
 */
public class ResModel {

    public AnnotatedType mAnnotatedType;
    public String idRes;
    public int id;


    public ResModel(String idRes, int id, AnnotatedType annotatedType) {
        this.idRes = idRes;
        this.id = id;
        this.mAnnotatedType = annotatedType;
    }
}
