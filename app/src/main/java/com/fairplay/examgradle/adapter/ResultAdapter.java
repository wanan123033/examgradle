package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.item_result;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.activity.DataDisplayActivity;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_result)
public class ResultAdapter extends BaseRecyclerViewAdapter<ResultAdapter.ViewHodel, item_result,RoundResult> {
    public ResultAdapter(Context context, List<RoundResult> roundResults) {
        super(context,roundResults);
    }

    @Override
    protected void setData(RoundResult roundResult, int position) {
        mBinding.tv_round.setText(roundResult.getRoundNo()+"");
        mBinding.tv_beginTime.setText(roundResult.getTestTime());
        mBinding.tv_score.setText(roundResult.getScore()+"");
        mBinding.tv_result.setText(roundResult.getResult());
        mBinding.tv_status.setText(roundResult.getExamType() == 0 ? "正常":"补考");
    }

    @Override
    protected ViewHodel getViewHolder(View itemView) {
        return new ViewHodel(this,itemView);
    }

    static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_result,RoundResult>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
