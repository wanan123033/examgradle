package com.fairplay.examgradle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.app.layout.activity_main;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.examgradle.R;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

import java.util.List;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseTitleActivity<activity_main> {
    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return null;
    }
    @OnClick({R.id.card_test,R.id.card_select,R.id.card_re,R.id.card_cannal})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.card_test:
                List<Item> items = DBManager.getInstance().getItemList();
                showItemDialog(items);
//                Intent intent = new Intent(getApplicationContext(),ExamScoreActivity.class);
//                startActivity(intent);
                break;
            case R.id.card_select:
                Intent intent1 = new Intent(getApplicationContext(),DataManagerActivity.class);
                startActivity(intent1);
                break;
            case R.id.card_re:

                break;
            case R.id.card_cannal:
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
                        startActivity(intent);
                    }
                }).show();
    }
}
