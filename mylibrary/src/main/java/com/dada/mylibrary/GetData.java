package com.dada.mylibrary;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.Gson.KM2ContentList;
import com.dada.mylibrary.Gson.KM2Video;
import com.dada.mylibrary.Gson.KM3ContentList;
import com.dada.mylibrary.Gson.KM3Video;
import com.dada.mylibrary.Gson.Mark;
import com.dada.mylibrary.Gson.Notice;
import com.dada.mylibrary.Gson.QuestionBank;
import com.dada.mylibrary.Gson.ZipInfo;
import com.dada.mylibrary.Util.CHttpRequestClass;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dada.mylibrary.Util.ExtractZIP;
import com.dada.mylibrary.Util.ReadFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpf on 9-22-0022.
 */
public class GetData {

    private List<String[]> fileList = new ArrayList<>();
    private String mUrl_Download = "http://dadaxueche2015.oss-cn-beijing.aliyuncs.com/TK/";
    private String mUrl_ZipInfo = "http://www.dadaxueche.com/m/zipInfo.do";
    private String mUrl_Notice = "http://www.dadaxueche.com/m/notice.do";
    private String mUrl_UploadMark = "http://www.dadaxueche.com/m/score.do";
    private MyGetAsyncTask mGetAsyncTask;
    private MyPostAsyncTask mPostAsyncTask;
    private GetResultCallBack mGetResultCallBack;
    private PostResultCalBack mPostResultCalBack;
    private DownloadSpeed mDownloadSpeed;
    private long mCurrentTime = 0;
    private long mOldTime = 0;
    private long mOldDownSize = 0;
    private boolean cando = true;
    private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    private String[] DownloadSizeUnit = new String[]{"B", "KB", "MB"};
    private ExtractZIP mExtractZIP = new ExtractZIP();
    private MyDataBase mDataBase = new MyDataBase();
    private int aTime = 1000;
    private Object object;

