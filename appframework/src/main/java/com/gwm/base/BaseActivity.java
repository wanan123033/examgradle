package com.gwm.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.gwm.R;
import com.gwm.annotation.Permission;
import com.gwm.annotation.layout.Layout;
import com.gwm.inter.IViewBind;
import com.gwm.layout.LayoutEventUtil;
import com.gwm.messagesendreceive.MessageBus;
import com.gwm.view.CustomDialog;

import java.lang.ref.SoftReference;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 该类加入以下功能：
 * 1."再按一次退出程序"提示(只需要调用addFirstToast()方法把activity添加到退出通知提示的集合即可)
 * 2.会自动根据需要是否隐藏系统键盘
 * 3.封装了Handler消息处理机制
 * 4.实现了短信消息监听（可以用来做短信验证码的自动填充）
 * 5.支持MessageBus消息站
 * 6.支持Layout注解
 * 7.权限申请管理
 */
public abstract class BaseActivity<V extends IViewBind> extends AppCompatActivity implements ViewClick{
	private static final int PERMISSION_REQUEST_CODE = 666;
	public static final int SHOW_PROGRESS = 444;
	public static final int DIMMSION_PROGREESS = 555;
	private long exitTime = 0;
    private InputMethodManager imm;


	private View view;

	protected V mBinding;

	protected Handler handler;



	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		handler = new SoftReference<>(new MyHandler(this)).get();
		MessageBus.getBus().register(this);
		BaseApplication.getInstance().getActivities().add(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setContentView();
		initPermission();
    }

	private void initPermission() {
		Permission permission = getClass().getAnnotation(Permission.class);
		if (permission != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			ActivityCompat.requestPermissions(this,permission.value(), PERMISSION_REQUEST_CODE);
		}
	}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isFlag = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
					isFlag = true;
                } else {
                	isFlag = false;
                	break;
                }
            }
            if (isFlag){
				onPermissionGranted();
			}else {
            	onPermissionDenied();
			}
        }
    }

	public void onPermissionDenied() {

	}

	public void onPermissionGranted() {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}

	protected void setContentView(){
		Layout layout = getClass().getAnnotation(Layout.class);
		int value = 0;
		if (layout != null) {
			value = layout.value();
		}
		if (value == 0){
			value = getLayout();
		}
		setContentView(value);
		mBinding = getViewBind(value);
		mBinding.bindView(getView());
		LayoutEventUtil.getInstance().bind(this,getView());
    }

	public int getLayout() {
		return 0;
	}


	/**
	 * 将Activity添加到退出通知
	 */

	public void addFirstToast() {
		BaseApplication.getInstance().getFisrstToasts().add(getActivityName());
	}

    public View getView(){
    	if (view == null){
			view = getWindow().getDecorView();
		}
        return view;
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		List<String> activitys = BaseApplication.getInstance().getFisrstToasts();
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if((System.currentTimeMillis() - exitTime) > 2000 && (activitys.indexOf(getActivityName()) != -1)){
				ToastUtils.showShort(R.string.app_add_first);
				exitTime = System.currentTimeMillis();
			} else if ((BaseApplication.getInstance().getFisrstToasts().indexOf(getActivityName()) != -1)) {
				if(getApplication() instanceof BaseApplication){
					((BaseApplication)getApplication()).exit();
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && (activitys.indexOf(getActivityName()) != -1)) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK){
			if(getApplication() instanceof BaseApplication){
				finish();
			}
			return true;
		}
		return onKeyDownMethod(keyCode, event);
	}
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		try {
			super.onConfigurationChanged(newConfig);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				landscape(newConfig);
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				portrait(newConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 横竖屏切换时切换到竖屏时调用
	 * @param newConfig
	 */
	protected void portrait(Configuration newConfig) {

	}

    /**
     * 横竖屏切换时切换到横屏时调用
     * @param newConfig
     */
	protected void landscape(Configuration newConfig) {

	}

	/**
	 * 无需重写onKeyDown()方法,需要时应该重写该方法 已经屏蔽了返回键，如需处理返回键，请重写onKeyDown()方法
	 * @param keyCode
	 * @param event
	 * @return
	 */
	protected boolean onKeyDownMethod(int keyCode, KeyEvent event){
		//如果启用了BasePage类，下面两行代码需要复制在activity的onKeyDown()方法中
//		BaseFragment frag = (BaseFragment) manager.findFragmentByTag("");
//		return frag.getPager().onKeyDownMethod(keyCode,event);

		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 如果启动了BasePager，请将如下代码迁移到BasePager所依附的Activity中
	 * 		但需在此之前调用setCurrentFragment()方法才会有效
	 */
//	public boolean onKeyDownMethod(int keyCode, KeyEvent event){ 
//		return current_Fragment.getCurrentPage().onKeyDownMethod(keyCode, event);
//	}
	/**
	 * 获取当前正在运行的Activity
	 * @return
	 */
	private String getActivityName() {
		return getClass().getSimpleName();
	}

	@Override
	protected void onDestroy() {
		MessageBus.getBus().unregister(this);
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
	}

	/**
	 * 退出当前应用程序
	 */
	public void exit(){
		BaseApplication.getInstance().exit();
	}


	public V getViewBind(@LayoutRes int resId){
		return BaseApplication.getInstance().getLayoutUtil().getViewBind(resId);
	}

	CustomDialog dialog;
	public void handlerMessage(Message message){
		if (message.what == SHOW_PROGRESS){
			if (dialog == null){
				dialog = new CustomDialog(this);
			}
			dialog.setContentText(message.obj.toString());
			if (!isFinishing() && !dialog.isShowing()){
				dialog.show();
			}
		}else if (message.what == DIMMSION_PROGREESS){
			if (dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
		}
	}

	protected void showDialog(String content){
		Message msg = Message.obtain();
		msg.what = SHOW_PROGRESS;
		msg.obj = content;
		handler.sendMessage(msg);
	}
	protected void dismissDialog(){
		Message msg = Message.obtain();
		msg.what = DIMMSION_PROGREESS;
		handler.sendMessage(msg);
	}

	protected static final class MyHandler extends Handler{
		private SoftReference<? extends BaseActivity> activity;
		public MyHandler(BaseActivity activity){
			super(Looper.getMainLooper());
			this.activity = new SoftReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			BaseActivity act = activity.get();
			if (act != null) {
				act.handlerMessage(msg);
			}
		}
	}
}