package com.gwm.util;

import com.gwm.inter.IViewBind;

public interface LayoutInflaterUtil {
    <V extends IViewBind> V getViewBind(int resId);
    void clear();
}
