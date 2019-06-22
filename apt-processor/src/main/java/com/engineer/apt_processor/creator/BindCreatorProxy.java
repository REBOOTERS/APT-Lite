package com.engineer.apt_processor.creator;

import com.engineer.apt_annotation.BindView;
import com.engineer.apt_processor.model.ResModel;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * @version V1.0
 * @author: rookie
 * @date: 2018-08-22 18:29
 */
public class BindCreatorProxy {


    public static final String VIEW_BINDING = "_Binding";
    public static final String PARAM_NAME = "target";

    private String mBindingClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    private Map<String, VariableElement> mStringElementMap = new HashMap<>();
    private Map<ResModel, VariableElement> mResModelVariableElementMap = new HashMap<>();

    public BindCreatorProxy(Elements elementUtils, TypeElement classElement) {
        this.mTypeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(mTypeElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packageName;
        this.mBindingClassName = className + VIEW_BINDING;
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
                .addModifiers(Modifier.FINAL)
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
                .addParameter(className, PARAM_NAME);

        for (ResModel model : mResModelVariableElementMap.keySet()) {
            if (model.className == BindView.class) {

                VariableElement element = mResModelVariableElementMap.get(model);
                String name = element.getSimpleName().toString();
                String code = PARAM_NAME + "." + name + "=" + PARAM_NAME + "." + "findViewById(" + model.idRes + ");\n";
                builder.addCode(code);
            }
        }


        return builder.build();
    }

    private MethodSpec generateStringBindMethod() {
        ClassName className = ClassName.bestGuess(mTypeElement.getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bindString")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(className, PARAM_NAME);

        for (String string : mStringElementMap.keySet()) {

            VariableElement element = mStringElementMap.get(string);
            String name = element.getSimpleName().toString();
            builder.addCode(PARAM_NAME + "." + name + ".setText(" + "\"" + string + "\"" + ");\n\n");
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
