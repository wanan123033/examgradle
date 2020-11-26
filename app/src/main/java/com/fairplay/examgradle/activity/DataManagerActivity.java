package com.fairplay.examgradle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.layout.activity_data_mamager;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.fairplay.database.DBManager;
import com.fairplay.examgradle.DBDataCleaner;
import com.fairplay.examgradle.R;
import com.fairplay.examgradle.adapter.OperationAdapter;
import com.fairplay.examgradle.bean.OperationBean;
import com.fairplay.examgradle.contract.MMKVContract;
import com.fairplay.examgradle.viewmodel.DataManagerViewModel;
import com.feipulai.common.db.ClearDataProcess;
import com.feipulai.common.db.DataBaseExecutor;
import com.feipulai.common.db.DataBaseRespon;
import com.feipulai.common.db.DataBaseTask;
import com.feipulai.common.dbutils.BackupManager;
import com.feipulai.common.dbutils.FileSelectActivity;
import com.feipulai.common.view.dialog.EditDialog;
import com.github.mjdev.libaums.fs.UsbFile;
import com.gwm.annotation.layout.Layout;
import com.gwm.base.BaseApplication;

import com.gwm.mvvm.BaseMvvmTitleActivity;
import com.gwm.view.titlebar.TitleBarBuilder;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Layout(R.layout.activity_data_mamager)
public class DataManagerActivity extends BaseMvvmTitleActivity<Object, DataManagerViewModel, activity_data_mamager> implements ClearDataProcess.OnProcessFinishedListener {
    private static final int REQUEST_CODE_RESTORE = 666;
    private final int REQUEST_CODE_BACKUP = 555;
    private BackupManager backupManager;

