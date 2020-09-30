package com.fairplay.examgradle.activity;

import android.content.Intent;

import com.app.layout.activity_splash;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.SplashViewModel;
import com.gwm.android.Handler;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmActivity;

@Layout(R.layout.activity_splash)
public class SplashActivity extends BaseMvvmActivity<Object, SplashViewModel,activity_splash> {
    @Override
    protected void onResume() {
        super.onResume();
        Handler.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        },1000);
    }

    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }
}
