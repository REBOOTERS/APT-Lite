package com.engineer.apt_processor;

import com.engineer.apt_annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementUtils;
    private Map<String, ClassCreatorProxy> mProxyMap = new HashMap<>();
    private Map<String, ClassCreatorProxyPro> mProxyProMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new HashSet<>();
        supportTypes.add(BindView.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager=processingEnvironment.getMessager();
        mElementUtils=processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE,"processing....");
        mProxyMap.clear();


//        genCodeManu(roundEnvironment);

        genCodeAuto(roundEnvironment);

        return true;
    }

    private void genCodeAuto(RoundEnvironment roundEnvironment) {


        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            VariableElement variableElement= (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName=classElement.getQualifiedName().toString();
            ClassCreatorProxyPro proxy = mProxyProMap.get(fullClassName);
            if (proxy == null) {
                proxy = new ClassCreatorProxyPro(mElementUtils, classElement);
                mProxyProMap.put(fullClassName, proxy);
            }

            BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
            int id=bindAnnotation.value();
            proxy.putElement(id,variableElement);
        }


        for (String key : mProxyProMap.keySet()) {
            ClassCreatorProxyPro proxyPro = mProxyProMap.get(key);
            try {
                JavaFile javaFile = JavaFile.builder(proxyPro.getPackageName(),proxyPro.generatorJavaCode()).build();
                javaFile.writeTo(processingEnv.getFiler());
            } catch (Exception e) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxyPro.getProxyClassFullName() + "error");
            }
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
    }


    private void genCodeManu(RoundEnvironment roundEnvironment) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            VariableElement variableElement= (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName=classElement.getQualifiedName().toString();
            ClassCreatorProxy proxy = mProxyMap.get(fullClassName);
            if (proxy == null) {
                proxy = new ClassCreatorProxy(mElementUtils, classElement);
                mProxyMap.put(fullClassName, proxy);
            }

            BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
            int id=bindAnnotation.value();
            proxy.putElement(id,variableElement);
        }



        //通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            ClassCreatorProxy proxyInfo = mProxyMap.get(key);
            try {
                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxyInfo.getProxyClassFullName());
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxyInfo.getProxyClassFullName() + "error");
            }
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
    }
}
