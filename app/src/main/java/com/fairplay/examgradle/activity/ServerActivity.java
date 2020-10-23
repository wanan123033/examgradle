package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.layout.activity_server;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.base.BaseTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.tencent.mmkv.MMKV;

@Layout(R.layout.activity_server)
public class ServerActivity extends BaseTitleActivity<activity_server> {
    private MMKV mmkv;
    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return super.setTitleBarBuilder(builder.setTitle("服务器"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        String string = mmkv.getString(MMKVContract.BASE_URL, "http://");
        mBinding.et_server.setText(string);
        mBinding.et_server.setSelection(mBinding.et_server.getText().length());
    }

    @OnClick({R.id.btn_login})
    public void onClick(View view){
        mmkv.putString(MMKVContract.BASE_URL,mBinding.et_server.getText().toString().trim());
        finish();
    }
}
