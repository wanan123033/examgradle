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
import com.fairplay.examgradle.utils.DataUtil;
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
        mBinding.tv_beginTime.setText(DataUtil.getDataFormat(roundResult.getTestTime()));
        mBinding.tv_score.setText(roundResult.getScore());
        mBinding.tv_result.setText(roundResult.getResult());
        mBinding.tv_status.setText(roundResult.getExamType() == 0 ? "正常":"补考");
        if (roundResult.getIsMultioleResult() == 1){
            List<MultipleResult> multioleResult = DBManager.getInstance().getMultioleResult(roundResult.getId());
            StringBuffer resultbuffer = new StringBuffer();
            StringBuffer scorebuffer = new StringBuffer();
            for (MultipleResult result : multioleResult){
                if (!TextUtils.isEmpty(result.getResult()))
                    resultbuffer.append(result.getDesc()+":"+result.getResult()+"/");
                if (!TextUtils.isEmpty(result.getScore()))
                    scorebuffer.append(result.getDesc()+":"+result.getScore()+"/");
            }
            if (resultbuffer.length() > 1)
                resultbuffer.deleteCharAt(resultbuffer.length() - 1);
            if (scorebuffer.length() > 1)
                scorebuffer.deleteCharAt(scorebuffer.length() - 1);
            mBinding.tv_result.setText(resultbuffer.toString().replaceAll("null","0"));
            mBinding.tv_score.setText(scorebuffer.toString().replaceAll("null","0"));
        }
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
