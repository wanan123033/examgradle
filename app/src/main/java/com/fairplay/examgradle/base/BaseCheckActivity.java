package com.fairplay.examgradle.base;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.setting.SettingHelper;
import com.fairplay.examgradle.setting.SystemSetting;
import com.feipulai.common.tts.TtsManager;
import com.feipulai.common.utils.LogUtil;
import com.feipulai.common.utils.ScannerGunManager;
import com.feipulai.common.utils.ToastUtils;
import com.feipulai.device.CheckDeviceOpener;
import com.feipulai.device.ic.ICCardDealer;
import com.feipulai.device.ic.NFCDevice;
import com.feipulai.device.ic.entity.StuInfo;
import com.gwm.inter.IViewBind;
import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.mvvm.ViewModel;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.utils.LogUtils;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseCheckActivity<M,VM extends ViewModel<M>,V extends IViewBind> extends BaseMvvmTitleActivity<M,VM,V> implements CheckDeviceOpener.OnCheckDeviceArrived, BaseAFRFragment.onAFRCompareListener {
    private View afrFrameLayout;
    private BaseAFRFragment afrFragment;
    private ScannerGunManager scannerGunManager;
    private boolean isOpenDevice = true;
    private static final int STUDENT_CODE = 0x0;
    private static final int ID_CARD_NO = 0x1;
    private static final int CHECK_IN = 0x0;
    private long lastBroadcastTime;
    private boolean needAdd = true;
    private MyHandler mHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
        if (SettingHelper.getSystemSetting().getCheckTool() == 4 && setAFRFrameLayoutResID() != null) {
            afrFrameLayout = setAFRFrameLayoutResID();
            afrFragment = new BaseAFRFragment();
            afrFragment.setCompareListener(this);
        }
        scannerGunManager = new ScannerGunManager(new ScannerGunManager.OnScanListener() {
            @Override
            public void onResult(String code) {
                boolean needAdd = checkQulification(code, STUDENT_CODE);
                if (needAdd) {
                    Student student = new Student();
                    student.setStudentCode(code);
                    showAddHint(student);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        if (isOpenDevice) {
            CheckDeviceOpener.getInstance().setQrLength(SettingHelper.getSystemSetting().getQrLength());
            CheckDeviceOpener.getInstance().setOnCheckDeviceArrived(this);
            int checkTool = SettingHelper.getSystemSetting().getCheckTool();
            CheckDeviceOpener.getInstance().open(this, checkTool == SystemSetting.CHECK_TOOL_IDCARD,
                    checkTool == SystemSetting.CHECK_TOOL_ICCARD,
                    checkTool == SystemSetting.CHECK_TOOL_QR);

        }
        super.onResume();
    }
    @Override
    public void compareStu(final Student student) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (student != null)
                    afrFrameLayout.setVisibility(View.GONE);
            }
        });
        if (student == null) {
            toastSpeak("该考生不存在");
            if (SettingHelper.getSystemSetting().isNetCheckTool()){
                showAddHint(student);
                afrFrameLayout.setVisibility(View.GONE);
            }
            return;
        }
        LogUtil.logDebugMessage("检入考生：" + student.toString());
        StudentItem studentItem = DBManager.getInstance().queryStuItemByStuCode(student.getStudentCode());
        if (studentItem == null) {
            LogUtil.logDebugMessage("检入考生：无此项目");
            toastSpeak( "无此项目");
            return;
        }
        // 可以直接检录
        checkInUIThread(student);
    }

    @Override
    public void onICCardFound(NFCDevice nfcd) {
        long startTime = System.currentTimeMillis();
        ICCardDealer icCardDealer = new ICCardDealer(nfcd);
        StuInfo stuInfo = icCardDealer.IC_ReadStuInfo();

        if (stuInfo == null || TextUtils.isEmpty(stuInfo.getStuCode())) {
//            TtsManager.getInstance().speak("读卡(ka3)失败");
//            InteractUtils.toast(this, "读卡失败");
            toastSpeak(getString(R.string.read_iccard_failed));
            return;
        }

        Logger.i("处理IC卡时间:" + (System.currentTimeMillis() - startTime) + "ms");
        Logger.i("iccard readInfo:" + stuInfo.toString());
        boolean needAdd = checkQulification(stuInfo.getStuCode(), STUDENT_CODE);
        if (needAdd) {
            Student student = new Student();
            student.setStudentCode(stuInfo.getStuCode());
            student.setStudentName(stuInfo.getStuName());
            student.setSex(stuInfo.getSex());
            showAddHint(student);
        }
    }

    @Override
    public void onIdCardRead(IDCardInfo idCardInfo) {
        boolean needAdd = checkQulification(idCardInfo.getId(), ID_CARD_NO);
        Logger.i("IDCardInfo readInfo:" + idCardInfo.toString());
        if (needAdd) {
            Student student = new Student();
            student.setStudentName(idCardInfo.getName());
            student.setSex(idCardInfo.getSex().contains("男") ? Student.MALE : Student.FEMALE);
            student.setIdCardNo(idCardInfo.getId());
            showAddHint(student);
        }
    }

    @Override
    public void onQrArrived(String qrCode) {
        boolean needAdd = checkQulification(qrCode, STUDENT_CODE);
        if (needAdd) {
            Student student = new Student();
            student.setStudentCode(qrCode);
            showAddHint(student);
        }
    }

    @Override
    public void onQRWrongLength(int length, int expectLength) {
        ToastUtils.showShort(R.string.qr_length_error);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (scannerGunManager != null && scannerGunManager.dispatchKeyEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    private boolean checkQulification(String code, int flag) {
        Student student = null;
        switch (flag) {
            case ID_CARD_NO:
                student = DBManager.getInstance().queryStudentByIDCode(code);
                break;
            case STUDENT_CODE:
                student = DBManager.getInstance().queryStudentByStuCode(code);
                break;
        }
        if (student == null) {
            if (!needAdd) {
                toastSpeak(getString(R.string.student_nonentity));
            }
            return needAdd;
        }
        LogUtil.logDebugMessage("检入考生：" + student.toString());
        StudentItem studentItem = DBManager.getInstance().queryStuItemByStuCode(student.getStudentCode());
        if (studentItem == null) {
            if (needAdd) {
                registerStuItem(student);
                checkInUIThread(student);
            } else {
                toastSpeak(getString(R.string.no_project));
            }
            return false;
        } else {
            checkInUIThread(student);
            return false;
        }
    }
    private void checkInUIThread(Student student) {
        Message msg = Message.obtain();
        msg.what = CHECK_IN;
        msg.obj = student;
        mHandler.sendMessage(msg);
    }

    // 为学生报名项目
    private void registerStuItem(Student student) {

    }
    protected void toastSpeak(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 播报时间间隔必须>500ms
                LogUtils.operation("语音提示:"+msg);
                long tmp = System.currentTimeMillis();
                if (tmp - lastBroadcastTime > 500) {
                    lastBroadcastTime = tmp;
                    ToastUtils.showShort(msg);
                    TtsManager.getInstance().speak(msg);
                    Logger.i(msg);
                }
            }
        });
    }
    private SweetAlertDialog dialog;
    private void showAddHint(final Student student) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = new SweetAlertDialog(BaseCheckActivity.this).setTitleText(getString(R.string.addStu_dialog_title))
                        .setContentText(getString(R.string.addStu_dialog_content))
                        .setConfirmText(getString(R.string.confirm)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
//                                new AddStudentDialog(BaseCheckActivity.this).showDialog(student, false);
                            }
                        }).setCancelText(getString(R.string.cancel)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                if (dialog != null && !dialog.isShowing()){
                    dialog.show();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (afrFragment != null && afrFragment.isOpenCamera) {
                showAFR();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (afrFragment != null && afrFragment.isOpenCamera) {
            showAFR();
        }
    }
    public void showAFR() {
        if (SettingHelper.getSystemSetting().getCheckTool() != 4) {
            ToastUtils.showShort("未选择人脸识别检录功能");
            return;
        }
        initAFR();
        if (afrFrameLayout == null) {
            return;
        }

        boolean isGoto = afrFragment.gotoUVCFaceCamera(!afrFragment.isOpenCamera);
        if (isGoto) {
            if (afrFragment.isOpenCamera) {
                afrFrameLayout.setVisibility(View.VISIBLE);
            } else {
                afrFrameLayout.setVisibility(View.GONE);
            }
        }
    }
    private void initAFR() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(setAFRFrameLayoutResID().getId(), afrFragment);
        transaction.commitAllowingStateLoss();// 提交更改
    }
    public abstract View setAFRFrameLayoutResID();
}
