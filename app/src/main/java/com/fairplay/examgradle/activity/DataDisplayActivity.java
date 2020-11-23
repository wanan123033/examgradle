package com.fairplay.examgradle.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_display;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.MyApplication;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.ResultAdapter;
import com.fairplay.examgradle.bean.ScoreBean;
import com.fairplay.examgradle.contract.Unit;
import com.fairplay.examgradle.viewmodel.DataDisplayViewModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_data_display)
public class DataDisplayActivity extends BaseMvvmTitleActivity<Object, DataDisplayViewModel, activity_data_display> {
    public static final String StudentCode = "StudentCode";
    public static final String ItemCode = "ItemCode";
    public static final String SubItemCode = "SubItemCode";
    public static final String EXAM_PLACE_NAME = "ExamPlaceName";
    private String studentCode;
    private String itemCode;
    private String subItemCode;
    private String examplaceName;
    private List<ScoreBean> scoreBeans;

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
        examplaceName = getIntent().getStringExtra(EXAM_PLACE_NAME);
        subItemCode = getIntent().getStringExtra(SubItemCode);
        Item item = DBManager.getInstance().getItemByItemCode(itemCode,itemCode);
        Item subItem = DBManager.getInstance().getItemByItemCode(itemCode,subItemCode);
        mBinding.tv_sex.setText(item.getItemName()+"-"+subItem.getItemName());
        mBinding.tv_stuCode.setText(studentCode);
        mBinding.tv_stuName.setText(examplaceName);
        List<RoundResult> stuRoundResult = DBManager.getInstance().getStuRoundResult(studentCode, itemCode, subItemCode);
        scoreBeans = new ArrayList<>();
        for (RoundResult result : stuRoundResult){
            if (result.getIsMultioleResult() == 0){  //单值类项目
                ScoreBean scoreBean = new ScoreBean();
                scoreBean.roundNo = result.getRoundNo();

                if (subItem.getMarkScore() == 0) {  //测量项目
                    if(subItem.getTestType() != 1) {
                        scoreBean.result = result.getResult() + Unit.getUnit(subItem.getUnit()).getDesc();
                    }else {
                        MyApplication.scoreDateFormat.applyPattern(subItem.getRemark1());
                        scoreBean.result = MyApplication.scoreDateFormat.format(new Date(Long.parseLong(result.getResult())));
                    }
                }else {
                    scoreBean.score = result.getScore();
                }

                scoreBean.examStatus = result.getExamType();
                scoreBean.testTime = MyApplication.simpleDateFormat.format(new Date(Long.parseLong(result.getTestTime())));
                scoreBeans.add(scoreBean);
            }else {
                List<MultipleResult> multipleResults = DBManager.getInstance().getMultioleResult(result.getId());
                for (MultipleResult multipleResult : multipleResults){
                    ScoreBean scoreBean = new ScoreBean();

                    scoreBean.testTime = MyApplication.simpleDateFormat.format(new Date(Long.parseLong(result.getTestTime())));
                    if (subItem.getMarkScore() == 0){  //测量项目
                        if(subItem.getTestType() != 1) {
                            scoreBean.result = multipleResult.getDesc() + "--" + multipleResult.getScore() + "  " + Unit.getUnit(subItem.getUnit()).getDesc();
                        }else {
                            MyApplication.scoreDateFormat.applyPattern(subItem.getRemark1());
                            scoreBean.result = MyApplication.scoreDateFormat.format(new Date(Long.parseLong(result.getScore())));
                        }
                    }else { //打分项目
                        scoreBean.score = multipleResult.getDesc()+"--"+multipleResult.getScore();
                    }
                    scoreBean.roundNo = result.getRoundNo();
                    scoreBean.examStatus = result.getExamType();
                    scoreBeans.add(scoreBean);
                }
            }
            ResultAdapter resultAdapter = new ResultAdapter(this,scoreBeans);
            mBinding.rv_result.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
            mBinding.rv_result.setAdapter(resultAdapter);
        }
    }
}
