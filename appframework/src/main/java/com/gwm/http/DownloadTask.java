package com.gwm.http;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.gwm.util.ContextUtil;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * simple download tool
 * <p>
 * a simple download tool based on {@link DownloadManager},
 * using to handle simple download demands like download update apk.
 * </p>
 *
 * @author linxiao
 * @since 2019-12-07
 */
public class DownloadTask {

    public interface DownloadListener {

        void onStart();

        void onProgress(int total, int downloaded);

        void onCompleted(String localPath);

        void onError(Throwable e);

    }

    private static final String TAG = DownloadTask.class.getSimpleName();
    private static final int MIN_UPDATE_PERIOD = 100;

    private DownloadManager.Request request;
    private File downloadTo;
    private long downloadId = 0;
    private Timer timer;
    private TimerTask task;
    private Handler callbackHandler = new Handler();
    private int updatePeriod = MIN_UPDATE_PERIOD;

    private DownloadListener downloadListener;


    public static DownloadTask newInstance(String url) {
        DownloadTask instance = new DownloadTask();
        instance.request = new DownloadManager.Request(Uri.parse(url));
        instance.request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_MOBILE |
                        DownloadManager.Request.NETWORK_WIFI);
        instance.request.setAllowedOverMetered(true);
        instance.request.setAllowedOverRoaming(true);
        String mimeType = instance.getMimeType(url);
        Log.d(TAG, "mimeType: " + mimeType);
        if (TextUtils.isEmpty(mimeType)) {
            instance.setDownloadMimeType(mimeType);
        }
        return instance;
    }

    /**
     * set download destination in local disk
     * <p>
     * please request sdcard read and write permission if the download destination is not
     * app's internal/external cache or file directory, otherwise the local file will not
     * be created
     * </p>
     * @param fullPath full path
     */
    public DownloadTask setDownloadTo(String fullPath) {
        downloadTo = new File(fullPath);
        return this;
    }

    /**
     * set download destination in local disk
     * <p>
     * please request sdcard read and write permission if the download destination is not
     * app's internal/external cache or file directory, otherwise the local file will not
     * be created
     * </p>
     * @param path download destination directory
     * @param fileName download file name
     */
    public DownloadTask setDownloadTo(String path, String fileName) {
        downloadTo = new File(path, fileName);
        return this;
    }

    /**
     * set download notification params
     * <p>this will set notification visibility to VISIBILITY_VISIBLE_NOTIFY_COMPLETED</p>
     * @param title title
     * @param desc description
     */
    public DownloadTask setNotification(String title, String desc) {
        request.setTitle(title);
        request.setDescription(desc);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        return this;
    }

    /**
     * download without notification
     * <p>
     * need to declare android.permission.DOWNLOAD_WITHOUT_NOTIFICATION in manifest
     * to enable this setting
     * </p>
     */
    public DownloadTask hideNotification() {
        if (PermissionUtils.isGranted("android.permission.DOWNLOAD_WITHOUT_NOTIFICATION")) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        return this;
    }

    /**
     * set specific file MIME type
     * <p>
     * the SimpleDownloadTask will auto generate MIME type for download file, use this
     * method to set a specific type if auto generate was failed or incorrect
     * </p>
     * @param mimeType mimeType string
     */
    public DownloadTask setDownloadMimeType(String mimeType) {
        request.setMimeType(mimeType);
        return this;
    }

    /**
     * add extra http request header for download
     * @param name header name
     * @param value header value
     */
    public DownloadTask addRequestHeader(String name, Object value) {
        request.addRequestHeader(name, String.valueOf(value));
        return this;
    }

    /**
     * set download progress query period, min value is 100ms
     * @param period query period
     */
    public DownloadTask setProgressUpdatePeriod(int period) {
        updatePeriod = period < MIN_UPDATE_PERIOD ? MIN_UPDATE_PERIOD : period;
        return this;
    }

    public void start(DownloadListener listener) {
        if (downloadId != 0) {
            return; // task has downloadId, means download in progress
        }
        downloadListener = listener;
        if (downloadTo == null) {
            if (downloadListener != null) {
                downloadListener.onError(new IOException("download destination is empty"));
            }
            return;
        }
        String downloadPath = downloadTo.getPath();
        if (!FileUtils.isFile(downloadPath) && !PermissionUtils.isGranted("android.permission.WRITE_EXTERNAL_STORAGE")) {
            if (downloadListener != null) {
                downloadListener.onError(new SecurityException("android.permission.WRITE_EXTERNAL_STORAGE"));
            }
            return;
        }
        if (downloadTo.getParentFile() == null) {
            if (downloadListener != null) {
                downloadListener.onError(new IOException("invalid file path: " + downloadPath));
            }
            return;
        }
        if (!downloadTo.getParentFile().exists()) {
            downloadTo.getParentFile().mkdirs();
        }

        request.setDestinationUri(Uri.fromFile(downloadTo));
        downloadId = getDownloadManager().enqueue(request);
        if (downloadListener != null) {
            downloadListener.onStart();
        }
        startProgressQuery();
    }

    public void cancel() {
        if (downloadId == 0) {
            return;
        }
        stopProgressQuery();
        getDownloadManager().remove(downloadId);
        downloadId = 0;
    }

    private void startProgressQuery() {
        stopProgressQuery();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = getDownloadManager().query(new DownloadManager.Query().setFilterById(downloadId));
                if (cursor == null) {
                    return;
                }
                if (cursor.moveToFirst()) {
//                    String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    final String localPath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    final int downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    final int total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (downloadListener != null) {
                        callbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                downloadListener.onProgress(total, downloaded);
                            }
                        });
                    }
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        if (downloadListener != null) {
                            callbackHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    downloadListener.onCompleted(localPath);
                                }
                            });
                        }
                        stopProgressQuery();
                    }
                }
                cursor.close();
            }
        };
        timer.schedule(task, 0, updatePeriod);
    }

    private void stopProgressQuery() {
        if (task != null) {
            task.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private DownloadManager getDownloadManager() {
        return (DownloadManager) ContextUtil.get()
                .getSystemService(Context.DOWNLOAD_SERVICE);
    }
}
