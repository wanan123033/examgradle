package com.gwm.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gwm.R;
import com.gwm.annotation.layout.Layout;
import com.gwm.inter.IViewBind;
import com.gwm.layout.LayoutEventUtil;
import com.gwm.view.titlebar.CustomNavigatorBar;
import com.gwm.view.titlebar.TitleBarBuilder;

public abstract class BaseTitleActivity<V extends IViewBind> extends BaseActivity {
    private CustomNavigatorBar titleBar;
    private FrameLayout content;
    protected V mBinding;
    @Override
    public final int getLayout() {
        return R.layout.activity_base_title;
    }

    @Override
    protected void setContentView() {
        Layout layout = getClass().getAnnotation(Layout.class);
        int value = 0;
        if (layout != null) {
            value = layout.value();
        }
        setContentView(getLayout());
        titleBar = findViewById(R.id.cnb_title);
        titleBar.setTitleBulder(setTitleBarBuilder(new TitleBarBuilder()));
        content = findViewById(R.id.frame_content);
        View view = LayoutInflater.from(this).inflate(value,content,false);
        content.addView(view);
        mBinding = (V) getViewBind(layout.value());
        mBinding.bindView(view);
        LayoutEventUtil.getInstance().bind(this,view);
    }

    public abstract TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder);
}
