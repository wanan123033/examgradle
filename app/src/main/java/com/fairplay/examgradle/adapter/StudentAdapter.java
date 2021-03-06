package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.app.layout.item_data_retieve;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.examgradle.R;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_data_retieve)
public class StudentAdapter extends BaseRecyclerViewAdapter<StudentAdapter.ViewHodel, item_data_retieve,DataRtiveBean> {
    public StudentAdapter(Context context, List<DataRtiveBean> student) {
        super(context,student);
    }

    @Override
    protected void setData(DataRtiveBean student, int position) {


    }

    @Override
    protected ViewHodel getViewHolder(View itemView) {
        return new ViewHodel(this,itemView);
    }

    static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_data_retieve,DataRtiveBean>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }

        @Override
        public void setData(DataRtiveBean student, int position) {
            super.setData(student, position);
            mBinding.tv_stuCode.setText(student.studentCode);
            mBinding.tv_stuName.setText(student.itemName);
            mBinding.tv_sex.setText(student.examPlaceName);
            mBinding.tv_score.setText(student.result);
            mBinding.tv_fraction.setText(student.score);

            mBinding.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                student.isSelected = isChecked;
            });
        }
    }
}
