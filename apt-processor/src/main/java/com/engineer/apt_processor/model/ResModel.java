package com.engineer.apt_processor.model;

import java.lang.annotation.Annotation;

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
