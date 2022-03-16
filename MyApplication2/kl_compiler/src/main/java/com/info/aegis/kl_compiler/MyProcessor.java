package com.info.aegis.kl_compiler;

import com.google.auto.service.AutoService;
import com.info.aegis.kl_annotation.Hello;
import com.info.aegis.kl_compiler.utils.Log;
import com.info.aegis.kl_compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)

@SupportedAnnotationTypes("com.info.aegis.kl_annotation.Hello")

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor {

    private Map<String, String> rootMap = new TreeMap<>();

    private Log log;

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;

    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        log = Log.newLog(processingEnvironment.getMessager());

        elementUtils = processingEnvironment.getElementUtils();

        filerUtils = processingEnvironment.getFiler();

        log.i("MyProcessor init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!Utils.isEmpty(annotations)) {
            Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(Hello.class);
            if (!Utils.isEmpty(rootElements)) {
                log.i("MyProcessor process");

                int i=0;
                for (Element element : rootElements) {

                    Hello hello = element.getAnnotation(Hello.class);
                    rootMap.put("11"+i, hello.value());
                    log.i("MyProcessor process ::" + rootMap.toString());
                    i++;
                }

                createClass();

            }
            return true;
        }

        return false;
    }

    public void createClass() {

        //创建参数类型 Map<String,String> routes>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(String.class));
        //参数 Map<String,String> routes> routes
        ParameterSpec parameter = ParameterSpec.builder(parameterizedTypeName, "routes").build();
        //函数 public void load(Map<String,String> routes> routes)
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameter);
        //函数体
        for (Map.Entry<String, String> entry : rootMap.entrySet()) {
            methodBuilder.addStatement("routes.put($S, $S)", entry.getKey(), entry.getValue());
        }
        //生成$Root$类
        String className = "kl_test";
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        try {
            JavaFile.builder("com.kl.test", typeSpec).build().writeTo(filerUtils);
            log.i("Generated RouteRoot：" + "com.kl.test" + "." + className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}