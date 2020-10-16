package com.fairplay.examgradle.httppresenter;

import com.fairplay.examgradle.base.JsonDataPresenter;
import com.fairplay.examgradle.bean.BaseBean;

public class ScoreUploadPresenter extends JsonDataPresenter<ScoreUploadPresenter.ScoreUploadInfo, BaseBean<Object>> {

    public ScoreUploadPresenter() {
        super(ScoreUploadInfo.class);
    }

    @Override
    protected void onNextResult(BaseBean<Object> response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }

    public interface ScoreUploadInfo extends JsonDataPresenter.HttpBaseBean{

    }
}
