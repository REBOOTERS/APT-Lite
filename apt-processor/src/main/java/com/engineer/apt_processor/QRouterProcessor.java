package com.engineer.apt_processor;


import com.engineer.apt_annotation.QRouter;
import com.engineer.apt_processor.creator.QRouterCreatorProxy;
import com.engineer.apt_processor.model.RouterModel;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author: rookie
 * @since: 2019-05-26
 */
@SupportedAnnotationTypes({QRouterProcessor.TYPE_Q_ROUTER})
@AutoService(Processor.class)
public class QRouterProcessor extends BaseProcessor {

    /**
     * {@link QRouter}
     */
    static final String TYPE_Q_ROUTER = "com.engineer.apt_annotation.QRouter";

    private QRouterCreatorProxy mQRouterCreatorProxy;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "processing....");


        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(QRouter.class);
        for (Element element : elements) {
            if (element instanceof TypeElement) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, "element=" + element.getSimpleName());
                mMessager.printMessage(Diagnostic.Kind.NOTE, "element=" + ((TypeElement) element).getQualifiedName().toString());
                mMessager.printMessage(Diagnostic.Kind.NOTE, "element=" + element.getEnclosingElement());

                if (mQRouterCreatorProxy == null) {
                    mQRouterCreatorProxy = new QRouterCreatorProxy(mElementUtils, (TypeElement) element);
                }

                QRouter routerAnnotation = element.getAnnotation(QRouter.class);
                mMessager.printMessage(Diagnostic.Kind.NOTE, "annotation==" + routerAnnotation.value());
                String router = routerAnnotation.value();
                String target = ((TypeElement) element).getQualifiedName().toString();
                ClassName className = ClassName.get((TypeElement) element);
                RouterModel model = new RouterModel(className);
                mQRouterCreatorProxy.putElement(router, model);

            } else {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "annotation on mistake type, the type should be class");
            }
        }

        generateRouterTable();

        return true;
    }

    private void generateRouterTable() {
        TypeSpec typeSpec = mQRouterCreatorProxy.genCode();
        String packageName = mQRouterCreatorProxy.getPackageName();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "---> create file start\n" + javaFile.toString());
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "---> create file finish\n");

    }
}
