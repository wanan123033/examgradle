package com.fairplay.examgradle.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Student;
import com.fairplay.examgradle.BuildConfig;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.setting.SettingHelper;
import com.fairplay.examgradle.task.DataBaseRespon;
import com.fairplay.examgradle.task.DataBaseTask;
import com.fairplay.examgradle.utils.ToastUtils;
import com.gwm.mvvm.BaseViewModel;
import com.gwm.util.ContextUtil;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.utils.LogUtils;
import com.ww.fpl.libarcface.common.Constants;
import com.ww.fpl.libarcface.faceserver.FaceServer;
import com.ww.fpl.libarcface.model.FaceRegisterInfo;
import com.ww.fpl.libarcface.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.StringUtils.getString;


public class SplashViewModel extends BaseViewModel<Object> {
    public static final String LOG_PATH_NAME = "examgradle";
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    @Override
    protected void onResume(LifecycleOwner owner) {
        super.onResume(owner);
        init();
        Log.e("TAG","SplashViewModel onResume");
    }
    private void init(){
        boolean isEngine = ConfigUtil.getISEngine(ContextUtil.get());
        if (isEngine) {
            initLocalFace();
        } else {
            activeEngine();
        }
        LogUtils.initLogger(BuildConfig.DEBUG, BuildConfig.DEBUG, LOG_PATH_NAME);
        ToastUtils.init(ContextUtil.get());
    }
    private void initLocalFace() {
        //本地人脸库初始化
        boolean isFaceInit = FaceServer.getInstance().init(ContextUtil.get());
        Logger.d("initLocalFace====>" + isFaceInit);
        if (SettingHelper.getSystemSetting().getCheckTool() == 4) {
            runThreadRunnable(new DataBaseTask(ContextUtil.get(), "数据加载中...", true) {
                @Override
                public DataBaseRespon executeOper() {
                    List<Student> studentList = DBManager.getInstance().queryStudentFeatures();
                    Log.i("faceRegisterInfoList", "->" + studentList.size());
                    List<FaceRegisterInfo> registerInfoList = new ArrayList<>();
                    for (Student student : studentList) {
                        registerInfoList.add(new FaceRegisterInfo(Base64.decode(student.getFaceFeature(), Base64.DEFAULT), student.getStudentCode()));
                    }
                    FaceServer.getInstance().addFaceList(registerInfoList);
                    return new DataBaseRespon(true, "", null);
                }

                @Override
                public void onExecuteSuccess(DataBaseRespon respon) {

                }

                @Override
                public void onExecuteFail(DataBaseRespon respon) {

                }
            });
        }
    }
    /**
     * 激活人脸识别引擎
     */
    public void activeEngine() {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) owner, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = FaceEngine.activeOnline(ContextUtil.get(), Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            ToastUtils.showShort(getString(R.string.active_success));

                            ConfigUtil.setISEngine(ContextUtil.get(), true);
                            //本地人脸库初始化
                            initLocalFace();
                            return;
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
//                            ToastUtils.showShort(getString(R.string.already_activated));
                        } else {
                            ToastUtils.showShort(getString(R.string.active_failed));
                        }
//
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(ContextUtil.get(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }
}
