package com.gwm.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by John on 2016/3/24.
 */
public class BaseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        BaseApplication.getInstance().getReceivers().add(this);
    }
}
