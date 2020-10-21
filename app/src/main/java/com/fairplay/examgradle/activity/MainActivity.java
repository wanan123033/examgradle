package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.layout.activity_main;
import com.fairplay.examgradle.R;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

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
                Intent intent = new Intent(getApplicationContext(),ExamScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.card_select:
                Intent intent1 = new Intent(getApplicationContext(),DataManagerActivity.class);
                startActivity(intent1);
                break;
            case R.id.card_re:

                break;
            case R.id.card_cannal:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFirstToast();
    }
}
