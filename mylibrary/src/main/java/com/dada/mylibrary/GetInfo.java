package com.dada.mylibrary;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.dada.mylibrary.Gson.DrivingSchool;
import com.dada.mylibrary.Gson.ErrorInfo;
import com.dada.mylibrary.Gson.Festival;
import com.dada.mylibrary.Gson.HomeBanner;
import com.dada.mylibrary.Gson.LoginInfo;
import com.dada.mylibrary.Gson.UserComment;
import com.dada.mylibrary.Gson.UserInfo;
import com.dada.mylibrary.Gson.VoucherInfo;
import com.dada.mylibrary.Util.CHttpClient;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.message.BasicNameValuePair;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by wpf on 8-19-0019.
 */
public class GetInfo {
    private MyGetAsyncTask mGetAsyncTask;
    private ImageCodeAsyncTask mImageCodeAsyncTask;
    private GetResultCallBack getResultCallBack;
    private ImageCodeCallBack mImageCodeCallBack;
    private PostInfoCallBack postInfoCallBack;
    private onPostSaveAppraise monPostSaveAppraise;
    private PostSaveAppraiseCallBack mPostSaveAppraiseCallBack;
    private Object object;
    private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    private String[] DownloadSizeUnit = new String[]{"B", "KB", "MB"};

    public GetInfo() {
        df.applyPattern("#.##");
    }


    public void setGetResultCallBack(GetResultCallBack getResultCallBack) {
        this.getResultCallBack = getResultCallBack;
    }

    public void setPostInfoCallBack(PostInfoCallBack postInfoCallBack) {
        this.postInfoCallBack = postInfoCallBack;
    }

    public void setmImageCodeCallBack(ImageCodeCallBack mImageCodeCallBack) {
        this.mImageCodeCallBack = mImageCodeCallBack;
    }

    public void setmPostSaveAppraiseCallBack(PostSaveAppraiseCallBack mPostSaveAppraiseCallBack) {
        this.mPostSaveAppraiseCallBack = mPostSaveAppraiseCallBack;
    }

