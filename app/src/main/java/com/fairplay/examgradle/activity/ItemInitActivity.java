package com.fairplay.examgradle.activity;

import android.os.Bundle;
import android.view.View;

import com.app.layout.activity_item_init;
import com.fairplay.database.DBManager;
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
        }else if (radioButtonId == R.id.tv_testType_3){
            item.setTestType(3);
        }else if (radioButtonId == R.id.tv_testType_4){
            item.setTestType(4);
        }
        item.setTestNum(Integer.parseInt(mBinding.tv_testNum.getText().toString()));
        item.setDigital(Integer.parseInt(mBinding.tv_digital.getText().toString()));
        radioButtonId = mBinding.tv_carryMode.getCheckedRadioButtonId();
        if (radioButtonId == R.id.tv_carryMode_1){
            item.setCarryMode(1);
        }else if (radioButtonId == R.id.tv_carryMode_2){
            item.setCarryMode(2);
        }else if (radioButtonId == R.id.tv_carryMode_3){
            item.setCarryMode(3);
        }else if (radioButtonId == R.id.tv_carryMode_4){
            item.setCarryMode(4);
        }
        item.setMinValue(Integer.parseInt(mBinding.tv_minValue.getText().toString()));
        item.setMaxValue(Integer.parseInt(mBinding.tv_maxValue.getText().toString()));
        radioButtonId = mBinding.tv_lastResultMode.getCheckedRadioButtonId();
        if (radioButtonId == R.id.tv_lastResultMode_1){
            item.setLastResultMode(1);
        }else if (radioButtonId == R.id.tv_lastResultMode_2){
            item.setLastResultMode(2);
        }else if (radioButtonId == R.id.tv_lastResultMode_3){
            item.setLastResultMode(3);
        }
        item.setMinScore(Integer.parseInt(mBinding.tv_minScore.getText().toString()));
        item.setRatio(Double.parseDouble(mBinding.tv_ratio.getText().toString()));
        radioButtonId = mBinding.tv_markScore.getCheckedRadioButtonId();
        if (radioButtonId == R.id.tv_markScore_0){
            item.setMarkScore(0);
        }else if (radioButtonId == R.id.tv_markScore_1){
            item.setMarkScore(1);
        }
        item.setScoreCount(Integer.parseInt(mBinding.tv_scoreCount.getText().toString()));

        DBManager.getInstance().insertItem(item);
    }
}
