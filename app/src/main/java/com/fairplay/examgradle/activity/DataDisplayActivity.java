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
    public static final String ItemCode = "ItemCode";
    public static final String SubItemCode = "SubItemCode";
    private String studentCode;
    private String itemCode;
    private String subItemCode;

    @Override
    protected Class<DataDisplayViewModel> getViewModelClass() {
        return DataDisplayViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return super.setTitleBarBuilder(builder.setTitle("数据详情"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentCode = getIntent().getStringExtra(StudentCode);
        itemCode = getIntent().getStringExtra(ItemCode);
        subItemCode = getIntent().getStringExtra(SubItemCode);
        Student student = DBManager.getInstance().queryStudentByStuCode(studentCode);
        mBinding.tv_stuCode.setText(studentCode);
        mBinding.tv_stuName.setText(student.getStudentName());
        mBinding.tv_sex.setText(student.getSex() == 0?"男":"女");
        List<RoundResult> roundResults = DBManager.getInstance().getRoundResultByStuItem(studentCode,itemCode,subItemCode);
        mBinding.rv_result.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.rv_result.setAdapter(new ResultAdapter(this,roundResults));

    }
}
