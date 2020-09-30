package com.gwm.layout;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class LayoutEventUtil {
    private Map<String,LayoutEvent> layoutEventMap;
    private static LayoutEventUtil layoutEventUtil;
    private LayoutEventUtil(){
        layoutEventMap = new HashMap<>();
        layoutEventMap.clear();
    }
    public void bind(Object obj, View view){
        String simpleName = obj.getClass().getSimpleName()+"Event";
        String packageName = "com.app.layoutevent";
        try {
            LayoutEvent layoutEvent = layoutEventMap.get(packageName + "." + simpleName);
            if (layoutEvent == null){
                layoutEvent = (LayoutEvent) Class.forName(packageName + "." + simpleName).newInstance();
                layoutEventMap.put(packageName + "." + simpleName,layoutEvent);
            }
            layoutEvent.bindEvent(obj,view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clear(){
        layoutEventMap.clear();
        layoutEventMap = null;
    }

    public synchronized static LayoutEventUtil getInstance(){
        if (layoutEventUtil == null){
            layoutEventUtil = new LayoutEventUtil();
        }
        if (layoutEventUtil.layoutEventMap == null){
            layoutEventUtil = new LayoutEventUtil();
        }
        return layoutEventUtil;
    }
}
