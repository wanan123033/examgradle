package com.fairplay.examgradle.activity;

import android.os.Bundle;

import com.app.layout.activity_exam_score;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.ExamViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmTitleActivity;

@Layout(R.layout.activity_exam_score)
public class ExamActivity extends BaseMvvmTitleActivity<Object, ExamViewModel, activity_exam_score> {

    @Override
    protected Class<ExamViewModel> getViewModelClass() {
        return ExamViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