    private class MyGetAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            if (params.length > 2 && Integer.valueOf(params[2]) == 99) {
            } else {
                switch (Integer.valueOf(params[1])) {
                    case 2:     //获取短信验证码
                        result = CHttpClient.doGet3(DadaUrlPath.MESSAGE_CODE_GET, params[0]);
                        if (result != null) {
                            ErrorInfo mErrorInfo = new Gson().fromJson(result,
                                    new TypeToken<ErrorInfo>() {
                                    }.getType());
                            object = mErrorInfo;
                            result = params[1] + ";" + mErrorInfo.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 3:     //获取用户信息
                        result = CHttpClient.doGet3(DadaUrlPath.USER_INFO_Get, params[0]);
                        if (result != null) {
                            UserInfo userInfo = new Gson().fromJson(result, new TypeToken<UserInfo>() {
                            }.getType());
                            object = userInfo;
                            result = params[1] + ";" + userInfo.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 4:     //获取驾校信息
                        result = CHttpClient.doGet3(DadaUrlPath.MYSCHOOL_INFO_Get, params[0]);
                        if (result != null) {
                            DrivingSchool mDrivingSchool = new Gson().fromJson(result,
                                    new TypeToken<DrivingSchool>() {
                                    }.getType());
                            object = mDrivingSchool;
                            result = params[1] + ";" + mDrivingSchool.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 6:     //申请退款
                        result = CHttpClient.doGet3(DadaUrlPath.REFUND_GET, params[0]);
                        if (result != null) {
                            ErrorInfo userRefund = new Gson().fromJson(result,
                                    new TypeToken<ErrorInfo>() {
                                    }.getType());
                            object = userRefund;
                            result = params[1] + ";" + userRefund.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 7:     //使用验证码登录
                        result = CHttpClient.doGet3(DadaUrlPath.LOGIN_INFO_GET, params[0]);
                        if (result != null) {
                            LoginInfo mLoginInfo = new Gson().fromJson(result, new TypeToken<LoginInfo>() {
                            }.getType());
                            object = result;
                            result = params[1] + ";" + mLoginInfo.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 8:     //代金券
                        result = CHttpClient.doGet3(DadaUrlPath.MY_VOUCHER, params[0]);
                        if (result != null) {
                            VoucherInfo mVoucherInfo = new Gson().fromJson(result, new TypeToken<VoucherInfo>() {
                            }.getType());
                            object = mVoucherInfo;
                            result = params[1] + ";" + mVoucherInfo.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 9:     //转让代金券
                        result = CHttpClient.doGet3(DadaUrlPath.MY_TRANSFER, params[0]);
                        if (result != null) {
                            ErrorInfo mErrorInfo = new Gson().fromJson(result, new TypeToken<ErrorInfo>() {
                            }.getType());
                            object = mErrorInfo;
                            result = params[1] + ";" + mErrorInfo.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 10:     //h获取首页banner图
                        result = CHttpClient.doGet3(DadaUrlPath.MY_BANNER, params[0]);
                        if (result != null) {
                            HomeBanner mHomeBanner = new Gson().fromJson(result, new TypeToken<HomeBanner>() {
                            }.getType());
                            object = mHomeBanner;
                            result = params[1] + ";" + mHomeBanner.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                    case 11:     //h获取首页banner图
                        result = CHttpClient.doGet3(DadaUrlPath.FESTIVAL, params[0]);
                        if (result != null) {
                            Festival mFestival = new Gson().fromJson(result, new TypeToken<Festival>() {
                            }.getType());
                            object = mFestival;
                            result = params[1] + ";" + mFestival.getSuccess();
                        } else {
                            result = params[1] + ";" + "获取出错!";
                        }
                        break;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] strings = s.split(";");
            if (!strings[0].isEmpty()) {
                Integer id = Integer.valueOf(strings[0]);
                Object Result = object;
                String Message = strings[1];
                if (getResultCallBack != null) {
                    getResultCallBack.getResultCallBack(id, Result, Message);
                } else {
                    Log.i("错误", "未设置接口!");
                }
            }
            super.onPostExecute(s);
        }
    }

    /**
     * 获取图片验证码
     */
    public class ImageCodeAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            switch (Integer.valueOf(params[1])) {
                case 1:
                    bitmap = CHttpClient.doGet2(DadaUrlPath.IMAGE_CODE_GET);
                    break;

            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                if (mImageCodeCallBack != null) {
                    mImageCodeCallBack.imageCodeCallBack(bitmap);
                }
            }
        }
    }

    /**
     * 上传评论
     */
    public class onPostSaveAppraise extends AsyncTask<List<BasicNameValuePair>, Integer, UserComment> {

        @Override
        protected UserComment doInBackground(List<BasicNameValuePair>... params) {
            String result = CHttpClient.doPost(DadaUrlPath.Comment_SAVEAPPRAISE_GET, params[0]);
            UserComment mUserComment = new Gson().fromJson(result, UserComment.class);
            return mUserComment;
        }

        @Override
        protected void onPostExecute(UserComment userComment) {
            super.onPostExecute(userComment);
            if (userComment != null) {
                if (mPostSaveAppraiseCallBack != null) {
                    mPostSaveAppraiseCallBack.postSaveAppraise(userComment);
                }
            }
        }
    }


    public void G01() {
        mImageCodeAsyncTask = new ImageCodeAsyncTask();
        mImageCodeAsyncTask.execute("", "1");
    }

    //获取个人信息接口
    public void G02(String phoneNumber, String phoneId, String code) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("phoneNumber=" + phoneNumber
                        + "&phoneId=" + phoneId
                        + "&code=" + code,
                "2");
    }

    //获取个人信息接口
    public void G03(String mobile, String phoneId, String date) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("mobile=" + mobile
                        + "&phoneId=" + phoneId
                        + "&=" + date,
                "3");
    }


    //我的驾校
    public void G04(String mobile, String phoneId) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("mobile=" + mobile
                        + "&phoneId=" + phoneId,
                "4");
    }

    //上传评论
    public void G05(List<BasicNameValuePair> params) {
        monPostSaveAppraise = new onPostSaveAppraise();
        monPostSaveAppraise.execute(params);
    }

    //申请退款
    public void G06(String phoneId, String mobile, int orderId, String reason) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("phoneId=" + phoneId
                + "&mobile=" + mobile
                + "&orderId=" + orderId
                + "&reason=" + reason
                , "6");
    }

    //使用验证码登录
    public void G07(final String mobile, final String phoneId, final String code) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("mobile=" + mobile
                + "&phoneId=" + phoneId
                + "&code=" + code
                , "7");
    }


    //代金卷
    public void G08(final String mobile, final String phoneId) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("mobile=" + mobile
                + "&phoneId=" + phoneId
                , "8");
    }

    //转让代金券
    public void G09(final String mobile, final String phoneId, final String transferPhone, final int voucherId) {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("mobile=" + mobile
                + "&phoneId=" + phoneId
                + "&transferPhone=" + transferPhone
                + "&voucherId=" + voucherId
                , "9");
    }

    //加载首页banner图
    public void G10() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("", "10");
    }

    //加载活动
    public void G11() {
        mGetAsyncTask = new MyGetAsyncTask();
        mGetAsyncTask.execute("", "11");
    }

    public interface GetResultCallBack {
        void getResultCallBack(Integer id, Object Result, String Message);
    }

    public interface PostInfoCallBack {
        void postInfoCallBack(Integer id, String Message);
    }

    public interface ImageCodeCallBack {
        void imageCodeCallBack(Bitmap Result);
    }

    public interface PostSaveAppraiseCallBack {
        void postSaveAppraise(UserComment mUserComment);
    }
}
