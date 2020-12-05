package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.HttpInterceptor;
import com.gwm.annotation.layout.Application;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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
public class HttpInterceptorProcessor extends BaseProcessor {
    public static final String packageName = "com.app.http";
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (javax.lang.model.element.Element elements : roundEnvironment.getElementsAnnotatedWith(Application.class)) {
            if (elements.getKind() == ElementKind.CLASS) {
                ClassName OkHttpClient = ClassName.get("okhttp3", "OkHttpClient");
                ClassName CookieJarImpl = ClassName.get("com.zhy.http.okhttp.cookie", "CookieJarImpl");
                ClassName PersistentCookieStore = ClassName.get("com.zhy.http.okhttp.cookie.store", "PersistentCookieStore");
                ClassName Context = ClassName.get("android.content", "Context");
                ClassName TimeUnit = ClassName.get("java.util.concurrent", "TimeUnit");
                MethodSpec.Builder builder = MethodSpec.methodBuilder("getOkHttpClient")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(OkHttpClient)
                        .addParameter(Context, "context")
                        .addCode("$T cookieJar = new $T(new $T(context));\n", CookieJarImpl, CookieJarImpl, PersistentCookieStore)
                        .addCode("OkHttpClient okHttpClient = new OkHttpClient.Builder()\n")
                        .addCode(".cookieJar(cookieJar)\n")
                        .addCode(".connectTimeout(60L*2L, $T.SECONDS)\n", TimeUnit)
                        .addCode(".readTimeout(60L*20L, TimeUnit.SECONDS)\n" +
                                ".writeTimeout(60L*20L, TimeUnit.SECONDS)\n")
                        .addCode(".retryOnConnectionFailure(true)\n");
//                ClassName HttpInterceptor = ClassName.get("com.gwm.http", "HttpInterceptor");
//                builder.addCode("\t.addInterceptor(new $T())\n", HttpInterceptor);
                for (Element element : roundEnvironment.getElementsAnnotatedWith(HttpInterceptor.class)) {
                    if (element.getKind() == ElementKind.CLASS) {
                        String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName() + "";
                        ClassName className = ClassName.get(packageName, element.getSimpleName().toString());
                        builder.addCode("\t.addInterceptor(new $T())\n", className);
                    }
                }
                builder.addCode("\t.build();\n");
                builder.addCode("return okHttpClient;\n");
                MethodSpec method = builder.build();
                TypeSpec clazz = TypeSpec.classBuilder("HttpClients")
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ClassName.get("com.gwm.http", "HttpClients"))
                        .addMethod(method)
                        .build();
                writeClazz(packageName, clazz);
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(HttpInterceptor.class.getCanonicalName());
        stringSet.add(HTTP.class.getCanonicalName());
        stringSet.add(Application.class.getCanonicalName());
        return stringSet;
    }
}
