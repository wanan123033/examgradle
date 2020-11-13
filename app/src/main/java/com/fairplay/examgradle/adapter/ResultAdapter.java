package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.layout.item_result;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.activity.DataDisplayActivity;
import com.fairplay.examgradle.bean.ScoreBean;
import com.fairplay.examgradle.utils.DataUtil;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_result)
public class ResultAdapter extends BaseRecyclerViewAdapter<ResultAdapter.ViewHodel, item_result, ScoreBean> {
    public ResultAdapter(Context context, List<ScoreBean> roundResults) {
        super(context,roundResults);
    }

    @Override
    protected void setData(ScoreBean roundResult, int position) {
        mBinding.tv_round.setText(roundResult.roundNo+"");
        mBinding.tv_status.setText(roundResult.examStatus == 0 ? "正常":"补考");
        mBinding.tv_result.setText(roundResult.result);
        mBinding.tv_score.setText(roundResult.score);
        mBinding.tv_beginTime.setText(roundResult.testTime);
    }

    @Override
    protected ViewHodel getViewHolder(View itemView) {
        return new ViewHodel(this,itemView);
    }

    static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_result,ScoreBean>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
