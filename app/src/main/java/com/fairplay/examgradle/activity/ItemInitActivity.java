package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;

import com.app.layout.activity_item_init;
import com.fairplay.database.entity.Item;
import com.fairplay.examgradle.R;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.base.BaseActivity;
@Layout(R.layout.activity_item_init)
public class ItemInitActivity extends BaseActivity<activity_item_init> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @OnClick(R.id.tv_insert)
    public void onClick(View view){
        Item item = new Item();
        item.setItemName(mBinding.tv_itemName.getText().toString().trim());
        item.setSubitemCode(mBinding.tv_itemCode.getText().toString().trim());
        item.setItemCode(mBinding.tv_itemCode.getText().toString().trim());
        item.setMachineCode(mBinding.tv_itemCode.getText().toString().trim());
        item.setUnit(mBinding.tv_unit.getText().toString().trim());
        int radioButtonId = mBinding.tv_testType.getCheckedRadioButtonId();
        if (radioButtonId == R.id.tv_testType_1){
            item.setTestType(1);
        }else if (radioButtonId == R.id.tv_testType_2){
            item.setTestType(2);
        }

    }
}
