package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;

import com.app.layout.activity_item_init;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.contract.MMKVContract;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseApplication;
import com.tencent.mmkv.MMKV;

@Layout(R.layout.activity_item_init)
public class ItemInitActivity extends BaseActivity<activity_item_init> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @OnClick(R.id.btn_insert)
    public void onClick(View view){
        String examPlaceName = mBinding.ed_ExamPlaceName.getText().toString();
        String groupNo = mBinding.et_GroupNo.getText().toString();
        String groupType = mBinding.et_GroupType.getText().toString();
        String sortName = mBinding.et_SortName.getText().toString();
        String trackNo = mBinding.et_trackNo.getText().toString();

        MMKV mmkv = BaseApplication.getInstance().getMmkv();
        mmkv.putString(MMKVContract.EXAMPLACENAME,examPlaceName);
        mmkv.putString(MMKVContract.GROUPNO,groupNo);
        mmkv.putInt(MMKVContract.GROUPTYPE,Integer.parseInt(groupType));
        mmkv.putString(MMKVContract.SORTNAME,sortName);
        mmkv.putString(MMKVContract.TRACKNO,trackNo);
    }
}
