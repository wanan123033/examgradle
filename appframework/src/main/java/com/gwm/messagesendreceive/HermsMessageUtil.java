package com.gwm.messagesendreceive;

import java.util.List;

public interface HermsMessageUtil {
    List<Class<? extends HermsMessageBusService>> getHersMessageServices();
}
