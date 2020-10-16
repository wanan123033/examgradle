package com.fairplay.examgradle.activity;

import android.content.Intent;

import com.app.layout.activity_splash;
import com.fairplay.database.DBManager;
import com.fairplay.database.entity.Student;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.viewmodel.SplashViewModel;
import com.gwm.android.Handler;
import com.gwm.annotation.Permission;
import com.gwm.annotation.layout.Layout;
import com.gwm.mvvm.BaseMvvmActivity;

@Permission({"android.permission.READ_PRIVILEGED_PHONE_STATE"})
@Layout(R.layout.activity_splash)
public class SplashActivity extends BaseMvvmActivity<Object, SplashViewModel,activity_splash> {
    @Override
    protected void onResume() {
        super.onResume();
        Handler.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDB();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        },1000);
    }

    private void initDB(){
        for(int i = 0 ; i < 5 ; i++){
            Student student = new Student();
            student.setStudentCode("20201009000"+i);
            student.setStudentName("考生"+i);
            student.setSchoolName("五华中学");
            student.setSex(1);
            DBManager.getInstance().insertStudent(student);
        }
    }

    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }
}
