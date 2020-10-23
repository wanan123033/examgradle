package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.item_data_retieve;
import com.fairplay.database.entity.Student;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.activity.DataSelectActivity;
import com.fairplay.examgradle.bean.DataRetrieveBean;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_data_retieve)
public class StudentAdapter extends BaseRecyclerViewAdapter<StudentAdapter.ViewHodel, item_data_retieve,DataRetrieveBean> {
    public StudentAdapter(Context context, List<DataRetrieveBean> student) {
        super(context,student);
    }

    @Override
    protected void setData(DataRetrieveBean student, int position) {
        mBinding.tv_stuCode.setText(student.getStudentCode());
        mBinding.tv_stuName.setText(student.getStudentName());
        mBinding.tv_sex.setText(student.getSex() == 1 ? "女":"男");
        mBinding.tv_score.setText(student.getScore());
        mBinding.tv_fraction.setText(student.getFaition());

    }

    @Override
    protected ViewHodel getViewHolder(View itemView) {
        return new ViewHodel(this,itemView);
    }

    static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_data_retieve,DataRetrieveBean>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
