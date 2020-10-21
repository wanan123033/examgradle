package com.fairplay.examgradle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.activity_data_select;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.Student;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.StudentAdapter;
import com.fairplay.examgradle.bean.DataRetrieveBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.utils.ToastUtils;
import com.fairplay.examgradle.viewmodel.DataSelectViewModel;
import com.feipulai.common.db.DataBaseExecutor;
import com.feipulai.common.db.DataBaseRespon;
import com.feipulai.common.db.DataBaseTask;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseApplication;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Layout(R.layout.activity_data_select)
public class DataSelectActivity extends BaseMvvmTitleActivity<Object, DataSelectViewModel, activity_data_select> {
    private MMKV mmkv;
    private StudentAdapter adapter;
    private int mPageNum;
    private List<DataRetrieveBean> mList;
    @Override
    protected Class<DataSelectViewModel> getViewModelClass() {
        return DataSelectViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmkv = BaseApplication.getInstance().getMmkv();
        mList = new ArrayList<>();
        adapter = new StudentAdapter(this,mList);
        mBinding.rv_results.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mBinding.rv_results.setAdapter(adapter);
        selectAll();
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DataDisplayActivity.class);
                String studentCode = adapter.getData().get(position).getStudentCode();
                intent.putExtra(DataDisplayActivity.StudentCode,studentCode);
                startActivity(intent);
            }
        });

    }

    private void selectAll() {
        final String itemCode = mmkv.getString(MMKVContract.CURRENT_ITEM,"");

        DataBaseExecutor.addTask(new DataBaseTask() {
            @Override
            public DataBaseRespon executeOper() {
                List<Student> student = DBManager.getInstance().getStudentByItemCode(itemCode, 100, 100*mPageNum);
                return new DataBaseRespon(true,"",student);
            }

            @Override
            public void onExecuteSuccess(DataBaseRespon respon) {
                List<Student> studentList = (List<Student>) respon.getObject();
                if (mPageNum == 0) {
                    mList.clear();
                    Map<String, Object> countMap = DBManager.getInstance().getItemStudenCount(getItemCode());
                    setStuCount(countMap.get("count"), countMap.get("women_count"), countMap.get("man_count"));
                    Logger.i("zzs===>" + countMap.toString());
                }
                if (studentList == null || studentList.size() == 0) {
                    ToastUtils.showShort("没有更多数据了");
                    mBinding.refreshview.finishRefreshAndLoad();
                    mBinding.refreshview.setLoadmoreFinished(true);
                    adapter.notifyDataSetChanged();
                    return;
                }
                //这个必须在获取到数据后再自增
                mPageNum++;
                for (int i = 0; i < studentList.size(); i++) {
                    //获取学生信息
                    Student student = studentList.get(i);
                    RoundResult result = displaStuResult(student.getStudentCode());
                    DataRetrieveBean bean = new DataRetrieveBean();
                    bean.setSex(student.getSex());
                    bean.setStudentName(student.getStudentName());
                    bean.setStudentCode(student.getStudentCode());
                    bean.setScore(result.getScore());
                    bean.setFaition(result.getResult());
                    bean.setWight(result.getWight());
                    mList.add(bean);
                }
                mBinding.refreshview.finishRefreshAndLoad();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onExecuteFail(DataBaseRespon respon) {

            }
        });

    }

    private RoundResult displaStuResult(String studentCode) {
        return DBManager.getInstance().getLastRoundResult(studentCode);
    }

    private String getItemCode() {
        return mmkv.getString(MMKVContract.CURRENT_ITEM,"");
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return builder.setTitle("数据查看")
                .setLeftText("返回")
                .setLeftImageResource(R.mipmap.icon_white_goback)
                .setLeftImageVisibility(View.VISIBLE);
    }
    private void setStuCount(Object sumCount, Object womenCount, Object mamCount) {
        mBinding.txt_stu_sumNumber.setText(sumCount + "");
        mBinding.txt_stu_manNumber.setText(mamCount + "");
        mBinding.txt_stu_womemNumber.setText(womenCount + "");
    }
}
