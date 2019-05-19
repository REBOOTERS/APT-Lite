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
        People people = new People();
        someTest();
        genSelfCode();
    }

    private static void someTest() {
        System.out.println("String.getCanonicalName==" + String.class.getCanonicalName());
        System.out.println("ArrayList.getCanonicalName==" + ArrayList.class.getCanonicalName());
        System.out.println("People.getCanonicalName==" + People.class.getCanonicalName());
        System.out.println("MainClass.getCanonicalName==" + MainClass.class.getCanonicalName());
        System.out.println("");
        System.out.println("用 java poet 输出当前类文件");
        System.out.println("");

    }

    private static void genSelfCode() {

        MethodSpec methodSpec = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T people=new $T",People.class,People.class)
                .addStatement("someTest()")
                .addStatement("genSelfCode()")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("MainClass")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.engineer.poet", typeSpec)
                .build();

        File outputFile = new File("poet/build/");

        try {
            javaFile.writeTo(outputFile);
            javaFile.writeTo(System.err);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
