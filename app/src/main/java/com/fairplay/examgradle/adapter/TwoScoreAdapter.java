package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.layout.item_score_two;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.activity.ExamScoreActivity;
import com.fairplay.examgradle.bean.ScoreBean;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_score_two)
public class TwoScoreAdapter extends BaseRecyclerViewAdapter<TwoScoreAdapter.ViewHodel,item_score_two, ScoreBean> {
    private int selPos;

    public TwoScoreAdapter(Context context, List<ScoreBean> data) {
        super(context, data);
    }

    @Override
    protected void setData(final ScoreBean scoreBean, final int position) {
        if (selPos == position) {
            if (!scoreBean.twoPos){
                mBinding.tv_score1.setBackgroundColor(context.getResources().getColor(R.color.green));
                mBinding.tv_score2.setBackgroundResource(R.drawable.score_bg);
            }else {
                mBinding.tv_score2.setBackgroundColor(context.getResources().getColor(R.color.green));
                mBinding.tv_score1.setBackgroundResource(R.drawable.score_bg);
            }
        }
        mBinding.tv_score1.setText(scoreBean.result1.toString());
        mBinding.tv_score2.setText(scoreBean.result2.toString());
        mBinding.tv_score1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ExamScoreActivity){
                    ((ExamScoreActivity)context).scoreOne(position,scoreBean);
                }
            }
        });
        mBinding.tv_score2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExamScoreActivity)context).scoreTwo(position,scoreBean);
            }
        });
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

    public int getSelectPosition() {
        return selPos;
    }

    static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_score_two,ScoreBean>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
