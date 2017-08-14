package com.dadaxueche.student.dadaapp.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.ExamResult;
import com.dada.mylibrary.Gson.QuestionBank;
import com.dada.mylibrary.Util.Check;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wpf on 9-6-0006.
 */
public class SaveExamResult implements GetInfo.PostInfoCallBack {

    private List<QuestionBank.TKListDataEntity> QuestionList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> result_Selects = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Context mContext;
    private UploadSuccess uploadSuccess;
    private GetInfo getInfo = new GetInfo();
    private GlobalData mGlobalData;
    private MyDataBase myDataBase = new MyDataBase();
    private Date mBeginDateTime;
    private Date mEndDateTime;
    private String exam_Result = "";
    private String examResultFilePath = "";
    private String KM = "";
    private String CX = "";


    public SaveExamResult(Context context) {
        mContext = context;
    }

    public void setCX(String CX) {
        this.CX = CX;
    }

    public void setExam_Result(String exam_Result) {
        this.exam_Result = exam_Result;
    }

    public void setExamResultFilePath(String examResultFilePath) {
        this.examResultFilePath = examResultFilePath;
    }

    public void setKM(String KM) {
        this.KM = KM;
    }

    public void setQuestionList(List<QuestionBank.TKListDataEntity> questionList) {
        QuestionList = questionList;
    }

    public void setResult_Selects(List<ExamResult.STResultsEntity> result_Selects) {
        this.result_Selects = result_Selects;
    }

    public void saveExamResult() {
        String kscx = CX;
        String tklx = KM;
        String beginTime = simpleDateFormat.format(mBeginDateTime);
        String endTime  = simpleDateFormat.format(mEndDateTime);
        int ksts = QuestionList.size();
        int wcts = getResultSelectSize();
        int kscj = getKSCJ();
        myDataBase.insertExamResult(kscx,tklx,beginTime,endTime,ksts,wcts,kscj);
    }

    public boolean isConnect(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null&& info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.v("错误", e.toString());
        }
        return false;
    }

    public int getResultSelectSize() {
        int i = 0;
        for(ExamResult.STResultsEntity resultsEntity : result_Selects) {
            if(resultsEntity.getXzda().contains("A")
                    || resultsEntity.getXzda().contains("B")
                    || resultsEntity.getXzda().contains("C")
                    || resultsEntity.getXzda().contains("D")
                    || resultsEntity.getXzda().contains("Y")
                    || resultsEntity.getXzda().contains("N")) {
                i++;
            }
        }
        return i;
    }

    private boolean saveResultToSDCard(String exam_Result) {
        Check.CheckDir(examResultFilePath);
        FileOutputStream out = null;
        String fileName = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        try {
            out = new FileOutputStream(examResultFilePath + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (out != null) {
            try {
                out.write(exam_Result.getBytes());
                out.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getKSCJ() {
        int i = 0;
        for(ExamResult.STResultsEntity resultsEntity : result_Selects) {
            if("T".equals(resultsEntity.getEvl()))
                i++;
        }
        return i;
    }

    @Override
    public void postInfoCallBack(Integer integer, String s) {
        if(integer == 6) {
            Log.i("上传", s);
            if(s.contains("成功")) {
                if(uploadSuccess != null) {
                    uploadSuccess.uploadSuccess(s);
                } else {
                    Log.i("错误","请设置接口");
                }
            } else {
                saveExamResult();
            }
        }
    }

    public void setmGlobalData(GlobalData mGlobalData) {
        this.mGlobalData = mGlobalData;
    }

    public void setmBeginDateTime(Date mBeginDateTime) {
        this.mBeginDateTime = mBeginDateTime;
    }

    public void setUploadSuccess(UploadSuccess uploadSuccessed) {
        this.uploadSuccess = uploadSuccessed;
    }

    public void setmEndDateTime(Date mEndDateTime) {
        this.mEndDateTime = mEndDateTime;
    }

    public interface UploadSuccess {
        void uploadSuccess(String msg);
    }
}
