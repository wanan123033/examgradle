package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.Application;
import com.gwm.annotation.layout.Layout;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class AppProcessor extends BaseProcessor {
    private static final String packageName = "com.app.layout";

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Application.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                String value = element.getAnnotation(Application.class).value();
                File layoutDir = new File(value + "/src/main/res/layout");
                File[] files = layoutDir.listFiles();
                TypeSpec.Builder datainflater = TypeSpec.classBuilder("LayoutInflaterUtils").addModifiers(Modifier.PUBLIC);
                FieldSpec layouts = FieldSpec.builder(ClassName.get("com.gwm.android","SparseArray"),"layouts", Modifier.PRIVATE,Modifier.STATIC,Modifier.FINAL).build();
                datainflater.addField(layouts);
                datainflater.addSuperinterface(ClassName.get("com.gwm.util","LayoutInflaterUtil"));
                FieldSpec layoutArr = FieldSpec.builder(ClassName.get("com.gwm.android","SparseArray"),"layoutArr", Modifier.PRIVATE).build();
                datainflater.addField(layoutArr);
                CodeBlock.Builder block = CodeBlock.builder();
                block.add(CodeBlock.builder().add("layouts = new SparseArray<String>();\nlayouts.clear();\n").build());
                for (File layoutFile : files) {
                    String name = layoutFile.getName();
                    TypeSpec.Builder clazz = TypeSpec.classBuilder(name.substring(0, name.indexOf("."))).addModifiers(Modifier.PUBLIC);
                    //TODO 获取xml里面所有ID控件
                    Map<String, ClassName> views = getViews(layoutFile);
                    Set<String> keySet = views.keySet();
                    MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("bindView")
                            .addParameter(ParameterSpec.builder(ClassName.get("android.view","View"),"view").build())
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC);
                    for (String key:keySet) {
                        ClassName values = views.get(key);
                        FieldSpec fieldSpec = FieldSpec.builder(values,key,Modifier.PUBLIC).build();
                        clazz.addField(fieldSpec);
                        if (!values.packageName().equals("com.app.layout")) {
                            methodSpec.addCode(key + "=view.findViewById($T.id." + key + ");\n", ClassName.get(getRPackageName(value), "R"));
                        }else {
                            methodSpec.addCode(key + "=new "+values.simpleName()+"();\n");
                            methodSpec.addCode(key + ".bindView("+"view.findViewById($T.id." + key + "));\n", ClassName.get(getRPackageName(value), "R"));
                        }
                    }
                    clazz.addMethod(methodSpec.build());
                    MethodSpec.Builder bindData = MethodSpec.methodBuilder("bindData")
                            .addParameter(Object.class,"data")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC);
                    Map<String, String> datas = getDatas(layoutFile);
                    Set<String> datasKeySet = datas.keySet();
                    for (String key:datasKeySet) {
                        String values = datas.get(key);

                    }
                    clazz.addMethod(bindData.build());
                    clazz.addSuperinterface(ClassName.get("com.gwm.inter","IViewBind"));

                    writeClazz(packageName,clazz.build());

                    block.add(CodeBlock.builder().add("layouts.put($T.layout."+name.substring(0, name.indexOf("."))+",$S);\n",
                            ClassName.get(getRPackageName(value),"R"),
                            packageName+"."+name.substring(0, name.indexOf("."))).build());
                }
                datainflater.addStaticBlock(block.build());

                MethodSpec getViewBind = MethodSpec.methodBuilder("getViewBind")
                        .addModifiers(Modifier.PUBLIC,Modifier.SYNCHRONIZED)
                        .addParameter(ParameterSpec.builder(int.class,"layoutId").build())
                        .addCode("if(layoutArr == null){\n" +
                                "      layoutArr = new SparseArray<IViewBind>();\n" +
                                " }\n" +
                                " IViewBind bind = (IViewBind)layoutArr.get(layoutId);\n" +
                                " if(bind == null){\n" +
                                " try {\n" +
                                "      bind = (IViewBind) Class.forName((String)layouts.get(layoutId)).newInstance();\n" +
                                " } catch (IllegalAccessException e) {\n" +
                                "      e.printStackTrace();\n" +
                                " } catch (InstantiationException e) {\n" +
                                "      e.printStackTrace();\n" +
                                " } catch (ClassNotFoundException e) {\n" +
                                "      e.printStackTrace();\n" +
                                " }\n" +
                                " layoutArr.put(layoutId,bind);\n" +
                                " }\n" +
                                " return bind;\n")
                        .addAnnotation(Override.class)
                        .returns(ClassName.get("com.gwm.inter","IViewBind"))
                        .build();
                datainflater.addMethod(getViewBind);

                MethodSpec clear = MethodSpec.methodBuilder("clear")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addCode("layoutArr.clear();\n" +
                                "layouts.clear();\n")
                        .build();
                datainflater.addMethod(clear);

                FieldSpec instance = FieldSpec.builder(ClassName.get(packageName,"LayoutInflaterUtils"),"instance")
                        .addModifiers(Modifier.STATIC,Modifier.PUBLIC)
                        .build();
                datainflater.addField(instance);
                MethodSpec contrutor = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
                datainflater.addMethod(contrutor);
                MethodSpec.Builder getInstance = MethodSpec.methodBuilder("getInstance").addModifiers(Modifier.PUBLIC,Modifier.STATIC,Modifier.SYNCHRONIZED).returns(ClassName.get(packageName,"LayoutInflaterUtils"));
                getInstance.addCode("if(instance == null){\n\tinstance = new LayoutInflaterUtils();\n}\nreturn instance;\n");
                datainflater.addMethod(getInstance.build());
                writeClazz(packageName,datainflater.build());
            }
        }
        return true;
    }

    private Map<String, String> getDatas(File layoutFile) {
        Map<String, String> views = new HashMap<>();
        //TODO  解析xml文件  id====类型
        paserXmlData(views,layoutFile);
        return views;
    }

    private void paserXmlData(Map<String, String> views, File layoutFile) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(layoutFile);
            Element rootele = document.getRootElement();
            getTextMap(views, rootele);
            Iterator it = rootele.elementIterator();
            paseritData(views, it);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(Application.class.getCanonicalName());
        stringSet.add(Layout.class.getCanonicalName());
        return stringSet;
    }

    public Map<String, ClassName> getViews(File layoutFile) {
        Map<String, ClassName> views = new HashMap<>();
        //TODO  解析xml文件  id====类型
        paserXml(views,layoutFile);
        return views;
    }

    private void paserXml(Map<String, ClassName> views, File file) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element rootele = document.getRootElement();
            getMap(views, rootele);
            Iterator it = rootele.elementIterator();
            paserit(views, it);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void paserit(Map<String, ClassName> views, Iterator it) {
        while (it.hasNext()){
            Element next = (Element) it.next();
            getMap(views, next);
            if (next.elementIterator().hasNext()){
                paserit(views,next.elementIterator());
            }
        }
    }
    private void paseritData(Map<String, String> views, Iterator it) {
        while (it.hasNext()){
            Element next = (Element) it.next();
            getTextMap(views, next);
            if (next.elementIterator().hasNext()){
                paseritData(views,next.elementIterator());
            }
        }
    }


    private void getMap(Map<String, ClassName> views, Element next) {
        List<Attribute> attributes = next.attributes();
        for (Attribute attr : attributes){
            String name = attr.getName();
            if (name.equals("id")){
                String id = null;
                if (attr.getValue().contains("+")) {
                    id = attr.getValue().substring(5);
                }else {
                    id = attr.getValue().substring(4);
                }
                String clazzName = next.getName();
                if (clazzName.equals("include")){
                    String value = next.attributeValue("layout").substring(8);
                    ClassName clazz = ClassName.get("com.app.layout",value);
                    views.put(id,clazz);
                    continue;
                }
                if (clazzName.indexOf(".") != -1){
                    ClassName clazz = ClassName.get(clazzName.substring(0,clazzName.lastIndexOf(".")),clazzName.substring(clazzName.lastIndexOf(".") + 1));
                    views.put(id,clazz);
                }else if (clazzName.equals("WebView")){
                    ClassName clazz = ClassName.get("android.webkit",clazzName);
                    views.put(id,clazz);
                }else if (clazzName.equals("TextureView")){
                    ClassName clazz = ClassName.get("android.view",clazzName);
                    views.put(id,clazz);
                }else if (!clazzName.equals("View")){
                    ClassName clazz = ClassName.get("android.widget",clazzName);
                    views.put(id,clazz);
                }else {
                    ClassName clazz = ClassName.get("android.view",clazzName);
                    views.put(id,clazz);
                }
            }
        }
    }

    private void getTextMap(Map<String, String> views, Element next) {
        List<Attribute> attributes = next.attributes();
        for (Attribute attr : attributes){
            String name = attr.getName();
            if (name.equals("id")){
                String id = attr.getValue().substring(5);
                for (Attribute att : attributes){
                    String dd = att.getName();
                    if (dd.equals("text")){
                        String value = att.getValue();
                        views.put(id,value);
                    }
                }
            }
        }
    }
}
