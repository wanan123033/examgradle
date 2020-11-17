package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.item_exam;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.contract.MBMContract;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.messagebus.Subscrition;
import com.gwm.base.BaseRecyclerViewAdapter;
import com.gwm.messagesendreceive.MessageBusMessage;

import java.util.List;

@Layout(R.layout.item_exam)
public class ExamAdapter extends BaseRecyclerViewAdapter<ExamAdapter.ExamViewHolder, item_exam, ExamScoreBean> {
    private int selPos = 0;

    public ExamAdapter(Context context, List<ExamScoreBean> data) {
        super(context, data);
    }

    @Override
    protected void setData(ExamScoreBean examScoreBean, int position) {
        mBinding.tv_itemName.setText(examScoreBean.item.getItemName());
        mBinding.rv_score.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL,false));
        ScoreAdapter adapter = new ScoreAdapter(context,examScoreBean.resultList);
        adapter.setSelectPosition(examScoreBean.currentPosition);
        adapter.setCurrentRound(examScoreBean.roundNo);
        if (selPos == position){
            adapter.setShow(true);
        }else {
            adapter.setShow(false);
        }
        mBinding.rv_score.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected ExamViewHolder getViewHolder(View itemView) {
        return new ExamViewHolder(this,itemView);
    }

    public int getSelectPosition() {
        return selPos;
    }
    public void setSelectPosition(int selectPosition){
        this.selPos = selectPosition;

    }

    public static class ExamViewHolder extends BaseRecyclerViewAdapter.ViewHolder<item_exam,ExamScoreBean>{

        public ExamViewHolder(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }

}
