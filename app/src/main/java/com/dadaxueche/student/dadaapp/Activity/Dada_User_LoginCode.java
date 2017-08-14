package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.ErrorInfo;
import com.dada.mylibrary.Gson.LoginInfo;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.View.Tools;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

public class Dada_User_LoginCode extends AppCompatActivity implements View.OnClickListener,
        GetInfo.GetResultCallBack, GetInfo.ImageCodeCallBack {
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private ImageView user_login_tp_btn;
    private EditText user_login_tp_code, user_login_mobile, user_login_mobile_code;
    private Button user_login_mb_btn, user_login;
    private TimerTask task;
    private GlobalData mGlobalData;
    private GetInfo getInfo;
    private int i = 0;
    private MyDataBase myDataBase = new MyDataBase();
    private Bitmap bgetHttpBitmap;
    private String image_Code, phoneNumber, message_Code;
    public SharedPreferences LoginID;
    public SharedPreferences.Editor editor;
    private Check cd;
    private ProgressDialog dialog = null;
    private Thread thread;
    private Timer timer;
    private static final int TIME_OUT = 0;
    private static final int SUCCESS = 1;
    // 超时的时限为5秒
    private static final int TIME_LIMIT = 15000;
    private LoginInfo mLoginInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__login_code);
        cd = new Check(getApplicationContext());
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        getInfo.setmImageCodeCallBack(this);
        LoginID = getSharedPreferences("isLogin", 0);
        editor = LoginID.edit();
        mGlobalData = (GlobalData) getApplication();
        toobar_back = (LinearLayout) findViewById(R.id.toobar_back);
        toolbar_content = (TextView) findViewById(R.id.toolbar_content);
        toolbar_content.setText("验证码登录");
        init();
    }


    private void init() {
        user_login_tp_code = (EditText) findViewById(R.id.user_login_tp_code);
        user_login_tp_btn = (ImageView) findViewById(R.id.user_login_tp_btn);
        user_login_mobile = (EditText) findViewById(R.id.user_login_mobile);
        user_login_mb_btn = (Button) findViewById(R.id.user_login_mb_btn);
        user_login_mobile_code = (EditText) findViewById(R.id.user_login_mobile_code);
        user_login = (Button) findViewById(R.id.user_login);

        user_login_tp_code.addTextChangedListener(tw);
        user_login_mobile.addTextChangedListener(tw);
        user_login_mobile_code.addTextChangedListener(tw);

        user_login_mb_btn.setOnClickListener(this);
        user_login_tp_btn.setOnClickListener(this);
        user_login.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        if (cd.isConnectingToInternet()) {
            getInfo.G01();
        } else {
            user_login_tp_btn.setImageResource(R.mipmap.tpyzm);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(201);
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    Bitmap data = (Bitmap) msg.obj;
                    user_login_tp_btn.setImageBitmap(data);
                    break;
                case 1:
                    ErrorInfo mErrorInfo = (ErrorInfo) msg.obj;
                    Tools.showToast(Dada_User_LoginCode.this, mErrorInfo.getMsg());
                    break;
                case 2:
                    boolean mboolean = (boolean) msg.obj;
                    if (mboolean) {
                        Intent intent = new Intent(Dada_User_LoginCode.this, Dada_User_Info.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case 3:
                    String content = (String) msg.obj;
                    Tools.showToast(Dada_User_LoginCode.this, content);
                    user_login_tp_code.setError(content);
                    break;
                case 4:
                    boolean istrue = (boolean) msg.obj;
                    if (istrue) {
                        user_login_mb_btn.setEnabled(false);
                        Timer timer = new Timer();
                        final Handler handler = new Handler() {
                            public void handleMessage(Message msg) {
                                if (msg.what == 1) {
                                    user_login_mb_btn.setText("还有" + (59 - i) + "秒");
                                    if (i == 59) {
                                        user_login_mb_btn.setText("获取验证码");
                                        user_login_mb_btn.setEnabled(true);
                                        i = -1;
                                        task.cancel();
                                    }
                                    i++;
                                }
                                super.handleMessage(msg);
                            }
                        };
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 1000, 1000);
                    } else {
                        Tools.showToast(Dada_User_LoginCode.this, istrue + "");
                    }
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_login_tp_btn:
                if (cd.isConnectingToInternet()) {
                    v.setVisibility(View.INVISIBLE);
                    getInfo.G01();
                } else {
                    Tools.showToast(Dada_User_LoginCode.this, "网络不可用");
                    user_login_tp_btn.setImageResource(R.mipmap.tpyzm);
                }

                break;
            case R.id.user_login_mb_btn:
                user_login.setFocusable(true);
                image_Code = user_login_tp_code.getText().toString();
                phoneNumber = user_login_mobile.getText().toString();
                if (image_Code.length() == 4) {
                    if (phoneNumber.length() == 11) {
                        verificateMobile(phoneNumber, mGlobalData.mDEVICE_ID, image_Code);
                    } else {
                        user_login_mobile.setError("填写正确的手机号");
                    }
                } else {
                    user_login_tp_code.setError("填写正确的验证码");
                }
                break;
            case R.id.user_login:
                if (cd.isConnectingToInternet()) {
                    phoneNumber = user_login_mobile.getText().toString();
                    message_Code = user_login_mobile_code.getText().toString();
                    if (!message_Code.isEmpty() && !phoneNumber.isEmpty()) {
                        dialog = ProgressDialog.show(Dada_User_LoginCode.this, "提示", "正在拼命加载中...");
                        dialog.setCanceledOnTouchOutside(true);
                        // 匿名内部线程
                        thread = new Thread() {
                            @Override
                            public void run() {
                                getInfo.G07(phoneNumber, mGlobalData.mDEVICE_ID, message_Code);
                            }
                        };
                        thread.start();
                        // 设定定时器
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendTimeOutMsg(TIME_OUT);
                            }
                        }, TIME_LIMIT);

                    } else {
                        user_login_mobile_code.setError("验证码不能为空");
                    }
                } else {
                    Tools.showToast(Dada_User_LoginCode.this, "网络异常重新登录");
                }

                break;
            case R.id.toobar_back:
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    Dada_User_LoginCode.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
                finish();

                break;
        }
    }


    /**
     * 监听输入焦点
     * */
    TextWatcher tw = new TextWatcher(){
        //@Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after){
        }
        //@Override
        public void onTextChanged(CharSequence s, int start, int before, int count){

        }
        //@Override
        public void afterTextChanged(Editable s){
            if(user_login_tp_code.getText().length()==4){
                user_login_tp_code.clearFocus();
                user_login_mobile.requestFocus();
                if(user_login_mobile.getText().length()==11){
                    user_login_mobile.clearFocus();
                    user_login_mobile_code.requestFocus();
                }
            }
        }
    };


    /**
     * 验证手机号
     */
    private void verificateMobile(String phoneNumber, String phoneId, String code) {
        if (!phoneNumber.isEmpty()) {
            Tools.showToast(Dada_User_LoginCode.this, "正在发送验证码，请稍后.........");
            getInfo.G02(phoneNumber, phoneId, code);
        } else {
            user_login_mb_btn.setError("请输入手机号");
        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 2:
                if (!Message.contains("true")) {
                    try {
                        ErrorInfo errorInfo = (ErrorInfo) Result;
                        //需要数据传递，用下面方法；
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = errorInfo.getMsg();//可以是基本类型，可以是对象，可以是List、map等；
                        mHandler.sendMessage(msg);
                        Tools.showToast(Dada_User_LoginCode.this, "发送短信验证码失败,请重试");
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    } catch (NullPointerException n) {
                        Log.v("show", "获取用户信息失败");
                    }
                } else {
                    try {
                        ErrorInfo errorInfo = (ErrorInfo) Result;
                        //需要数据传递，用下面方法；
                        Message msg2 = new Message();
                        msg2.what = 4;
                        msg2.obj = errorInfo.getSuccess();//可以是基本类型，可以是对象，可以是List、map等；
                        mHandler.sendMessage(msg2);
                        Tools.showToast(Dada_User_LoginCode.this, "验证码已经通过短信发送到手机");
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    } catch (NullPointerException n) {
                        Log.v("show", "获取用户信息失败");
                    }
                }
                break;
            case 7:
                String userLogin = (String) Result;
                if (Message.contains("true")) {
                    dialog.dismiss();
                    Cursor cursor = myDataBase.queryUserInfo(phoneNumber);
                    if (cursor.getCount() > 0) {
                        if(myDataBase.deleteUserInfo(phoneNumber)){
                            if (!userLogin.isEmpty()) {
                                try {
                                    mLoginInfo = new Gson().fromJson(userLogin, LoginInfo.class);
                                    if (mLoginInfo.getSuccess()) {
                                        insertUserInofoDataBase();
                                    }
                                } catch (ClassCastException e) {
                                    Log.v("show", "类转换异常：" + Result.getClass().getName());
                                }
                            }
                        }else{
                            Log.v("show","删除个人数据失败");
                        }
                    } else {
                        if (!userLogin.isEmpty()) {
                            try {
                                mLoginInfo = new Gson().fromJson(userLogin, LoginInfo.class);
                                if (mLoginInfo.getSuccess()) {
                                    insertUserInofoDataBase();
                                }
                            } catch (ClassCastException e) {
                                Log.v("show", "类转换异常：" + Result.getClass().getName());
                            } catch (NullPointerException n) {
                                Log.v("show", "获取用户信息失败");
                            }
                        }
                    }
                    Message msgSuc = new Message();
                    msgSuc.what = SUCCESS;
                    myHandler.sendMessage(msgSuc);
                } else {
                    ErrorInfo mErrorInfo = new Gson().fromJson(userLogin, ErrorInfo.class);
                    //需要数据传递，用下面方法；
                    Message msgErrorInfo = new Message();
                    msgErrorInfo.what = 1;
                    msgErrorInfo.obj = mErrorInfo;//可以是基本类型，可以是对象，可以是List、map等；
                    mHandler.sendMessage(msgErrorInfo);
                    dialog.dismiss();
                }
                break;
        }
    }

    /**
     * 将个人信息添加在数据库
     */
    private void insertUserInofoDataBase() {
        /**个将个人信息存放在数据库*/
        myDataBase.insertUserInfo(mLoginInfo.getEntity().getName(),
                mLoginInfo.getEntity().getIdentityId(),
                mLoginInfo.getEntity().getMobile(),
                mLoginInfo.getEntity().getPhoto(),
                mLoginInfo.getEntity().getSchoolName(),
                mLoginInfo.getEntity().getLongitude(),
                mLoginInfo.getEntity().getLatitude(),
                mLoginInfo.getUrl());
        mGlobalData.mUser_Mobile = mLoginInfo.getEntity().getMobile();
        editor.putInt("islonginId", 1);
        editor.putString("user_mobile", mLoginInfo.getEntity().getMobile());
        editor.commit();
        //                        MobclickAgent.onProfileSignIn(mLoginInfo.getEntity().getMobile());
        //需要数据传递，用下面方法；
        Message succeed = new Message();
        succeed.what = 2;
        succeed.obj = mLoginInfo.getSuccess();//可以是基本类型，可以是对象，可以是List、map等；
        mHandler.sendMessage(succeed);
    }

    @Override
    public void imageCodeCallBack(Bitmap Result) {
        user_login_tp_btn.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessage(0);
        //需要数据传递，用下面方法；
        Message msg = new Message();
        msg.what = 0;
        msg.obj = Result;//可以是基本类型，可以是对象，可以是List、map等；
        mHandler.sendMessage(msg);

    }

    // 接收消息的Handler
    final Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_OUT:
                    //打断线程
                    thread.interrupt();
                    dialog.dismiss();
//                    Toast.makeText(Dada_User_LoginCode.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    //取消定时器
                    timer.cancel();
                    dialog.dismiss();
//                    Toast.makeText(Dada_User_MyJx.this, "数据加载完成", Toast.LENGTH_SHORT)
//                            .show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    //向handler发送超时信息
    private void sendTimeOutMsg(int zt) {
        Message timeOutMsg = new Message();
        timeOutMsg.what = zt;
        myHandler.sendMessage(timeOutMsg);
    }

}
