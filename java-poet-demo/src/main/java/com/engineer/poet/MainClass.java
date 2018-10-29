package com.engineer.poet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.lang.model.element.Modifier;

public class MainClass {

    public static void main(String[] args) {
        someTest();
//        genSelfCode();
    }

    private static void someTest() {
        System.out.println("String.getCanonicalName==" + String.class.getCanonicalName());
        System.out.println("ArrayList.getCanonicalName==" + ArrayList.class.getCanonicalName());
        System.out.println("People.getCanonicalName==" + People.class.getCanonicalName());
        System.out.println("MainClass.getCanonicalName==" + MainClass.class.getCanonicalName());
    }

    private static void genSelfCode() {
        MethodSpec methodSpec = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("genSelfCode()")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("MainClass1")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.engineer.poet", typeSpec)
                .addStaticImport(com.squareup.javapoet.JavaFile.class, "*")
                .addStaticImport(com.squareup.javapoet.MethodSpec.class, "*")
                .addStaticImport(com.squareup.javapoet.TypeSpec.class, "*")
                .addStaticImport(java.io.File.class, "*")
                .addStaticImport(java.io.IOException.class, "*")
                .addStaticImport(javax.lang.model.element.Modifier.class, "*")
                .build();

        File outputFile = new File("poet/build/");

        try {
            javaFile.writeTo(outputFile);
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
