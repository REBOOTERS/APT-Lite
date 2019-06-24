package com.engineer.apt_processor.creator;

import com.engineer.apt_processor.model.RouterModel;
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
 * @author: rookie
 * @since: 2019-05-26
 */
public class QRouterCreatorProxy {
    private static final String Q_ROUTER_CLASS_NAME = "QRouterTable";

    private HashMap<String, RouterModel> elements = new HashMap<>();
    private String mPackageName;


    public QRouterCreatorProxy(Elements elementUtils, TypeElement classElement) {
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        mPackageName = packageElement.getQualifiedName().toString();
    }

    public void putElement(String router, RouterModel targetClass) {
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
            /*
              ```Map<String, String>```
             */
        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(String.class)
        );

        MethodSpec.Builder builder = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(inputMapTypeOfGroup);

        builder.addCode("\n");
        builder.addStatement("$T<$T,$T> tables=new $T()", HashMap.class, String.class, String.class, HashMap.class);
        builder.addCode("\n");

        for (String key : elements.keySet()) {
            RouterModel target = elements.get(key);
            String code = "tables.put(\"" + key + "\",\"" + target.target + "\");\n";
            builder.addCode(code);
        }
        builder.addCode("return tables;\n");
        return builder.build();
    }

    public String getPackageName() {
        return mPackageName;
    }


}
