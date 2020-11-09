package com.gwm.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gwm.annotation.layout.Layout;
import com.gwm.inter.IViewBind;
import com.gwm.layout.LayoutEventUtil;
import com.gwm.messagesendreceive.MessageBus;

import java.lang.ref.SoftReference;

/**
 * AndroidX fragment懒加载是通过Adapter控制的  跟fragment无关  只需要将数据加载放在onResume()里面即可实现懒加载 同时取消加载放在与之对应的onPause()上
 * 		1.配合ViewPager 用 FragmentStatePagerAdapter(fragmentManager, Behavior.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
 * 		2.在 add() show() replace()等时调用setMaxLifecycle(fragment,Lifecycle.State.STARTED)即可
 * 					显示Fragment,并设置其最大Lifecycle为Lifecycle.State.RESUMED------|
 * 																				 |------> 通过FragmentTransaction调用
 * 				    隐藏Fragment,并设置最大Lifecycle为Lifecycle.State.STARTED------|
 *
 * AndroidX 中:
 * 		Behavior.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT    表示不会调用setUserVisibleHint()方法
 * 				 BEHAVIOR_SET_USER_VISIBLE_HINT           表示会调用setUserVisibleHint()方法
 *
 * 	通过上述说明,我特意丢弃原框架的Fragment懒加载的基类  因为不需要了  在新的androidx中懒加载是写在onResume上的  不需要我就舍弃了
 * @param <V>
 */
public abstract class BaseFragment<V extends IViewBind> extends Fragment implements ViewClick {
	protected FragmentManager manager;
    protected View view;
    protected V mBinding;
	protected Handler handler;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		handler = new SoftReference<>(new MyHandler(this)).get();
		manager = getChildFragmentManager();
		MessageBus.getBus().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		Layout layout = getClass().getAnnotation(Layout.class);
		view = inflate(getContext(),layout.value());
        return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mBinding = getViewBind();
		mBinding.bindView(view);
		LayoutEventUtil.getInstance().bind(this,view);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MessageBus.getBus().unregister(this);
	}



	@Nullable
	@Override
	public View getView() {
		return view;
	}

	public Context getContext(){
		return BaseApplication.getInstance().getApplicationContext();
	}

	public V getViewBind(){
		return BaseApplication.getInstance().getLayoutUtil().getViewBind(getClass().getAnnotation(Layout.class).value());
	}
	private static View inflate(Context context, int resId) {
		View view = LayoutInflater.from(context).inflate(resId, null,false);
		return view;
	}
	public void handlerMessage(Message message){

	}
	private static final class MyHandler extends Handler{
		private SoftReference<? extends BaseFragment> fragment;
		public MyHandler(BaseFragment fragment){
			super(Looper.getMainLooper());
			this.fragment = new SoftReference<>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			fragment.get().handlerMessage(msg);
		}
	}
}
