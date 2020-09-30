package com.gwm.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwm.annotation.layout.Layout;
import com.gwm.inter.IViewBind;
import com.gwm.layout.LayoutEventUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * OnClick类似的注解不能用在ViewHolder上,要使用在Adapter上
 * @param <VH>
 * @param <D>
 */
public abstract class BaseRecyclerViewAdapter<VH extends BaseRecyclerViewAdapter.ViewHolder,V extends IViewBind,D> extends RecyclerView.Adapter<VH> {
    private Context context;
    public List<D> data;
    public AdapterView.OnItemClickListener onItemClickListener;
    private View view;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    protected V mBinding;

    public BaseRecyclerViewAdapter(Context context, List<D> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Layout layout = getClass().getAnnotation(Layout.class);
        int value = layout.value();
        view = LayoutInflater.from(context).inflate(value, viewGroup, false);
        VH viewHolder = getViewHolder(view);
        viewHolder.setlayoutId(value);
        viewHolder.init();
        mBinding = (V) viewHolder.mBinding;
        return viewHolder;
    }
    protected abstract void setData(D d,int position);

    protected abstract VH getViewHolder(View itemView);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setData(getItem(position),position);
    }

    @Override
    public int getItemCount() {
        if (data != null && !data.isEmpty()){
            return data.size();
        }
        return 0;
    }

    public void clearData() {
        if (data != null){
            data.clear();
        }
        notifyDataSetChanged();
    }

    public void addData(List<D> rows) {
        if (data == null){
            data = new ArrayList<>();
        }
        data.addAll(rows);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public D getItem(int position) {
        if (data != null && !data.isEmpty()){
            return data.get(position);
        }
        return null;
    }

    public static abstract class ViewHolder<V extends IViewBind,D> extends RecyclerView.ViewHolder{
        protected BaseRecyclerViewAdapter adapter;
        public V mBinding;
        private int layoutId;

        public ViewHolder(BaseRecyclerViewAdapter adapter, @NonNull View itemView) {
            super(itemView);
            this.adapter = adapter;
        }

        public void init(){
            mBinding = getViewBind();
            mBinding.bindView(itemView);
            LayoutEventUtil.getInstance().bind(this.adapter,itemView);
        }

        protected V getViewBind(){
            return BaseApplication.getInstance().getLayoutUtil().getViewBind(layoutId);
        }

        public void setData(D d, final int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter != null && adapter.onItemClickListener != null)
                        adapter.onItemClickListener.onItemClick(null,itemView,position,0);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (adapter != null && adapter.onItemLongClickListener != null)
                        return adapter.onItemLongClickListener.onItemLongClick(null,itemView,position,0);
                    return false;
                }
            });
            adapter.setData(d,position);
        }

        public void setlayoutId(int value) {
            this.layoutId = value;
        }
    }


}
