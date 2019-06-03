package com.engineer.apt_processor.creator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author: zhuyongging
 * @since: 2019-05-26
 */
public class QRouterCreatorProxy {
    public static final String Q_ROUTER_CLASS_NAME = "QRouterTable";

    private HashMap<String, String> elements = new HashMap<>();
    private String mPackageName;


    public QRouterCreatorProxy(Elements elementUtils, TypeElement classElement) {
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        mPackageName = packageElement.getQualifiedName().toString();
    }

    public void putElement(String router, String targetClass) {
        elements.put(router, targetClass);
    }

    public TypeSpec genCode() {
        TypeSpec typeSpec = TypeSpec.classBuilder(Q_ROUTER_CLASS_NAME)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(genRouterTable())
                .build();
        return typeSpec;
    }

    /**
     * @return HashMap<String, String>
     */
    private MethodSpec genRouterTable() {
//        ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(String.class));
//        ParameterSpec parameterSpec = ParameterSpec.builder(type, "string").build();

            /*

              ```Map<String, String>```
             */
        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(String.class)
        );

        ParameterSpec parameterSpec = ParameterSpec.builder(inputMapTypeOfGroup, "atlas").build();


        MethodSpec.Builder builder = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addParameter(parameterSpec)
                .returns(inputMapTypeOfGroup);

        builder.addCode("\n");
        builder.addStatement("$T<$T,$T> tables=new $T()", HashMap.class, String.class, String.class, HashMap.class);
        builder.addCode("\n");

        for (String key : elements.keySet()) {
            String target = elements.get(key);
            String code = "tables.put(\"" + key + "\",\"" + target + "\");\n";
            builder.addCode(code);
        }
        builder.addCode("return tables;\n");
        return builder.build();
    }

    public String getPackageName() {
        return mPackageName;
    }


}
