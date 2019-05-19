package com.engineer.apt_processor;

import com.engineer.apt_annotation.BindView;
import com.engineer.apt_processor.model.ResModel;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.swing.text.View;

/**
 * @version V1.0
 * @author: zhuyongging
 * @date: 2018-08-22 18:29
 */
public class ClassCreatorProxyPro {


    public static final String VIEW_BINDING = "_Binding";

    private String mBindingClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    private Map<Integer, VariableElement> mVariableElementMap = new HashMap<>();
    private Map<String, VariableElement> mStringElementMap = new HashMap<>();
    private Map<ResModel, VariableElement> mResModelVariableElementMap = new HashMap<>();

    public ClassCreatorProxyPro(Elements elementUtils, TypeElement classElement) {
        this.mTypeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(mTypeElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packageName;
        this.mBindingClassName = className + VIEW_BINDING;
    }

    public void putElement(int id, VariableElement element) {
        mVariableElementMap.put(id, element);
    }

    public void putElement(String string, VariableElement element) {
        mStringElementMap.put(string, element);
    }

    public void putElement(ResModel string, VariableElement element) {
        mResModelVariableElementMap.put(string, element);
    }


    public TypeSpec generatorJavaCode() {
        TypeSpec typeSpec = TypeSpec.classBuilder(mBindingClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateViewBindMethod())
                .addMethod(generateStringBindMethod())
                .build();
        return typeSpec;
    }

    private MethodSpec generateViewBindMethod() {
        ClassName className = ClassName.bestGuess(mTypeElement.getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(className, "host");

        for (int id : mVariableElementMap.keySet()) {
            VariableElement element = mVariableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.addCode("host." + name + " = "
                    + "(" + type + ")(((android.app.Activity)host).findViewById( " + id + "));\n");
        }

        for (ResModel model : mResModelVariableElementMap.keySet()) {
            if (model.mAnnotatedType instanceof BindView) {
                System.out.println("--------------" + model.idRes);
            }
        }


        return builder.build();
    }

    private MethodSpec generateStringBindMethod() {
        ClassName className = ClassName.bestGuess(mTypeElement.getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bindString")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(className, "host");

        for (String string : mStringElementMap.keySet()) {
            VariableElement element = mStringElementMap.get(string);
            String name = element.getSimpleName().toString();
            builder.addCode("host." + name + ".setText(" + "\"" + string + "\"" + ");\n\n");
        }


        return builder.build();
    }


    public String getPackageName() {
        return mPackageName;
    }

    public String getProxyClassFullName() {
        return mPackageName + "." + mBindingClassName;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }
}
