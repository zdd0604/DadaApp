package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dadaxueche.student.dadaapp.R;

/**
 * Created by wpf on 8-11-0011.
 */
public class MyProgressDialog {
    private AlertDialog.Builder mProgressBar;
    private AlertDialog mDialog;
    private MyProgressBar myProgressBar;
    private DownloadState downloadState;
    private int Type;

    public MyProgressDialog(final Context context) {
        mProgressBar = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.myprogressdialog,null,false);
        myProgressBar = (MyProgressBar) view.findViewById(R.id.view_myprogressbar);
        mProgressBar.setView(view);
        mProgressBar.setCancelable(false);
        mProgressBar.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (downloadState != null) {
                            downloadState.DownloadState(Type, 0);
                        } else {
                            Log.i("错误", "请设置接口!");
                        }
                    }
                });

//        mProgressBar.setButton(DialogInterface.BUTTON_POSITIVE, "暂停",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
//                            field.setAccessible(true);
//                            field.set(dialog, false);
//                            dialog.dismiss();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if(downloadState!=null) {
//                            downloadState.DownloadState(Type, 1);
//                            mProgressBar.setButton(DialogInterface.BUTTON_POSITIVE, "开始",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            try {
//                                                Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
//                                                field.setAccessible(true);
//                                                field.set(dialog, false);
//                                                dialog.dismiss();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            if(downloadState!=null) {
//                                                downloadState.DownloadState(Type, 2);
//                                            } else {
//                                                Log.i("错误", "请设置接口!");
//                                            }
//                                        }
//                                    });
//                        } else {
//                            Log.i("错误", "请设置接口!");
//                        }
//                    }
//                });
    }

    public void ProgressDialogShow() {
        mDialog = mProgressBar.show();
    }

    public void notShow() {
        mDialog.dismiss();
    }

    public void setProgressDialogMax(int max) {
        myProgressBar.setmProgressMax(max);
    }

    public void setProgressDialogProgress(int progress) {
        myProgressBar.setmProgress(progress);
    }

    public void setProgressDialogTitle(String title) {
        mProgressBar.setTitle(title);
    }

    public void setDownloadState(DownloadState downloadState) {
        this.downloadState = downloadState;
    }

    public void setType(int type) {
        Type = type;
    }

    public interface DownloadState {
        void DownloadState(int Type, int State);
    }
}
