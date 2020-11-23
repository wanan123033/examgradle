package com.fairplay.examgradle.httppresenter;


import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.ItemInfoBean;
import com.fairplay.examgradle.contract.Unit;
import com.geek.thread.GeekThreadPools;
import com.gwm.base.BaseActivity;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.retrofit.Observable;

public class DownItemInfoPresenter extends JsonDataPresenter<DownItemInfoPresenter.DownItemInfo,ItemInfoBean> {
    public DownItemInfoPresenter() {
        super(DownItemInfo.class);
    }

    @Override
    protected void onNextResult(ItemInfoBean response, int id) {
        GeekThreadPools.executeWithGeekThreadPool(new Runnable() {
            @Override
            public void run() {
                if (response.data != null && !response.data.isEmpty()){
                    for (int i = 0 ; i < response.data.size() ; i++){  //父项信息

                        ItemInfoBean.ItemInfo info = response.data.get(i);
                        Item item = new Item();
                        item.setItemName(info.itemName);
                        //父项信息 子项项目代码=父项项目代码
                        item.setItemCode(info.examItemCode);
                        item.setSubitemCode(info.examItemCode);
                        item.setUnit(info.resultUnit);
                        item.setItemType(info.itemType);
                        item.setTestType(info.testType);
                        item.setTestNum(info.resultTestNum);
                        item.setRoundNum(info.itemRoundNum);
                        item.setDigital(info.decimalDigits);
                        item.setCarryMode(info.carryMode);
                        item.setScoreCountMode(info.scoreCountMode);
                        item.setScoreCountRule(info.scoreCountRule);
                        item.setScoreCarryMode(info.scoreCarryMode);
                        item.setScoreCarryByHundredth(info.scoreCarryByHundredth);
                        item.setMinValue(info.minResult);
                        item.setMaxValue(info.maxResult);
                        item.setLastResultMode(info.lastResultMode);
                        item.setMachineCode(info.machineCode);
                        item.setItemProperty(info.itemProperty);
                        item.setExmItemType(info.exmItemType);
                        item.setLimitGender(info.limitGender);
                        item.setMinScore(info.minScore);
                        item.setRatio(info.ratio);
                        item.setIsLowestPoint(info.isLowestPoint);
                        if (item.getUnit() != null)
                            item.setRemark1(Unit.getUnit(item.getUnit().trim()).getDescription());
                        if (TextUtils.isEmpty(item.getRemark1())){
                            if (item.getDigital() == 0){
                                item.setRemark1("整数值");
                            }else if (item.getDigital() == 1){
                                item.setRemark1("0.0");
                            }else if (item.getDigital() == 2){
                                item.setRemark1("0.00");
                            }
                        }
                        DBManager.getInstance().insertItem(item);
                        if (info.subitemList != null && !info.subitemList.isEmpty()){
                            for (int j = 0 ; j < info.subitemList.size() ; j++){          //子项信息
                                ItemInfoBean.SubItem subInfo = info.subitemList.get(j);
                                Item subItem = new Item();
                                subItem.setItemName(subInfo.subitemName);
                                //子项信息 父项项目代码对应多个子项项目代码
                                subItem.setItemCode(item.getItemCode());
                                subItem.setSubitemCode(subInfo.subitemCode);
                                subItem.setItemType(subInfo.subitemType);
                                subItem.setTestType(subInfo.subitemTestType);
                                subItem.setUnit(subInfo.subitemUnit);
                                subItem.setTestNum(subInfo.subitemTestNum);
                                subItem.setRoundNum(subInfo.subitemRoundNum);
//                        subItem.setRatio(subInfo.scoreMultiple);
                                subItem.setMarkScore(subInfo.markScore);
                                if (subInfo.markScore == 0) {   //测量项目
                                    subItem.setDigital(subInfo.decimalDigits);
                                }else if (subInfo.markScore == 1){  //打分项目
                                    subItem.setDigital(subInfo.scoreDecimalDigits);
                                }
                                subItem.setCarryMode(subInfo.carryMode);
                                subItem.setScoreCountMode(subInfo.scoreCountMode);
                                subItem.setScoreCountRule(subInfo.scoreCountRule);
                                subItem.setScoreCarryMode(subInfo.scoreCarryMode);
                                subItem.setScoreCarryByHundredth(subInfo.scoreCarryByHundredth);
                                subItem.setScoreCount(subInfo.scoreCount);
                                subItem.setCalScoreType(subInfo.calScoreType);
                                subItem.setEnableTempGroup(subInfo.enableTempGroup);
                                subItem.setEnableTempGroupRight(subInfo.enableTempGroupRight);
                                subItem.setMinValue(subInfo.minResult);
                                subItem.setMaxValue(subInfo.maxResult);
                                subItem.setMachineCode(subInfo.machineCode);
                                subItem.setMinScore(subInfo.minScore);
                                subItem.setRatio(subInfo.ratio);
                                subItem.setIsLowestPoint(subInfo.isLowestPoint);
                                if (subItem.getUnit() != null)
                                    subItem.setRemark1(Unit.getUnit(subItem.getUnit().trim()).getDescription());
                                if (TextUtils.isEmpty(subItem.getRemark1())){
                                    if (subItem.getDigital() == 0){
                                        subItem.setRemark1("整数值");
                                    }else if (subItem.getDigital() == 1){
                                        subItem.setRemark1("0.0");
                                    }else if (subItem.getDigital() == 2){
                                        subItem.setRemark1("0.00");
                                    }
                                }
                                Log.e("TAG===>",subItem.toString());
                                long subItemId = DBManager.getInstance().insertItem(subItem);
                                if (subInfo.multipleValueSetting != null){
                                    if (subInfo.multipleValueSetting.valueList != null && !subInfo.multipleValueSetting.valueList.isEmpty()){
                                        for (int m = 0 ; m < subInfo.multipleValueSetting.valueList.size() ; m++){
                                            ItemInfoBean.Value value = subInfo.multipleValueSetting.valueList.get(m);
                                            MultipleItem multipleItem = new MultipleItem();
                                            multipleItem.setItemId(subItemId);
                                            multipleItem.setOrder(value.order);
                                            multipleItem.setDesc(value.desc);
                                            multipleItem.setGroup(value.group);
                                            multipleItem.setScore(value.score);
                                            multipleItem.setMachineScore(value.machineScore);
                                            DBManager.getInstance().insertMultipItem(multipleItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ToastUtils.showShort("数据下载成功");
                }else {
//                    ToastUtils.showShort("数据下载失败");
                }
                ((BaseViewModel)getViewModel()).sendLiveData(BaseActivity.DIMMSION_PROGREESS);
            }
        });

    }

    /**
     * 下载考点项目
     */
    public void downItem() {
        String json = genJsonString(100020111,"");
        String token = getToken();
        Observable<ItemInfoBean> itemInfoBeanObservable = getHttpPresenter().downItemInfo(token, json);
        addHttpSubscriber(itemInfoBeanObservable,ItemInfoBean.class);
    }

    public interface DownItemInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