    @Override
    protected Class<DataManagerViewModel> getViewModelClass() {
        return DataManagerViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBarBuilder(TitleBarBuilder builder) {
        return super.setTitleBarBuilder(builder.setTitle("数据管理"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backupManager = new BackupManager(this, DBManager.DB_NAME, BackupManager.TYPE_EXAM);
        mBinding.rv_operation.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        List<OperationBean> operationBeans = new ArrayList<>();
        String[] typeName = getResources().getStringArray(R.array.data_admin);
        int[] resIds = {R.drawable.icon_data_import,R.drawable.icon_data_look,R.drawable.icon_data_backup,R.drawable.icon_data_restore,R.drawable.icon_data_clear,R.drawable.icon_data_import};
        Log.e("TAG+++", Arrays.toString(resIds));
        for (int i = 0 ; i < typeName.length  ; i++){
            OperationBean bean = new OperationBean();
            bean.name = typeName[i];
            bean.res = resIds[i];
            operationBeans.add(bean);
        }
        OperationAdapter adapter = new OperationAdapter(this,operationBeans);
        mBinding.rv_operation.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:      //名单下载
                        rosterDownload();
                        break;
                    case 1:    //数据查看
                        Intent intent = new Intent(getApplicationContext(),DataSelectActivity.class);
                        startActivity(intent);
                        break;
                    case 2:    //数据备份
                        Intent intent2 = new Intent();
                        intent2.setClass(getApplicationContext(), FileSelectActivity.class);
                        intent2.putExtra(FileSelectActivity.INTENT_ACTION, FileSelectActivity.CHOOSE_DIR);
                        startActivityForResult(intent2, REQUEST_CODE_BACKUP);
                        break;
                    case 3:   //数据还原
                        new DBDataCleaner(DataManagerActivity.this, ClearDataProcess.CLEAR_FOR_RESTORE, DataManagerActivity.this).process();
                        break;
                    case 4:   //数据清空
                        new DBDataCleaner(DataManagerActivity.this, ClearDataProcess.CLEAR_DATABASE, DataManagerActivity.this).process();
                        break;
                    case 5:   //成绩上传
                        viewModel.uploadScore();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_BACKUP:
                showBackupFileNameDialog();
                break;
            case REQUEST_CODE_RESTORE:
                DBManager.getInstance().close();
                boolean restoreSuccess = backupManager.restore(FileSelectActivity.sSelectedFile);
                ToastUtils.showShort(restoreSuccess ? "数据库恢复成功" : "数据库恢复失败,请检查文件格式");
                Logger.i(restoreSuccess ? ("数据库恢复成功,文件路径:" + FileSelectActivity.sSelectedFile.getName())
                        : "数据库恢复失败");
                DBManager.getInstance().initDB();
                break;
        }
    }

    private void rosterDownload() {
        new AlertDialog.Builder(this)
                .setTitle("下载类型")
                .setItems(new String[]{"正常", "缓考", "补考"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.getInstance().getMmkv().putInt(MMKVContract.EXAMTYPE,which);
                        dialog.dismiss();
                        showDialog("下载信息中...");
                        DBManager.getInstance().clear();
                        viewModel.rosterDownload();
                    }
                }).setCancelable(false).create().show();

    }
    private void showBackupFileNameDialog() {

        createFileNameDialog((dialog, content) -> {
            String text = content.trim();
            UsbFile targetFile;
            try {
                targetFile = FileSelectActivity.sSelectedFile.createFile(text + ".db");
                boolean backupSuccess = backupManager.backup(targetFile);
                UsbFile deleteFile = FileSelectActivity.sSelectedFile.createFile("." + text + "delete.db");
                deleteFile.delete();
                ToastUtils.showShort(backupSuccess ? "数据库备份成功" : "数据库备份失败");
                Logger.i(backupSuccess ? ("数据库备份成功,备份文件名:" +
                        FileSelectActivity.sSelectedFile.getName() + "/" + targetFile.getName())
                        : "数据库备份失败");
                FileSelectActivity.sSelectedFile = null;
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtils.showShort("文件创建失败,请确保路径目录不存在已有文件");
                Logger.i("文件创建失败,数据库备份失败");
            }
        });
    }

    private void createFileNameDialog(EditDialog.OnConfirmClickListener confirmListener) {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        new EditDialog.Builder(this).setTitle("文件名")
                .setCanelable(false)
                .setMessage("输入合法保存文件名")
                .setEditHint("请输入文件名")
                .setEditText("db_backup_" + df.format(new Date()))
                .setPositiveButton(confirmListener)
                .build().show();
    }

    @Override
    public void onRestoreConfirmed() {
        chooseFile();
        ToastUtils.showShort("请选择需要恢复的数据库文件");
    }
    public void chooseFile() {
        Intent intent = new Intent();
        intent.setClass(this, FileSelectActivity.class);
        intent.putExtra(FileSelectActivity.INTENT_ACTION, FileSelectActivity.CHOOSE_FILE);
        startActivityForResult(intent, REQUEST_CODE_RESTORE);
        ToastUtils.showShort("请选择备份文件");
    }
    @Override
    public void onClearDBConfirmed() {
        DataBaseExecutor.addTask(new DataBaseTask(this, "数据清除中，请稍后。。。.", false) {
            @Override
            public DataBaseRespon executeOper() {
                boolean autoBackup = backupManager.autoBackup();
                Logger.i(autoBackup ? "自动备份成功" : "自动备份失败");
                DBManager.getInstance().clear();
                DBManager.getInstance().initDB();
                Glide.get(getApplicationContext()).clearDiskCache();
                Logger.i("进行数据清空");

                return new DataBaseRespon(true, "", "");
            }

            @Override
            public void onExecuteSuccess(DataBaseRespon respon) {
                Logger.i("数据清空完成");
                ToastUtils.showShort("数据清空完成");
            }

            @Override
            public void onExecuteFail(DataBaseRespon respon) {

            }
        });
    }

    @Override
    public void onChanged(Object o) {
        super.onChanged(o);
    }
}
