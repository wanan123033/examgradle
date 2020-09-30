package com.fairplay.examgradle.activity;

import android.view.View;

import com.app.layout.activity_exam_score;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.ExamScoreViewModel;
import com.gwm.annotation.layout.Layout;
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

}
