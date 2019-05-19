package com.engineer.apt_processor;

import com.engineer.apt_annotation.BindString;
import com.engineer.apt_annotation.BindView;
import com.engineer.apt_processor.model.ResModel;
import com.google.auto.service.AutoService;
import com.hendraanggrian.RParser;
import com.squareup.javapoet.JavaFile;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class BindViewProcessor extends BaseProcessor {


    private Map<String, ClassCreatorProxyPro> mProxyProMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new HashSet<>();
        supportTypes.add(BindView.class.getCanonicalName());
        supportTypes.add(BindString.class.getCanonicalName());
        return supportTypes;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "processing....");

        mProxyProMap.clear();
        assembleAnnotations(roundEnvironment);
        genCodeAuto();


        mMessager.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
        return true;
    }

    private void assembleAnnotations(RoundEnvironment roundEnvironment) {
        processAnnotations(roundEnvironment, BindView.class);
        processAnnotations(roundEnvironment, BindString.class);
    }


    private void processAnnotations(RoundEnvironment roundEnvironment, Class<? extends Annotation> className) {

        RParser parser = RParser.builder(processingEnv)
                .setSupportedAnnotations(BindView.class)
                .setSupportedTypes("id")
                .build();

        parser.scan(roundEnvironment);

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(className);

        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            ClassCreatorProxyPro proxy = mProxyProMap.get(fullClassName);
            if (proxy == null) {
                proxy = new ClassCreatorProxyPro(mElementUtils, classElement);
                mProxyProMap.put(fullClassName, proxy);
            }

            if (className.getCanonicalName().equals(BindView.class.getCanonicalName())) {
                BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
                int id = bindAnnotation.value();

                String idRes = null;
                String packageName = null;

                for (String path : fullClassName.split("\\.")) {
                    if (packageName == null) {
                        packageName = path;
                    } else {
                        packageName = packageName + "." + path;
                    }

                    idRes = parser.parse(packageName, id);
                    if (!idRes.equals(String.valueOf(id))) {
                        break;
                    }
                }
                ResModel model = new ResModel(idRes, id, BindView.class);
                proxy.putElement(model, variableElement);

            } else if (className.getCanonicalName().equals(BindString.class.getCanonicalName())) {
                BindString bindAnnotation = variableElement.getAnnotation(BindString.class);
                String str = bindAnnotation.value();
                proxy.putElement(str, variableElement);
            }
        }
    }

    private void genCodeAuto() {
        for (String key : mProxyProMap.keySet()) {
            ClassCreatorProxyPro proxyPro = mProxyProMap.get(key);
            try {
                JavaFile javaFile = JavaFile.builder(proxyPro.getPackageName(), proxyPro.generatorJavaCode()).build();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "---> create file start\n" + javaFile.toString());
                javaFile.writeTo(mFiler);
                mMessager.printMessage(Diagnostic.Kind.NOTE, "---> create file finish\n");
            } catch (Exception e) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxyPro.getProxyClassFullName() + "error");
            }
        }

    }
}
