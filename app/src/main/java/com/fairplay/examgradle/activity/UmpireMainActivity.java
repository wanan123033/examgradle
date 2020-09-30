package com.fairplay.examgradle.activity;

import android.view.View;

import com.app.layout.activity_umpire;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.UmpireViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 主裁判的主界面
 */
@Layout(R.layout.activity_umpire)
public class UmpireMainActivity extends BaseMvvmTitleActivity<Object, UmpireViewModel, activity_umpire> {
    @Override
    protected Class<UmpireViewModel> getViewModelClass() {
        return UmpireViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return null;
    }

    @OnClick({R.id.btn_push,R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_push:
                push();
                break;
            case R.id.btn_commit:
                break;
        }
    }
    public void push(){
        new SweetAlertDialog(this)
                .setTitleText("提示消息")
                .setContentText("推送成功")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }
}
