package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.layout.item_exam;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_exam)
public class ExamAdapter extends BaseRecyclerViewAdapter<ExamAdapter.ExamViewHolder, item_exam, ExamScoreBean> {

    public ExamAdapter(Context context, List<ExamScoreBean> data) {
        super(context, data);
    }

    @Override
    protected void setData(ExamScoreBean examScoreBean, int position) {

    }

    @Override
    protected ExamViewHolder getViewHolder(View itemView) {
        return null;
    }

    public static class ExamViewHolder extends BaseRecyclerViewAdapter.ViewHolder<item_exam,ExamScoreBean>{

        public ExamViewHolder(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
