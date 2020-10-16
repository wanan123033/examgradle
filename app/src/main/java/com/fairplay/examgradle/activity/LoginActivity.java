package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.layout.activity_login;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.LoginBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.utils.ToastUtils;
import com.fairplay.examgradle.viewmodel.LoginViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.mvvm.BaseMvvmActivity;
import com.tencent.mmkv.MMKV;

@Layout(R.layout.activity_login)
public class LoginActivity extends BaseMvvmActivity<LoginBean, LoginViewModel, activity_login> {
    private MMKV mmkv;
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = MMKV.defaultMMKV();
        mBinding.et_username.setText(mmkv.getString(MMKVContract.USERNAME,""));
        mBinding.et_password.setText(mmkv.getString(MMKVContract.PASSWORD,""));
    }
    @OnClick({R.id.btn_login,R.id.tv_server})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
//                login();
                break;
            case R.id.tv_server:
                Intent intent1 = new Intent(getApplicationContext(),ServerActivity.class);
                startActivity(intent1);
                break;
        }

    }
    private void login() {
        String username = mBinding.et_username.getText().toString().trim();
        String password = mBinding.et_password.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            ToastUtils.showShort("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtils.showShort("请输入密码");
            return;
        }
        if (mBinding.cb_agin_pass.isChecked()){
            mmkv.putString(MMKVContract.USERNAME,username);
            mmkv.putString(MMKVContract.PASSWORD,password);
        }
        viewModel.login(username,password);
    }

    @Override
    public void onChanged(LoginBean o) {
        super.onChanged(o);

    }
}
