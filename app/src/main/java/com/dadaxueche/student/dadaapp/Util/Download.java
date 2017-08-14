package com.dadaxueche.student.dadaapp.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.dada.mylibrary.GetData;
import com.dada.mylibrary.Gson.QuestionBank;
import com.dada.mylibrary.Gson.ZipInfo;
import com.dada.mylibrary.Util.CHttpRequestClass;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.View.MyProgressDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Download implements
        GetData.GetResultCallBack ,
        GetData.DownloadSpeed ,
        MyProgressDialog.DownloadState {

    private Context mContext;
    private MyProgressDialog myProgressDialog;
    private ProgressDialog mProgressDialog;
    private DownloadSuccess downloadSuccess;
    private DownloadFail downloadFail;
    private DownloadSize downloadSize;
    private ZipInfo mZipInfo = new ZipInfo();
    private List<QuestionBank.TKListDataEntity> insertList = new ArrayList<>();
    private List<QuestionBank.TKListDataEntity> updateList = new ArrayList<>();
    private GetData getData = new GetData();
    private String filePath = "";
    private String fileName = "";
    private String KM;
    private int Type = -1,questionDownSize = 0;
    private boolean isStartDownload;
    private String[] DownloadSizeUnit = new String[]{"B", "KB", "MB"};
    private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();

    public Download(Context context,String KM) {
        mContext = context;
        this.KM = KM;
        getData.setGetResultCallBack(this);
        getData.setDownloadSpeed(this);
        getData.setCando(true);
        filePath = Environment.getExternalStorageDirectory() + "/Dada/Exam/";
        Check.CheckDir(filePath);
        df.applyPattern("#.##");
    }

    public void getZipInfo() {
        getData.getZipInfo();
        Type = -1;
    }

    public void downloadQuestionBank() {
        myProgressDialog = new MyProgressDialog(mContext);
        myProgressDialog.setDownloadState(this);
        updateList.clear();
        insertList.clear();
        fileName = "tk.zip";
        new File(filePath+fileName).delete();
        myProgressDialog.setProgressDialogTitle("正在为初次使用做准备");
        myProgressDialog.ProgressDialogShow();
        isStartDownload = true;
        getZipInfo();
        Type = 0;
    }

    private void getQuestionData(String filePath, String fileName) {
        getData.getQuestion(filePath, fileName);
    }

    public void downloadImage() {
        fileName = "km"+KM+".zip";
        new File(filePath+fileName).delete();
        if(myProgressDialog == null) {
            myProgressDialog = new MyProgressDialog(mContext);
            myProgressDialog.setDownloadState(this);
            myProgressDialog.setProgressDialogTitle("正在为初次使用做准备");
            myProgressDialog.ProgressDialogShow();
        }
        if(Type == 0) {
            Type = 1;
            getImage(filePath, fileName);
        } else if(Type == -1) {
            getZipInfo();
            Type = 1;
        }
        isStartDownload = true;
    }

    private void getImage(String filePath,String fileName) {
        getData.getImage(filePath, fileName);
    }

    private void extract() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("正在解压");
        mProgressDialog.setMessage("请稍等");
        mProgressDialog.show();
        getData.extract();
        isStartDownload = false;
    }

    @Override
    public void getResultCallBack(String ID,Object object,String Message) {
        if (Message.contains("成功")) {
            switch (ID) {
                case "tk.zip":
                    questionDownSize = mZipInfo.getTksize();
                    downloadImage();
                    break;
                case "km1.zip":
                    //downloadSuccess.downloadSuccess();
                    CHttpRequestClass.downSize = 0;
                    myProgressDialog.notShow();
                    isStartDownload = false;
                    extract();
                    break;
                case "km4.zip":
                    //downloadSuccess.downloadSuccess();
                    CHttpRequestClass.downSize = 0;
                    myProgressDialog.notShow();
                    isStartDownload = false;
                    extract();
                    break;
                case "ZipInfo":
                    mZipInfo = (ZipInfo) object;
                    if(Type == -1) {
                        double Size = 0;
                        if(KM.equals("1")) {
                            Size = mZipInfo.getTksize() + mZipInfo.getKm1size();
                        } else if(KM.equals("4")) {
                            Size = mZipInfo.getTksize() + mZipInfo.getKm1size();
                        }
                        downloadSize.downloadSize(df.format(getUnitSize(Size)) + DownloadSizeUnit[getSizeUnit(Size)]);
                    } else if(Type == 0) {
                        if(KM.equals("1"))
                            myProgressDialog.setProgressDialogMax(mZipInfo.getTksize()+mZipInfo.getKm1size());
                        else if(KM.equals("4"))
                            myProgressDialog.setProgressDialogMax(mZipInfo.getTksize()+mZipInfo.getKm4size());
                        getQuestionData(filePath, fileName);
                    } else if(Type == 1) {
                        if(KM.equals("1"))
                            myProgressDialog.setProgressDialogMax(mZipInfo.getKm1size());
                        else if(KM.equals("4"))
                            myProgressDialog.setProgressDialogMax(mZipInfo.getKm4size());
                        getImage(filePath, fileName);
                    }
                    break;
            }
        } else {
            Toast.makeText(mContext,Message,Toast.LENGTH_SHORT).show();
            downloadFail.downloadFail();
            myProgressDialog.notShow();
        }
    }

    private int getSizeUnit(double DownSize) {
        double size = DownSize;
        int i = 1;
        while ((size /= 1024) > 1024) {
            i++;
        }
        return i;
    }

    private double getUnitSize(double DownSize) {
        double size = DownSize;
        while ((size /= 1024) > 1024) ;
        return size;
    }

    @Override
    public void extractSuccess(String ID, String Message) {
        mProgressDialog.dismiss();
        downloadSuccess.downloadSuccess();
    }

    public void setDownloadSuccess(DownloadSuccess downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }

    @Override
    public void Speed(String Speed,int DownloadSize) {
        if(isStartDownload) {
            myProgressDialog.setProgressDialogProgress(questionDownSize + DownloadSize);
        }
    }

    public void setCanDo(boolean canDo) {
        getData.setCando(canDo);
    }

    public void setDownloadFail(DownloadFail downloadFail) {
        this.downloadFail = downloadFail;
    }

    public void setDownloadSize(DownloadSize downloadSize) {
        this.downloadSize = downloadSize;
    }

    @Override
    public void DownloadState(int Type, int State) {
        switch (Type) {
            case 0:
                getData.setCando(false);
                isStartDownload = false;
                downloadFail.downloadFail();
                break;
        }
    }

    public interface DownloadSuccess {
        void downloadSuccess();
    }

    public interface DownloadFail {
        void downloadFail();
    }

    public interface DownloadSize {
        void downloadSize(String Size);
    }

}
