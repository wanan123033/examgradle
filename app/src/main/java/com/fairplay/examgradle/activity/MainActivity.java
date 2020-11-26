package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.app.layout.activity_main;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.ChannelBean;
import com.fairplay.examgradle.bean.EnvInfoBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.contract.Permission;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.fairplay.examgradle.utils.CommonUtils;
import com.fairplay.examgradle.viewmodel.MainViewModel;
import com.feipulai.common.view.baseToolbar.StatusBarUtil;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmActivity;
import com.gwm.util.ContextUtil;
import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import com.orhanobut.logger.utils.LogUtils;
import com.tencent.mmkv.MMKV;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseMvvmActivity<Object, MainViewModel,activity_main>{
    private static final int QR_CODE = 7598;
    private MMKV mmkv;
    @OnClick({R.id.card_test,R.id.card_select,R.id.card_re,R.id.card_cannal})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.card_test:
//                showDialog("sssssss");
                String permission = mmkv.getString(MMKVContract.PERMISSION,"");
                if (permission.contains(Permission.hasTestScore)) {
                    String ip = mmkv.getString(MMKVContract.MQIP, "");
                    String port = mmkv.getString(MMKVContract.MQPORT, "");
                    startMqttService(ip, port);
                }else {
                    ToastUtils.showShort("该用户无打分权限");
                    return;
                }
//                Intent intent = new Intent(getApplicationContext(),ExamResultActivity.class);
//                startActivity(intent);
                break;
            case R.id.card_select:
                Intent intent1 = new Intent(getApplicationContext(),DataManagerActivity.class);
                startActivity(intent1);
                break;
            case R.id.card_re:
                Intent intent2 = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(intent2,QR_CODE);
                break;
            case R.id.card_cannal:
                MqttManager.getInstance().disConnect();
                Intent intent3 = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent3);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        addFirstToast();
        mBinding.txt_deviceid.setText(CommonUtils.getDeviceId(ContextUtil.get()));
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CODE && data != null){
            String result = data.getStringExtra(Intents.Scan.RESULT);
            ChannelBean channelBean = GsonUtils.fromJson(result, ChannelBean.class);
            viewModel.scanQr(channelBean.channelCode);
        }
    }
    private void startMqttService(String ip,String port) {
        showDialog("连接中...");
        if(!MqttManager.getInstance().isConnected()){
            MqttManager.getInstance().init(getApplication())
                    .setServerIp(ip)
                    .setServerPort(Integer.parseInt(port))
                    .connect(this);
            LogUtils.operation("MQTT 连接信息:ip="+ip+",port="+port);
            MqttManager.getInstance().regeisterServerMsg(new OnMqttAndroidConnectListener() {
                @Override
                public void connect() {
                    super.connect();
                    dismissDialog();
                    LogUtils.operation("MQTT 连接成功");
                    Intent intent = new Intent(getApplicationContext(),ExamResultActivity.class);
                    startActivity(intent);
                }

                @Override
                public void disConnect() {
                    super.disConnect();
                    LogUtils.operation("MQTT 连接断开");
                    dismissDialog();
                }

                @Override
                public void onDataReceive(String message) {
                    Log.e("TAG===>",message);
                }
            });
        }
    }
}
