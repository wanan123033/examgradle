package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnCheckedChange;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.layout.OnItemClick;
import com.gwm.annotation.layout.OnItemSelected;
import com.gwm.annotation.layout.OnLongClick;
import com.gwm.annotation.layout.OnMultiClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class EventProcessor extends BaseProcessor {
    private static final String packageNames = "com.app.layoutevent";
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> layoutEles = roundEnvironment.getElementsAnnotatedWith(Layout.class);
        for (Element layoutEle : layoutEles) {
            if (layoutEle.getKind() == ElementKind.CLASS) {
                String simple = layoutEle.getSimpleName().toString();
                String packageName = processingEnv.getElementUtils().getPackageOf(layoutEle).getQualifiedName().toString();
                TypeSpec.Builder eventLis = TypeSpec.classBuilder(simple+"Event").addModifiers(Modifier.PUBLIC);
                eventLis.addSuperinterface(ClassName.get("com.gwm.layout","LayoutEvent"));

                MethodSpec.Builder bindEvent = MethodSpec.methodBuilder("bindEvent").addModifiers(Modifier.PUBLIC);
                bindEvent.addParameter(Object.class,"act");
                bindEvent.addParameter(ClassName.get("android.view","View"),"view");
                bindEvent.addCode("final $T activity = ($T)act;\n",ClassName.get(packageName,simple),ClassName.get(packageName,simple));
                List<? extends Element> elements = layoutEle.getEnclosedElements();
                for (Element me : elements){
                    String methodName = me.getSimpleName().toString();
                    if (me.getAnnotation(OnClick.class) != null){
                        OnClick onClick = me.getAnnotation(OnClick.class);
                        for (int resId : onClick.value()) {
                            bindEvent.addCode("view.findViewById(" + resId + ").setOnClickListener(new android.view.View.OnClickListener(){\n" +
                                    "\tpublic void onClick(View view){\n" +
                                    "\t\tactivity." + methodName + "(view);\n" +
                                    "\t}\n" +
                                    "});\n");
                        }
                    }
                    if (me.getAnnotation(OnItemClick.class) != null){
                        OnItemClick onClick = me.getAnnotation(OnItemClick.class);
                        for (int resId : onClick.value()) {
                            bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnItemClickListener(new AdapterView.OnItemClickListener(){\n" +
                                    "\tpublic void onItemClick(AdapterView<?> parent, View view, int position, long id){\n" +
                                    "\t\tactivity." + methodName + "(parent,position);\n" +
                                    "\t}\n" +
                                    "});\n",ClassName.get("android.widget","AdapterView"));
                        }
                    }
                    if (me.getAnnotation(OnItemSelected.class) != null){
                        OnItemSelected onClick = me.getAnnotation(OnItemSelected.class);
                        for (int resId : onClick.value()) {
                            bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){\n" +
                                    "\tpublic void onItemSelected(AdapterView<?> parent, View view, int position, long id) {\n" +
                                    "\t\tactivity." + methodName + "(parent,position);\n" +
                                    "\t}\n" +
                                    "public void onNothingSelected(AdapterView<?> parent){\n" +
                                    "}\n"+
                                    "});\n",ClassName.get("android.widget","AdapterView"));
                        }
                    }
                    if (me.getAnnotation(OnCheckedChange.class) != null){
                        OnCheckedChange onClick = me.getAnnotation(OnCheckedChange.class);
                        for (int resId : onClick.value()) {
                            bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){\n" +
                                    "\tpublic void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {\n" +
                                    "\t\tactivity." + methodName + "(buttonView,isChecked);\n" +
                                    "\t}\n" +
                                    "});\n",ClassName.get("android.widget","CompoundButton"));
                        }
                    }
                    if (me.getAnnotation(OnMultiClick.class) != null){
                        OnMultiClick onMultiClick = me.getAnnotation(OnMultiClick.class);
                        for (int resId : onMultiClick.value()){
                            bindEvent.addCode("view.findViewById(" + resId + ").setOnClickListener(new com.gwm.android.OnMultiClickListener(){\n" +
                                    "\tpublic void onMultiClick(View view){\n" +
                                    "\t\tactivity." + methodName + "(view);\n" +
                                    "\t}\n" +
                                    "});\n");
                        }
                    }
                    if (me.getAnnotation(OnLongClick.class) != null){
                        OnLongClick onMultiClick = me.getAnnotation(OnLongClick.class);
                        for (int resId : onMultiClick.value()){
                            bindEvent.addCode("view.findViewById(" + resId + ").setOnLongClickListener(new android.view.View.OnLongClickListener(){\n" +
                                    "\tpublic boolean onLongClick(View v){\n" +
                                    "\t\treturn activity." + methodName + "(v);\n" +
                                    "\t}\n" +
                                    "});\n");
                        }
                    }
                }
                eventLis.addMethod(bindEvent.build());
                writeClazz(packageNames,eventLis.build());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(Layout.class.getCanonicalName());
        stringSet.add(OnClick.class.getCanonicalName());
        stringSet.add(OnItemClick.class.getCanonicalName());
        stringSet.add(OnMultiClick.class.getCanonicalName());
        stringSet.add(OnCheckedChange.class.getCanonicalName());
        stringSet.add(OnItemSelected.class.getCanonicalName());
        stringSet.add(OnLongClick.class.getCanonicalName());
        return stringSet;
    }
}
