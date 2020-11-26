package com.fairplay.examgradle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.examgradle.R;

import java.util.List;

public class StudentResultAdapter extends RecyclerView.Adapter<StudentResultAdapter.StudentViewHolder> {
    private List<DataRtiveBean> list;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    public StudentResultAdapter(List<DataRtiveBean> list, Context context) {
        this.list = list;
        this.context = context;
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_retieve, parent, false);
        return new StudentViewHolder(this,view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.setData(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView tv_stuCode,tv_stuName,tv_sex,tv_score,tv_fraction;
        CheckBox cb_select;
        StudentResultAdapter adapter;

        public StudentViewHolder(StudentResultAdapter adapter,@NonNull View itemView) {
            super(itemView);
            this.adapter = adapter;
            tv_stuCode = itemView.findViewById(R.id.tv_stuCode);
            tv_stuName = itemView.findViewById(R.id.tv_stuName);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_score = itemView.findViewById(R.id.tv_score);
            tv_fraction = itemView.findViewById(R.id.tv_fraction);
            cb_select = itemView.findViewById(R.id.cb_select);
        }

        public void setData(DataRtiveBean dataRtiveBean, int position) {
            tv_stuCode.setText(dataRtiveBean.studentCode);
            tv_stuName.setText(dataRtiveBean.itemName);
            tv_sex.setText(dataRtiveBean.examPlaceName);
            tv_score.setText(dataRtiveBean.result);
            tv_fraction.setText(dataRtiveBean.score);
            cb_select.setChecked(dataRtiveBean.isSelected);
            itemView.setOnClickListener(v -> adapter.onItemClickListener.onItemClick(null,itemView,position,0));
            cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                dataRtiveBean.isSelected = isChecked;
            });
        }
    }
}
