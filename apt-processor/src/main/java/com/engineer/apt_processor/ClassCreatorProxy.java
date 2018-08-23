package com.engineer.apt_processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

class ClassCreatorProxy {
    private String mBindingClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    private Map<Integer, VariableElement> mVariableElementMap = new HashMap<>();

    public ClassCreatorProxy(Elements elementUtils, TypeElement classElement) {
        this.mTypeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(mTypeElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packageName;
        this.mBindingClassName = className + "_ViewBinding";
    }

    public void putElement(int id, VariableElement element) {
        mVariableElementMap.put(id, element);
    }

    /**
     * 创建Java代码
     * @return  code
     */
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import com.engineer.apt_library.*;\n");
        builder.append('\n');
        builder.append("public class ").append(mBindingClassName);
        builder.append(" {\n");

        generateMethods(builder);

        builder.append('\n');
        builder.append("}\n");
        return builder.toString();
    }

    /**
     * 加入Method
     * @param builder StringBuilder
     */
    private void generateMethods(StringBuilder builder) {
        builder.append("public void bind(").append(mTypeElement.getQualifiedName()).append(" host ) {\n");
        for (int id : mVariableElementMap.keySet()) {
            VariableElement element = mVariableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append("host.").append(name).append(" = ");
            builder.append("(").append(type).append(")(((android.app.Activity)host).findViewById( ").append(id).append("));\n");
        }
        builder.append("  }\n");
    }

    public String getProxyClassFullName()
    {
        return mPackageName + "." + mBindingClassName;
    }

    public TypeElement getTypeElement()
    {
        return mTypeElement;
    }
}
