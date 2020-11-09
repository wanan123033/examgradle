package com.fairplay.examgradle.base;

import com.fairplay.database.entity.Student;
import com.gwm.base.BaseFragment;

public class BaseAFRFragment extends BaseFragment {
    public boolean isOpenCamera;
    private onAFRCompareListener compareListener;

    public boolean gotoUVCFaceCamera(boolean b) {
        return false;
    }
    public void setCompareListener(onAFRCompareListener compareListener) {
        this.compareListener = compareListener;
    }

    public interface onAFRCompareListener {
        void compareStu(Student student);
    }
}