    public GetData() {
        df.applyPattern("#.###");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (cando && msg.what == 0x01) {
                mCurrentTime = System.currentTimeMillis();
                double speed = 0;
                if(mOldTime != 0) {
                    speed = (double) (CHttpRequestClass.downSize - mOldDownSize) / 1024
                            * ((double) (mCurrentTime - mOldTime) / aTime);
                }
                if(speed != 0) {
                    String strSpeed = df.format(speed) + DownloadSizeUnit[getSizeUnit(speed)];
                    mDownloadSpeed.Speed(strSpeed, CHttpRequestClass.downSize);
                }
                mOldTime = mCurrentTime;
                mOldDownSize = CHttpRequestClass.downSize;
            }
        }
    };

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

    public void setCando(boolean cando) {
        this.cando = cando;
        CHttpRequestClass.cando = cando;
        fileList.clear();
    }

    public void setDownloadSpeed(DownloadSpeed mDownloadSpeed) {
        this.mDownloadSpeed = mDownloadSpeed;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cando) {
                    Message message = handler.obtainMessage();
                    message.what = 0x01;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(aTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setmPostResultCalBack(PostResultCalBack mPostResultCalBack) {
        this.mPostResultCalBack = mPostResultCalBack;
    }

    private class MyGetAsyncTask extends AsyncTask<String, Integer, String> {
        String result = "";
        @Override
        protected String doInBackground(String... params) {
            if (params.length > 1) {
                String filePath = params[0];
                String fileName = params[1];
                String Url = mUrl_Download + fileName;

                CHttpRequestClass.filePath = filePath;
                CHttpRequestClass.fileName = fileName;

                if (!Check.CheckFile(filePath, fileName) && CHttpRequestClass.getData(Url)) {
                    result = saveToList(fileName,filePath, fileName);
                } else {
                    result = params[0] + ";" + "网络异常，请重试.";
                }
            } else {
                if(params.length == 1) {
                    switch (params[0]) {
                        case "ZipInfo":
                            result = CHttpRequestClass.sendGet(mUrl_ZipInfo);
                            if (!result.isEmpty()) {
                                ZipInfo zipInfo = new Gson().fromJson(result,new TypeToken<ZipInfo>() {}.getType());
                                object = zipInfo;
                                mUrl_Download = zipInfo.getUrl() + "TK/";
                                if(zipInfo.getSuccess())
                                    result = params[0] + ";" + "成功";
                                else
                                    result = params[0] + ";" + zipInfo.getMsg();
                            } else {
                                result = params[0] + ";" + "失败";
                            }
                            break;
                        case "notice":
                            result = CHttpRequestClass.sendGet(mUrl_Notice);
                            if (!result.isEmpty()) {
                                Notice notice = new Gson().fromJson(result,
                                        new TypeToken<Notice>() {}.getType());
                                object = notice;
                                result = params[0] + ";" + notice.getSuccess();
                            } else
                                result = params[0] + ";" + "失败";
                            break;
                        case "video2":
                            result = CHttpRequestClass.sendGet(DadaUrlPath.KM2VideoPath);
                            if (!result.isEmpty()) {
                                object = new Gson().<KM2Video>fromJson(result,
                                        new TypeToken<KM2Video>() {}.getType());
                                result = params[0] + ";" + "成功";
                            } else
                                result = params[0] + ";" + "失败";
                            break;
                        case "km2contentlist":
                            result = CHttpRequestClass.sendGet(DadaUrlPath.KM2VideoContentPath);
                            if (!result.isEmpty()) {
                                object = new Gson().<KM2ContentList>fromJson(result,
                                        new TypeToken<KM2ContentList>() {
                                        }.getType());
                                result = params[0] + ";" + "成功";
                            } else
                                result = params[0] + ";" + "失败";
                            break;
                        case "video3":
                            result = CHttpRequestClass.sendGet(DadaUrlPath.KM3VideoPath);
                            if (!result.isEmpty()) {
                                object = new Gson().<KM3Video>fromJson(result,
                                        new TypeToken<KM3Video>() {
                                        }.getType());
                                result = params[0] + ";" + "成功";
                            } else
                                result = params[0] + ";" + "失败";
                            break;
                        case "km3contentlist":
                            result = CHttpRequestClass.sendGet(DadaUrlPath.KM3VideoContentPath);
                            if (!result.isEmpty()) {
                                object = new Gson().<KM3ContentList>fromJson(result,
                                        new TypeToken<KM3ContentList>() {
                                        }.getType());
                                result = params[0] + ";" + "成功";
                            } else
                                result = params[0] + ";" + "失败";
                            break;
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] strings = s.split(";");
            if (!strings[0].isEmpty()) {
                String ID = strings[0];
                String Message = strings[1];
                if(mGetResultCallBack != null) {
                    mGetResultCallBack.getResultCallBack(ID,object,Message);
                } else {
                    Log.i("错误", "未设置接口");
                }
            }
            super.onPostExecute(s);
        }
    }

    private class MyPostAsyncTask extends AsyncTask<String,Integer,String> {

        String result = "";
        @Override
        protected String doInBackground(String... params) {
            switch (params[0]) {
                case "0":
                    result = CHttpRequestClass.sendPost(mUrl_UploadMark,params[1]);
                    if(!result.isEmpty()) {
                        object = new Gson().fromJson(result,
                                new TypeToken<Mark>() {
                                }.getType());
                        if(result.contains("true")) {
                            result = params[0] + ";" + "成功";
                        } else {
                            result = params[0] + ";" + ((Mark)object).getMsg();
                        }
                    } else {
                        result = params[0] + ";" + "网络异常，请查看网络";
                    }
                    break;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] strings = s.split(";");
            String ID = strings[0];
            String Message = strings[1];
            if(mPostResultCalBack != null) {
                mPostResultCalBack.postResultCallBack(ID,object,Message);
            } else {
                Log.i("错误", "未设置接口");
            }

        }
    }

    private class MyExtractAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = params[0] + ";" + "失败";
            for(String[] strings : fileList) {
                String Type = strings[1];
                String filePath = strings[0];
                String fileName = strings[1];
                try {
                    result = save(Type, filePath, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] strings = s.split(";");
            if (!strings[0].isEmpty()) {
                String ID = strings[0];
                String Message = strings[1];
                if(mGetResultCallBack != null) {
                    fileList.clear();
                    mGetResultCallBack.extractSuccess(ID, Message);
                } else {
                    Log.i("错误", "未设置接口");
                }
            }

            super.onPostExecute(s);
        }
    }

    private void updateDataBase(QuestionBank.TKListDataEntity data) {
        Cursor cursor = mDataBase.queryExam(data.getStxh());
        cursor.moveToFirst();
        String fileName = data.getTxlj().trim();
        fileName = fileName.replace(".Jpeg", ".jpg");
        if(fileName.contains("动画"))
            fileName = fileName.replace("动画","") + ".mp4";
        data.setTxlj(fileName);
        if (cursor.getCount() <= 0) {
            mDataBase.insertExam(data);
        } else if (cursor.getCount() > 0) {
            mDataBase.updateExam(data.getStxh(), data.getDatas());
        }
        cursor.close();
    }

    public void getKM2Video() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("video2");
    }
    public void getKM2ContentList() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("km2contentlist");
    }
    public void getKM3Video() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("video3");
    }
    public void getKM3ContentList() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("km3contentlist");
    }

    public void getNotice() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("notice");
    }

    public void getZipInfo() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("ZipInfo");
    }

    public void getQuestion(String filePath,String fileName) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute(filePath, fileName);
    }

    public void getImage(String filePath,String fileName) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute(filePath, fileName);
    }

    public void uploadMark(String phoneId,String mobile,int score,int stage,String startTime,String endTime) {
        mPostAsyncTask = new MyPostAsyncTask();
        mPostAsyncTask.execute("0","phoneId=" + phoneId
                + "&mobile=" + mobile
                + "&score=" + score
                + "&stage=" + stage
                + "&startTime=" + startTime
                + "&endTime=" + endTime);
    }

    public void extract() {
        MyExtractAsyncTask myExtractAsyncTask = new MyExtractAsyncTask();
        myExtractAsyncTask.execute("Extract");
    }

    private String saveToList(String Type,String filePath,String fileName) {
        if(filePath.isEmpty() || fileName.isEmpty()) {
            return Type + ";" + "失败";
        } else {
            String[] strings = new String[2];
            strings[0] = filePath;
            strings[1] = fileName;
            fileList.add(strings);
            return Type + ";" + "成功";
        }
    }

    private String save(String Type,String filePath,String fileName) throws IOException {
        if(!mExtractZIP.Extract(filePath,mExtractZIP.getZipFile(filePath,fileName)))
            return Type + ";" + "失败";
        if(!fileName.contains("km")) {
            String questionExtractName = mExtractZIP.getQuestionExtractName();
            String JSONString = ReadFile.Read(filePath, questionExtractName);
            QuestionBank questionBank = new Gson().fromJson(JSONString,
                    new TypeToken<QuestionBank>() {
                    }.getType());
            List<QuestionBank.TKListDataEntity> questionList = questionBank.getTKListData();
            for (QuestionBank.TKListDataEntity data : questionList) {
                updateDataBase(data);
            }
            return Type + ";" + questionBank.getMessage();
        }
        return Type + ";" + "成功";
    }

    public void setGetResultCallBack(GetResultCallBack mGetResultCallBack) {
        this.mGetResultCallBack = mGetResultCallBack;
    }

    public interface GetResultCallBack {
        void getResultCallBack(String ID,Object object,String Message);
        void extractSuccess(String ID,String Message);
    }

    public interface PostResultCalBack {
        void postResultCallBack(String ID,Object object,String Message);
    }

    public interface DownloadSpeed {
        void Speed(String Speed, int DownloadSize);
    }
}
