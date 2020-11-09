package com.gwm.messagesendreceive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.gwm.HermsMessageAidl;
public class HermsMessageBusService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private static IBinder binder = new HermsMessageAidl.Stub() {
        @Override
        public void postAll(HermsMessage obj) {
            MessageBusMessage msg = new MessageBusMessage(obj);
            MessageBus.getBus().postAll(msg);
        }

        @Override
        public void post(HermsMessage obj) {
            MessageBusMessage msg = new MessageBusMessage(obj);
            MessageBus.getBus().post(msg);
        }
    };

}
