package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_display;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.Student;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ResultAdapter;
import com.fairplay.examgradle.viewmodel.DataDisplayViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

import java.util.List;

@Layout(R.layout.activity_data_display)
public class DataDisplayActivity extends BaseMvvmTitleActivity<Object, DataDisplayViewModel, activity_data_display> {
    public static final String StudentCode = "StudentCode";
    private String studentCode;

    @Override
    protected Class<DataDisplayViewModel> getViewModelClass() {
        return DataDisplayViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return builder.setTitle("数据详情")
                .setLeftText("返回")
                .setLeftImageResource(R.mipmap.icon_white_goback)
                .setLeftImageVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentCode = getIntent().getStringExtra(StudentCode);
        Student student = DBManager.getInstance().queryStudentByStuCode(studentCode);
        mBinding.tv_stuCode.setText(studentCode);
        mBinding.tv_stuName.setText(student.getStudentName());
        mBinding.tv_sex.setText(student.getSex() == 0?"男":"女");
        List<RoundResult> roundResults = DBManager.getInstance().getRoundResultByStuCode(studentCode);
        mBinding.rv_result.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.rv_result.setAdapter(new ResultAdapter(this,roundResults));

    }
}
