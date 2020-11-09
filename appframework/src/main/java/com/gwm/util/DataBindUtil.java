package com.gwm.util;

import android.view.View;
import android.widget.TextView;

import com.gwm.base.ViewClick;
import com.gwm.inter.IViewBind;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

public class DataBindUtil {

    public static void cache(ViewClick object){
        try {
            HashMap<String,View> viewMap = new HashMap<>();
            Field mBinding = object.getClass().getField("mBinding");
            mBinding.setAccessible(true);
            IViewBind bind = (IViewBind) mBinding.get(object);
            Field[] fields = bind.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                if (field.get(bind) instanceof View) {
                    View view = (View) field.get(bind);
                    if (view instanceof TextView) {
                        String text = ((TextView) view).getText().toString();
                        if (text.contains("@{") && text.endsWith("}")){
                            text = text.substring(text.indexOf("@{") + 1);
                            viewMap.put(text, view);
                        }
                    }
                }
            }
            object.getView().setTag(viewMap);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static <M> void bind(View v, M o) {
        if (v.getTag() == null || !(v.getTag() instanceof HashMap)){
            return;
        }
        HashMap<String,View> tag = (HashMap<String, View>) v.getTag();
        Set<String> keySet = tag.keySet();
        for (String key : keySet) {
            View view = tag.get(key);
            Field[] fields = o.getClass().getFields();
            String name = key.substring(key.indexOf(".") + 1,key.lastIndexOf("}"));
            for (Field field : fields){
                if (field.getName().equals(name)){
                    if (view instanceof TextView) {
                        try {
                            ((TextView) view).setText(field.get(o).toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }
}
