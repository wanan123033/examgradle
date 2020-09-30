package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.http.FileUpload;
import com.gwm.annotation.http.GenHttp;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.Header;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.HttpModel;
import com.gwm.annotation.http.Path;
import com.gwm.annotation.http.Query;
import com.gwm.annotation.http.QueryUrl;
import com.gwm.annotation.http.RequestBody;
import com.gwm.annotation.http.Url;
import com.gwm.annotation.json.JSON;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@AutoService(Processor.class)
public class GenHttpProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(HttpModel.class);
        for (Element element : elements){
            if (element.getKind() == ElementKind.INTERFACE){
                String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName()+"";
                String simpleName = element.getSimpleName()+"";
                TypeSpec.Builder httpModelImpl = TypeSpec.classBuilder("HttpModel_Impl")
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ClassName.get(packageName,simpleName));
                for (Element e : roundEnvironment.getElementsAnnotatedWith(GenHttp.class)){
                    if (e.getKind() == ElementKind.METHOD){
                        String methodName = e.getSimpleName()+"";
                        MethodSpec.Builder method = MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC);
                        if (e instanceof ExecutableElement){
                            method.addCode("$T httpparam = new $T();\n",
                                    ClassName.get("com.gwm.http","HttpParams"),
                                    ClassName.get("com.gwm.http","HttpParams"));
                            HTTP http = e.getAnnotation(HTTP.class);
                            if (http != null) {
                                method.addCode("httpparam.way = com.gwm.annotation.http.HTTP.WAY." + http.way() + ";\n");
                                method.addCode("httpparam.url = $S;\n", http.url());
                            }
                            Header header = e.getAnnotation(Header.class);
                            if (header != null){
                                method.addCode("httpparam.headers = new $T();\n", HashMap.class);
                                String[] value = header.value();
                                for (String va : value){
                                    String[] split = va.split(":");
                                    method.addCode("httpparam.headers.put($S,$S);\n",split[0],split[1]);
                                }
                            }
                            List<? extends VariableElement> parameters = ((ExecutableElement) e).getParameters();
                            if (parameters.size() > 0) {
                                method.addCode("httpparam.params = new $T();\n",HashMap.class);
                                for (VariableElement parameter : parameters) {
                                    method.addParameter(ClassName.get(parameter.asType()), parameter.getSimpleName() + "");
                                    Query query = parameter.getAnnotation(Query.class);
                                    if (query != null){
                                        method.addCode("httpparam.params.put($S,"+parameter.getSimpleName()+");\n",query.value());
                                    }
                                    HeaderString headerString = parameter.getAnnotation(HeaderString.class);
                                    if (headerString != null){
                                        method.addCode("if(httpparam.headers == null){\nhttpparam.headers = new $T();\n}",HashMap.class);
                                        method.addCode("httpparam.headers.put($S,"+parameter.getSimpleName()+");\n",headerString.value());
                                    }
                                    FileUpload fileUpload = parameter.getAnnotation(FileUpload.class);
                                    if (fileUpload != null){
                                        method.addCode("if(httpparam.files == null){httpparam.files = new $T();\n}",HashMap.class);
                                        method.addCode("httpparam.files.put($S,"+parameter.getSimpleName()+");\n",fileUpload.value());
                                    }
                                    Url url = parameter.getAnnotation(Url.class);
                                    if (url != null){
                                        method.addCode("httpparam.url = "+parameter.getSimpleName()+";\n");
                                    }
                                    RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                                    if (requestBody != null){
                                        method.addCode("httpparam.isRequestBody = true;\n");
                                        method.addCode("httpparam.body = "+parameter.getSimpleName()+";\n");
                                    }
                                    JSON json = parameter.getAnnotation(JSON.class);
                                    if (json != null){
                                        method.addCode("httpparam.isJson = true;\n");
                                        method.addCode("httpparam.json = "+parameter.getSimpleName()+";\n");
                                    }
                                    QueryUrl queryUrl = parameter.getAnnotation(QueryUrl.class);
                                    if (queryUrl != null){
                                        method.addCode("if(httpparam.url != null){\n");
                                        method.addCode("if(httpparam.url.contains(\"?\")){\n");
                                        method.addCode("httpparam.url += (\"&&\" + "+queryUrl.value()+" + \"=\" + "+parameter.getSimpleName()+");\n");
                                        method.addCode("}else{\n");
                                        method.addCode("httpparam.url += (\"?\" + "+queryUrl.value()+" + \"=\" + "+parameter.getSimpleName()+");\n");
                                        method.addCode("}\n");
                                        method.addCode("}\n");
                                    }
                                    Path path = parameter.getAnnotation(Path.class);
                                    if (path != null){
                                        method.addCode("params.url = params.url.replaceAll(\"\\\\{\"+"+path.value()+"+\"\\\\}\", "+parameter.getSimpleName()+"+\"\");");
                                    }
                                }
                            }
                            method.returns(ClassName.get(((ExecutableElement) e).getReturnType()));
                            method.addCode("$T observable = new $T();\n",ClassName.get(((ExecutableElement) e).getReturnType()),ClassName.get(((ExecutableElement) e).getReturnType()));
                            method.addCode("observable.subscriber(httpparam);\n");
                            method.addCode("return observable;\n");
                        }
                        httpModelImpl.addMethod(method.build());
                    }
                }
                writeClazz("com.app.http",httpModelImpl.build());
            }
        }
        return false;
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(GenHttp.class.getCanonicalName());
        stringSet.add(HttpModel.class.getCanonicalName());
        return stringSet;
    }
}
