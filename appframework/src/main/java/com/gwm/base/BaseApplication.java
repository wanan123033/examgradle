package com.gwm.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import androidx.multidex.MultiDexApplication;

import com.gwm.http.HttpClients;
import com.gwm.layout.LayoutEventUtil;
import com.gwm.messagesendreceive.HermsMessageBusService;
import com.gwm.messagesendreceive.HermsMessageUtil;
import com.gwm.util.ContextUtil;
import com.gwm.util.LayoutInflaterUtil;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 做了一些处理的Application的类，为了维持该框架的运行特意写的一个类
 * 集成了前端运行日志的采集
 * @author gwm
 */
public abstract class BaseApplication extends MultiDexApplication {

	private static BaseApplication instance;
	private List<String> firstToasts;
	private LayoutInflaterUtil inflaterUtil;
	private HttpClients clients;
	private HermsMessageUtil hermsMessageUtil;
	private List<Activity> activities;

	private MMKV mmkv;

	/**
	 * 获取该类的实例
	 * @return
	 */
	public static <T extends BaseApplication> T getInstance(){
		return (T) instance;
	}



	@Override
	public void onCreate(){
		super.onCreate();
		if(instance == null){
			instance = this;
		}
		firstToasts = new ArrayList<>();
		activities = new LinkedList<>();
		ContextUtil.setGlobalContext(getApplicationContext());
		MMKV.initialize(getApplicationContext());
		mmkv = MMKV.defaultMMKV();
		inflaterUtil = getLayoutUtil();
//		HermsMessageBus.init(this);
    }
	/**
	 * 退出该应用程序
	 */
	public void exit(){
		firstToasts.clear();
		inflaterUtil.clear();
		LayoutEventUtil.getInstance().clear();
		MMKV.onExit();
		for (Activity activity : activities){
			if (!activity.isFinishing()){
				activity.finish();
			}
		}
		activities.clear();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 获取跨进程通讯的服务集
	 * @return
	 */
	public List<Class<? extends HermsMessageBusService>> getHersMessageServices(){
		return getHermsUtil().getHersMessageServices();
	}

	public List<String> getFisrstToasts() {
		return firstToasts;
	}
	public synchronized HermsMessageUtil getHermsUtil(){
		try {
			if (hermsMessageUtil == null){
				hermsMessageUtil = (HermsMessageUtil) Class.forName("com.app.bus.HermsMessageUtils").newInstance();
			}
			return hermsMessageUtil;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	protected synchronized LayoutInflaterUtil getLayoutUtil(){
		try {
			if (inflaterUtil == null){
				inflaterUtil = (LayoutInflaterUtil) Class.forName("com.app.layout.LayoutInflaterUtils").newInstance();
			}
			return inflaterUtil;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized HttpClients getHttpClients(){
		try {
			if (clients == null){
				clients = (HttpClients) Class.forName("com.app.http.HttpClients").newInstance();
			}
			return clients;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public MMKV getMmkv() {
		return mmkv;
	}

	public List<Activity> getActivities() {
		return activities;
	}
}