package com.fairplay.examgradle;

import android.content.Context;

import androidx.annotation.NonNull;

import com.fairplay.database.DBManager;
import com.feipulai.common.db.ClearDataProcess;

/**
 * Created by James on 2018/1/4 0004.
 * 深圳市菲普莱体育发展有限公司   秘密级别:绝密
 */
public class DBDataCleaner extends ClearDataProcess {
	
	
	public DBDataCleaner(@NonNull Context context, int clearType, OnProcessFinishedListener listener) {
		super(context, clearType, listener);
	}
	
	@Override
	protected int getUnUploadNum() {
		return DBManager.getInstance().getUnUploadNum();
	}
	
}
