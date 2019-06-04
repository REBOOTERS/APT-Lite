package com.engineer.apt_processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author: zhuyongging
 * @since: 2019-05-19
 */
public abstract class BaseProcessor extends AbstractProcessor {
    // 进行日志打印的接口
    Messager mMessager;
    // Element 元素处理的接口
    Elements mElementUtils;
    // 帮助文件生成的接口
    Filer mFiler;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();

        mMessager.printMessage(Diagnostic.Kind.NOTE, "init");
    }
}
