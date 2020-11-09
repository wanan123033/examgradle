package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.Application;
import com.gwm.annotation.messagebus.HermsMessageService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by Administrator on 2019/1/10.
 */
@AutoService(Processor.class)
public class HermsMessageProcessor extends BaseProcessor {
    private static final String packageName = "com.app.bus";
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (javax.lang.model.element.Element elements : roundEnvironment.getElementsAnnotatedWith(Application.class)) {
            if (elements.getKind() == ElementKind.CLASS) {
                ClassName HermsMessageBusService = ClassName.get("com.gwm.messagesendreceive", "HermsMessageBusService");
                MethodSpec.Builder builder = MethodSpec.methodBuilder("getHersMessageServices");
                builder.returns(ArrayList.class);
                builder.addModifiers(Modifier.PUBLIC);
                builder.addCode("ArrayList<Class<? extends $T>> services = new ArrayList<>();\n", HermsMessageBusService);
                for (Element element : roundEnvironment.getElementsAnnotatedWith(HermsMessageService.class)) {
                    if (element.getKind() == ElementKind.CLASS) {
                        String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName() + "";
                        ClassName HermsMessageBusService1 = ClassName.get(packageName, element.getSimpleName().toString());
                        builder.addCode("services.add($T.class);\n", HermsMessageBusService1);
                    }
                }
                builder.addCode("return services;\n");
                builder.addAnnotation(Override.class);
                TypeSpec clazz = TypeSpec.classBuilder("HermsMessageUtils")
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ClassName.get("com.gwm.messagesendreceive", "HermsMessageUtil"))
                        .addMethod(builder.build())
                        .build();
                writeClazz(packageName, clazz);
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(HermsMessageService.class.getCanonicalName());
        stringSet.add(Application.class.getCanonicalName());
        return stringSet;
    }
}
