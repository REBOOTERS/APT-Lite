package com.engineer.apt_processor;

import com.engineer.apt_annotation.BindString;
import com.engineer.apt_annotation.BindView;
import com.engineer.apt_processor.creator.BindCreatorProxy;
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


    private Map<String, BindCreatorProxy> mProxyProMap = new HashMap<>();

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
        // 代理方法清空
        mProxyProMap.clear();
        // 处理注解，完成需要生成代码的前期准备
        assembleAnnotations(roundEnvironment);
        // 生成代码
        genCodeAuto();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
        return true;
    }

    /**
     *
     * @param roundEnvironment 集中收集所有需要处理的 注解
     */
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

        // 获取所有打上 className 类型注解的元素
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(className);
        // 遍历元素
        for (Element element : elements) {
            // 因为 BindView 和 BindString 的 target 是 ElementType.FIELD，因此这里强转理论上是安全的，
            // 除非故意把注解打在了错误的位置
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();

            BindCreatorProxy proxy = mProxyProMap.get(fullClassName);
            if (proxy == null) {
                proxy = new BindCreatorProxy(mElementUtils, classElement);
                mProxyProMap.put(fullClassName, proxy);
            }

            if (className.getCanonicalName().equals(BindView.class.getCanonicalName())) {
                // BindView 注解处理
                BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
                int id = bindAnnotation.value();
                // 返回当前的 id 的 human 值，类似 R.id.xxx
                String idRes = getBindViewResId(parser, fullClassName, id);
                ResModel model = new ResModel(idRes, id, BindView.class);
                proxy.putElement(model, variableElement);

            } else if (className.getCanonicalName().equals(BindString.class.getCanonicalName())) {
                // BindString 注解处理
                BindString bindAnnotation = variableElement.getAnnotation(BindString.class);
                String str = bindAnnotation.value();
                proxy.putElement(str, variableElement);
            }
        }
    }

    /**
     * @param parser        {@link RParser}
     * @param fullClassName 当前注解所在的类名
     * @param id            资源的原始 id （类似 2343434343，438943493，在 R 文件中）
     * @return 返回资源的 R.id.xxx (包含完整包名）
     */
    private String getBindViewResId(RParser parser, String fullClassName, int id) {
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
        return idRes;
    }

    private void genCodeAuto() {
        for (String key : mProxyProMap.keySet()) {
            BindCreatorProxy proxyPro = mProxyProMap.get(key);
            try {
                JavaFile javaFile = JavaFile.builder(proxyPro.getPackageName(), proxyPro.generatorJavaCode()).build();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "---> create file start\n" + javaFile.toString());
                javaFile.writeTo(mFiler);
                mMessager.printMessage(Diagnostic.Kind.NOTE, "---> create file finish\n");
            } catch (Exception e) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " +
                        proxyPro.getProxyClassFullName() + " error,because " + e.getMessage());
            }
        }

    }
}
