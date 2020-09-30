package com.gwm.base;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.gwm.inter.IViewBind;

public class BaseBottomDialog<T extends IViewBind> extends BaseDialog<T> {
    private int mDialogHeight;

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        if (win == null) {
            return;
        }
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL); //可设置dialog的位置
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        WindowManager.LayoutParams params = win.getAttributes();
        if (mDialogHeight > 0) {
            win.setLayout(dm.widthPixels, mDialogHeight);
        } else {
            win.setLayout(dm.widthPixels, params.height);
        }
    }
    /**
     * 设置底部Dialog高度
     * */
    public void setDialogHeight(int height) {
        mDialogHeight = height;
    }
}
