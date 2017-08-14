package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.DrivingSchool;
import com.dada.mylibrary.Gson.UserComment;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.View.Tools;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Dada_User_pingjia extends AppCompatActivity implements View.OnClickListener,
        GetInfo.GetResultCallBack, GetInfo.PostSaveAppraiseCallBack {
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private TextView pj_bm, pj_k1, pj_k2, pj_k3, pj_k4, pj_zh;
    private Button user_comment;
    private EditText user_comment_content;
    private RatingBar user_comment_RatingBar;
    private int ratingBar_size = 0;
    private GetInfo getInfo;
    private GlobalData mGlobalData;
    public SharedPreferences LoginID;
    private String phoneNumber;
    public SharedPreferences.Editor editor;
    private int levelnumber;
    private DrivingSchool mDrivingSchool;
    private ProgressDialog dialog = null;
    private Check cd;
    private Thread thread;
    private Timer timer;
    private static final int TIME_OUT = 0;
    private static final int SUCCESS = 1;
    // 超时的时限为5秒
    private static final int TIME_LIMIT = 15000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user_pingjia);
        LoginID = getSharedPreferences("isLogin", 0);
        editor = LoginID.edit();
        phoneNumber = LoginID.getString("user_mobile", "");
        cd = new Check(getApplicationContext());
        mGlobalData = (GlobalData) getApplication();
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        getInfo.setmPostSaveAppraiseCallBack(this);

        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        toolbar_content.setText("我要评价");
        toobar_back.setOnClickListener(this);

        if (cd.isConnectingToInternet()) {
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    getInfo.G04(phoneNumber, mGlobalData.mDEVICE_ID);
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
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
            }, 0);
        }
        init();

    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_pingjia.this, "提示", "正在拼命加载中...");
        pj_bm = (TextView) findViewById(R.id.pj_bm);
        pj_k1 = (TextView) findViewById(R.id.pj_k1);
        pj_k2 = (TextView) findViewById(R.id.pj_k2);
        pj_k3 = (TextView) findViewById(R.id.pj_k3);
        pj_k4 = (TextView) findViewById(R.id.pj_k4);
        pj_zh = (TextView) findViewById(R.id.pj_zh);
        user_comment = (Button) findViewById(R.id.user_comment);
        user_comment_RatingBar = (RatingBar) findViewById(R.id.user_comment_ratingBar);
        user_comment_content = (EditText) findViewById(R.id.user_comment_content);
        user_comment.setOnClickListener(this);
        user_comment_RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar_size = (int) rating;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_comment:
                if (levelnumber < 6) {
                    dialog = ProgressDialog.show(Dada_User_pingjia.this, "提示", "正在提交评价请稍后...");
                    final String comment_content = user_comment_content.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        if (ratingBar_size > 0) {
                            editor.putInt("levelnumber", levelnumber);
                            editor.commit();
                            final int schoolid = LoginID.getInt("schoolid", 0);
                            // 匿名内部线程
                            thread = new Thread() {
                                @Override
                                public void run() {
                                    LinkedList<BasicNameValuePair> params = new LinkedList<>();
                                    params.add(new BasicNameValuePair("phoneId", mGlobalData.mDEVICE_ID));
                                    params.add(new BasicNameValuePair("mobile", phoneNumber));
                                    params.add(new BasicNameValuePair("school", schoolid + ""));
                                    params.add(new BasicNameValuePair("subject", levelnumber + ""));
                                    params.add(new BasicNameValuePair("level", ratingBar_size + ""));
                                    params.add(new BasicNameValuePair("content", comment_content));
                                    getInfo.G05(params);
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
                            dialog.dismiss();
                            Tools.showToast(Dada_User_pingjia.this, "请先评价");
                        }
                    } else {
                        dialog.dismiss();
                        showDialog("未接入互联网,请设置网络");
                    }

                } else {
                    user_comment.setText("评价完成");
                    user_comment.setEnabled(false);
                    setTextType(5);
                }
                break;
            case R.id.toobar_lift_back:
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        Dada_User_pingjia.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
        }
    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 4:
                if (Message.contains("true")) {
                    Message msgSuc = new Message();
                    msgSuc.what = SUCCESS;
                    myHandler.sendMessage(msgSuc);
                    dialog.dismiss();
                    mDrivingSchool = (DrivingSchool) Result;
                    levelnumber = mDrivingSchool.getAppraise().size();
                    editor.putInt("schoolid", mDrivingSchool.getSchool().getId());
                    editor.commit();
                    if (levelnumber != 6) {
                        setTextType(levelnumber);
                    } else {
                        setTextType(5);
                        user_comment.setText("评价完成");
                        user_comment.setEnabled(false);
                    }
                }
                break;
        }
    }

    private void setTextType(int index) {
        pj_bm.setTextAppearance(Dada_User_pingjia.this, R.style.OriginalTextStyle);
        pj_k1.setTextAppearance(Dada_User_pingjia.this, R.style.OriginalTextStyle);
        pj_k2.setTextAppearance(Dada_User_pingjia.this, R.style.OriginalTextStyle);
        pj_k3.setTextAppearance(Dada_User_pingjia.this, R.style.OriginalTextStyle);
        pj_k4.setTextAppearance(Dada_User_pingjia.this, R.style.OriginalTextStyle);
        pj_zh.setTextAppearance(Dada_User_pingjia.this, R.style.OriginalTextStyle);

        switch (index) {
            case 0:
                pj_bm.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment_RatingBar.setNumStars(0);
                break;
            case 1:
                pj_k1.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment_RatingBar.setNumStars(0);
                break;
            case 2:
                pj_k2.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment_RatingBar.setNumStars(0);
                break;
            case 3:
                pj_k3.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment_RatingBar.setNumStars(0);
                break;
            case 4:
                pj_k4.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment_RatingBar.setNumStars(0);
                break;
            case 5:
                pj_zh.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment_RatingBar.setNumStars(0);
                break;
        }
    }

    private void showDialog(String mess) {
        new AlertDialog.Builder(this).setTitle("信息").setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivity(intent);
                    }
                }).show();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    // 接收消息的Handler
    final Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_OUT:
                    //打断线程
                    thread.interrupt();
                    dialog.dismiss();
                    Toast.makeText(Dada_User_pingjia.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                    setTextType(LoginID.getInt("levelnumber", 0));

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

    @Override
    public void postSaveAppraise(UserComment mUserComment) {
        dialog.dismiss();
        if (mUserComment.getSuccess()) {
            Tools.showToast(this, "评价成功");
            dialog.dismiss();
            editor.putInt("levelnumber", levelnumber);
            editor.commit();
            levelnumber += 1;
            if (levelnumber == 6) {
                pj_zh.setTextAppearance(Dada_User_pingjia.this, R.style.TextStyle);
                user_comment.setText("评价完成");
                user_comment.setEnabled(false);
            }
            if (levelnumber < 6) {
                setTextType(levelnumber);
            } else {
                setTextType(5);
            }
            Message msgSuc = new Message();
            msgSuc.what = SUCCESS;
            myHandler.sendMessage(msgSuc);

            Intent intent = new Intent(Dada_User_pingjia.this,Dada_User_MyJx.class);
            startActivity(intent);
            finish();
        } else {
            Tools.showToast(this, mUserComment.getMsg());
        }
    }
}
