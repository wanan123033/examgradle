package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.layout.item_score;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.ExamScoreBean;
import com.fairplay.examgradle.contract.MBMContract;
import com.fairplay.examgradle.contract.Unit;
import com.fairplay.examgradle.viewmodel.ExamResultModel;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;
import com.gwm.messagesendreceive.MessageBus;
import com.gwm.messagesendreceive.MessageBusMessage;

import java.util.List;

@Layout(R.layout.item_score)
public class ScoreAdapter extends BaseRecyclerViewAdapter<ScoreAdapter.ScoreViewHodel, item_score,ExamScoreBean.Score> {
    private int currentPos;
    private boolean show;
    private int roundNo;

    public ScoreAdapter(Context context, List<ExamScoreBean.Score> resultList) {
        super(context,resultList);
    }

    @Override
    protected void setData(final ExamScoreBean.Score score, int position) {
        Log.e("TAG====<",score.toString());
        mBinding.tv_desc.setText(score.desc);
        mBinding.tv_result.setText(score.result.toString());
        mBinding.tv_unit.setText(Unit.getUnit(score.unit).getDesc());
//        mBinding.tv_result.setHint("数据格式: " + Unit.getUnit(score.unit).getDescription());
        if (currentPos == position && show){
            mBinding.tv_result.setBackgroundResource(R.drawable.score_blue_bg);
        }else {
            mBinding.tv_result.setBackgroundResource(R.drawable.score_bg);
        }
        if (score.isLock){
            mBinding.iv_lock.setVisibility(View.VISIBLE);
        }else {
            mBinding.iv_lock.setVisibility(View.GONE);
        }

        mBinding.tv_result.setOnClickListener(v -> {
            if (!score.isLock){
                Bundle bundle = new Bundle();
                bundle.putInt("currentRound",roundNo);
                bundle.putInt("currentPos",position);
               MessageBus.getBus().post(new MessageBusMessage(bundle,MBMContract.UN_LOCK));
            }
        });
    }

    @Override
    protected ScoreViewHodel getViewHolder(View itemView) {
        return new ScoreViewHodel(this,itemView);
    }

    public void setSelectPosition(int currentPosition) {
        this.currentPos = currentPosition;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setCurrentRound(int roundNo) {
        this.roundNo = roundNo;
    }

    static class ScoreViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_score,ExamScoreBean.Score>{

        public ScoreViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
