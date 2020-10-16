package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;

import com.app.layout.activity_exam_score;
import com.fairplay.database.DBManager;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.ExamScoreViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

@Layout(R.layout.activity_exam_score)
public class ExamScoreActivity extends BaseMvvmTitleActivity<Object, ExamScoreViewModel, activity_exam_score> {
    @Override
    protected Class<ExamScoreViewModel> getViewModelClass() {
        return ExamScoreViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return builder.setTitle("足球")
                .setLeftText("返回")
                .setLeftImageResource(R.mipmap.icon_white_goback)
                .setLeftImageVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.tv_0,R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6,R.id.tv_7,R.id.tv_8,R.id.tv_9,
            R.id.tv_dian,R.id.tv,R.id.tvj,R.id.tv_enter1,R.id.tv_enter2,R.id.tv_send})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_0:
                break;
            case R.id.tv_1:
                break;
            case R.id.tv_2:
                break;
            case R.id.tv_3:
                break;
            case R.id.tv_4:
                break;
            case R.id.tv_5:
                break;
            case R.id.tv_6:
                break;
            case R.id.tv_7:
                break;
            case R.id.tv_8:
                break;
            case R.id.tv_9:
                break;
            case R.id.tv_dian:
                break;
            case R.id.tv:
                break;
            case R.id.tvj:
                break;
            case R.id.tv_enter1:
                break;
            case R.id.tv_enter2:
                break;
            case R.id.tv_send:

                break;
        }
    }
}
