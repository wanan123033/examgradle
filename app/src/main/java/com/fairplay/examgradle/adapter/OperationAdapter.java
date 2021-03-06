package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.layout.item_operation;
import com.bumptech.glide.Glide;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.bean.OperationBean;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseRecyclerViewAdapter;

import java.util.List;

@Layout(R.layout.item_operation)
public class OperationAdapter extends BaseRecyclerViewAdapter<OperationAdapter.ViewHodel, item_operation, OperationBean> {
    public OperationAdapter(Context context, List<OperationBean> data) {
        super(context, data);
    }

    @Override
    protected void setData(OperationBean operationBean, int position) {
        Log.e("TAG===",operationBean.toString());
        mBinding.iv_type_image.setBackgroundResource(operationBean.res);
        mBinding.tv_item.setText(operationBean.name);
    }

    @Override
    protected ViewHodel getViewHolder(View itemView) {
        return new ViewHodel(this,itemView);
    }

    public static class ViewHodel extends BaseRecyclerViewAdapter.ViewHolder<item_operation,OperationBean>{

        public ViewHodel(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(adapter, itemView);
        }
    }
}
