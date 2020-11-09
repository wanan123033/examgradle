package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.layout.item_score_one;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.activity.ExamScoreActivity;
import com.fairplay.examgradle.bean.ScoreBean;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_score_one)
public class OneScoreAdapter extends BaseRecyclerViewAdapter<OneScoreAdapter.ViewHodel,item_score_one, ScoreBean> {

    private int selPos = 0;

    public OneScoreAdapter(Context context, List<ScoreBean> data) {
        super(context, data);
    }

    @Override
    protected void setData(ScoreBean scoreBean, final int position) {
        mBinding.tv_pos.setText((position+1)+"");
        mBinding.tv_score.setText(scoreBean.result1.toString());
        if (selPos == position){
            mBinding.tv_score.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else {
            mBinding.tv_score.setBackgroundResource(R.drawable.score_bg);
        }
        if (scoreBean.isLook){
            mBinding.iv_lock.setVisibility(View.VISIBLE);
        }else {
            mBinding.iv_lock.setVisibility(View.GONE);
        }
    }

    @Override
    protected ViewHodel getViewHolder(View itemView) {
        return new ViewHodel(this,itemView);
    }

    public void setSelectPosition(int position) {
        this.selPos = position;
    }

    public int getSelectPosition(){
        return selPos;
    }

    static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_score_one,ScoreBean>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
