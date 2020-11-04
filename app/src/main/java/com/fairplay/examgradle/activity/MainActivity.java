package com.fairplay.examgradle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.app.layout.activity_main;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.mq.MqttManager;
import com.fairplay.examgradle.mq.interfaces.OnMqttAndroidConnectListener;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;

import java.util.List;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseTitleActivity<activity_main> {
    private static final int QR_CODE = 7598;

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return null;
    }
    @OnClick({R.id.card_test,R.id.card_select,R.id.card_re,R.id.card_cannal})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.card_test:
//                List<Item> items = DBManager.getInstance().getItemList();
//                showItemDialog(items);
                Intent intent = new Intent(getApplicationContext(),ExamActivity.class);
                startActivity(intent);
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
                Intent intent3 = new Intent(getApplicationContext(),ItemInitActivity.class);
                startActivity(intent3);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFirstToast();
    }
    public void showItemDialog(final List<Item> items){
        String[] itemStr = new String[items.size()];
        for (int i = 0 ; i < items.size() ; i++){
            itemStr[i] = items.get(i).getItemName();
        }
        new AlertDialog.Builder(this).setTitle("提示信息")
                .setItems(itemStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),ExamActivity.class);
                        intent.putExtra("itemCode",items.get(which).getItemCode());
                        intent.putExtra("subItemCode",items.get(which).getSubitemCode());
                        startActivity(intent);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CODE){
            String result = data.getStringExtra(Intents.Scan.RESULT);
            Log.e("TAG====>",result);
        }
    }
}
