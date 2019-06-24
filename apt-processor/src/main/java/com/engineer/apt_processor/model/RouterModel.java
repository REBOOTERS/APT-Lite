package com.engineer.apt_processor.model;

import com.squareup.javapoet.ClassName;

/**
 * Created on 2019/6/24.
 *
 * @author rookie
 */
public class RouterModel {

   public final ClassName target;

    public RouterModel(ClassName target) {
        this.target = target;
    }
}
