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

    private View.OnClickListener leftOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

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

    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder){
        return builder.setLeftText("返回")
                .setLeftTextSize(18)
                .setTitleTextSize(18)
                .setLeftImageResource(R.mipmap.icon_white_goback)
                .setLeftImageVisibility(View.VISIBLE)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setLeftTextOnClickListener(leftOnClickListener)
                .setLeftImageOnClickListener(leftOnClickListener);
    }
}
