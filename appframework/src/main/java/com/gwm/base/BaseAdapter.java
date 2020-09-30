package com.gwm.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwm.annotation.layout.Layout;
import com.gwm.inter.IViewBind;
import com.gwm.layout.LayoutEventUtil;
import com.gwm.util.ContextUtil;

import java.util.List;
/**
 * ViewHolder 需要使用Layout注解才能生成与之对应的Event
 * @param <T>
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    private List<T> data;
    private Context context;

    public BaseAdapter(List<T> data){
        this.context = ContextUtil.get();
        this.data = data;
    }

    public List<T> getData(){
        return data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            int layout = getClass().getAnnotation(Layout.class).value();
            convertView = LayoutInflater.from(context).inflate(layout,parent,false);
            holder = getViewHolder(convertView);
            holder.mBinding = getViewBind(layout);
            holder.mBinding.bindView(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        LayoutEventUtil.getInstance().bind(holder,convertView);
        holder.initData(getItem(position),position);
        return convertView;
    }

    private IViewBind getViewBind(int layout) {
        return BaseApplication.getInstance().getLayoutUtil().getViewBind(layout);
    }

    protected abstract ViewHolder getViewHolder(View convertView);

    public abstract static class ViewHolder<T,V extends IViewBind>{
        protected V mBinding;
        protected Context context;
        public ViewHolder(View itemView){
            context = itemView.getContext();
        }
        public abstract void initData(T item,int position);
    }
}
