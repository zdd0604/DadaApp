package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.DrivingSchool;
import com.dada.mylibrary.Gson.ErrorInfo;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Dada_User_refund extends AppCompatActivity implements View.OnClickListener,
        GetInfo.GetResultCallBack {
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private List<String> list_title = new ArrayList<>();
    private List<String> list_content = new ArrayList<>();
    private ListView my_jiaxiao_tuikuai;
    private Button but_lianxi_jx, but_lianxi_jl, but_lianxi_dada, but_queren;
    private EditText user_yijian;
    private GetInfo getInfo;
    private GlobalData mGlobalData;
    private Boolean isInternetPresent;
    public SharedPreferences LoginID;
    public SharedPreferences.Editor editor;
    private String phoneNumber;
    private MyDataBase myDataBase = new MyDataBase();
    private ProgressDialog dialog = null;
    private String JX_Dada_Tel, JX_School_Telephone,
            JX_School_Mobile, schoolname, coachname,
            money, car, ClassName, LicenseType;
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
        setContentView(R.layout.activity_dada__user_refund);
        LoginID = getSharedPreferences("isLogin", 0);
        editor = LoginID.edit();
        phoneNumber = LoginID.getString("user_mobile", "");
        cd = new Check(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet(); // true or false
        getInfo = new GetInfo();
        mGlobalData = (GlobalData) getApplication();
        getInfo.setGetResultCallBack(this);
        init();
    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_refund.this, "提示", "拼命加载中...");
        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        my_jiaxiao_tuikuai = (ListView) findViewById(R.id.my_jiaxiao_tuikuai);
        but_lianxi_jx = (Button) findViewById(R.id.but_lianxi_jx);
        but_lianxi_jl = (Button) findViewById(R.id.but_lianxi_jl);
        but_lianxi_dada = (Button) findViewById(R.id.but_lianxi_dada);
        but_queren = (Button) findViewById(R.id.but_queren);
        user_yijian = (EditText) findViewById(R.id.user_yijian);

        toolbar_content.setText("申请退款");
        toobar_back.setOnClickListener(this);
        but_lianxi_jx.setOnClickListener(this);
        but_lianxi_jl.setOnClickListener(this);
        but_lianxi_dada.setOnClickListener(this);
        but_queren.setOnClickListener(this);

        if (isInternetPresent) {
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
            showDialog("未接入互联网,请设置网络");
        }

    }

    private void setUserHand(String photoNumber) {

        Cursor cursor = myDataBase.querySchoolInfo(photoNumber);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                JX_Dada_Tel = cursor.getString(cursor.getColumnIndex("Dada_telephone"));
                JX_School_Telephone = cursor.getString(cursor.getColumnIndex("School_telephone"));
                JX_School_Mobile = cursor.getString(cursor.getColumnIndex("School_mobile"));

                schoolname = cursor.getString(cursor.getColumnIndex("Order_schoolName"));
                coachname = cursor.getString(cursor.getColumnIndex("Order_coachName"));
                money = cursor.getString(cursor.getColumnIndex("Order_money"));
                car = cursor.getString(cursor.getColumnIndex("Order_car"));
                ClassName = cursor.getString(cursor.getColumnIndex("Order_className"));
                LicenseType = cursor.getString(cursor.getColumnIndex("Order_licenseType"));
                initListData(schoolname, money, car, ClassName, LicenseType);
            }
        }
    }

    public void initListData(String schoolname, String money, String car, String classname, String LicenseType) {
        list_title.clear();
        list_content.clear();

        list_title.add("驾校：");
        list_content.add(schoolname);

        list_title.add("价格：");
        list_content.add("$ " + money);

        list_title.add("驾照：");
        list_content.add(car);

        list_title.add("班级：");
        list_content.add(classname);

        list_title.add("车型：");
        list_content.add(LicenseType);

        if (list_content.size() > 0) {
            my_jiaxiao_tuikuai.setAdapter(new My_jx_Adapter(list_title, list_content));
        }
        Message msgSuc = new Message();
        msgSuc.what = SUCCESS;
        myHandler.sendMessage(msgSuc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_lift_back:
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        Dada_User_refund.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
            case R.id.but_lianxi_jx:
                showDialog(JX_School_Telephone, JX_School_Telephone);
                break;
            case R.id.but_lianxi_jl:
                showDialog(JX_School_Mobile, JX_School_Mobile);
                break;
            case R.id.but_lianxi_dada:
                showDialog(JX_Dada_Tel, JX_Dada_Tel);
                break;
            case R.id.but_queren:
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        Dada_User_refund.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (cd.isConnectingToInternet()) {
                    dialog = ProgressDialog.show(Dada_User_refund.this, "提示", "正在提交申请、请稍等...");
                    final String yj = user_yijian.getText().toString();
                    final Integer orderid = LoginID.getInt("orderid", 0);
                    // 匿名内部线程
                    thread = new Thread() {
                        @Override
                        public void run() {
                            getInfo.G06(mGlobalData.mDEVICE_ID, phoneNumber, orderid, yj);
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
                    but_queren.setEnabled(false);
                }
                break;
        }
    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 4:
                if (Message.contains("true")) {
                    try {
                        mGlobalData.mDrivingSchool = (DrivingSchool) Result;
                        Cursor cursor = myDataBase.querySchoolInfo(phoneNumber);
                        if (cursor.getCount() > 0) {
                            if (myDataBase.deleteSchoolInfo(phoneNumber)) {
                                insertSchoolInfo();
                            }

                        } else {
                            insertSchoolInfo();
                        }
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                }
                break;
            case 6:
                if (Message.contains("true")) {
                    showDialog("申请退款成功，工作人员将与您联系，请保持手机畅通");
                    Message msgSuc = new Message();
                    msgSuc.what = SUCCESS;
                    myHandler.sendMessage(msgSuc);
                    but_queren.setEnabled(false);
                } else {
                    try {
                        ErrorInfo errorInfo = (ErrorInfo) Result;
                        Message msgSuc = new Message();
                        msgSuc.what = TIME_OUT;
                        msgSuc.obj = errorInfo.getMsg();
                        myHandler.sendMessage(msgSuc);
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                }
                break;
        }
    }

    private void insertSchoolInfo() {
        myDataBase.insertSchoolInfo(phoneNumber, mGlobalData.mDrivingSchool.getOrder().getId() + "",
                mGlobalData.mDrivingSchool.getOrder().getCoachName(),
                mGlobalData.mDrivingSchool.getOrder().getLicenseType(),
                mGlobalData.mDrivingSchool.getOrder().getMoney() + "",
                mGlobalData.mDrivingSchool.getOrder().getSchool() + "",
                mGlobalData.mDrivingSchool.getOrder().getCar(),
                mGlobalData.mDrivingSchool.getOrder().getClasses() + "",
                mGlobalData.mDrivingSchool.getOrder().getClassName(),
                mGlobalData.mDrivingSchool.getOrder().getSchoolName(),
                mGlobalData.mDrivingSchool.getDada_tel(),
                mGlobalData.mDrivingSchool.getSchool().getMobile(),
                mGlobalData.mDrivingSchool.getSchool().getTelephone(),
                mGlobalData.mDrivingSchool.getAppraise().toString());

        schoolname = mGlobalData.mDrivingSchool.getOrder().getSchoolName();
        coachname = mGlobalData.mDrivingSchool.getOrder().getCoachName();
        money = String.valueOf(mGlobalData.mDrivingSchool.getOrder().getMoney());
        car = mGlobalData.mDrivingSchool.getOrder().getCar();
        ClassName = mGlobalData.mDrivingSchool.getOrder().getClassName();
        LicenseType = mGlobalData.mDrivingSchool.getOrder().getLicenseType();

        JX_Dada_Tel = mGlobalData.mDrivingSchool.getDada_tel();
        JX_School_Mobile = mGlobalData.mDrivingSchool.getSchool().getMobile();
        JX_School_Telephone = mGlobalData.mDrivingSchool.getSchool().getTelephone();

        editor.putInt("orderid", mGlobalData.mDrivingSchool.getOrder().getId());
        editor.commit();
        initListData(schoolname, money, car, ClassName, LicenseType);
    }

    private class My_jx_Adapter extends BaseAdapter {

        private List<String> list_adapter_title;
        private List<String> list_adapter_content;

        public My_jx_Adapter(List<String> list_adapter_title, List<String> list_adapter_content) {
            this.list_adapter_title = list_adapter_title;
            this.list_adapter_content = list_adapter_content;
        }

        @Override
        public int getCount() {
            return list_title.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyItem myItem;
            if (convertView == null) {
                myItem = new MyItem();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_adapter_myjx, null);
                myItem.title = (TextView) convertView.findViewById(R.id.list_myjx_title);
                myItem.content = (TextView) convertView.findViewById(R.id.list_myjx_content);
                convertView.setTag(myItem);
            } else {
                myItem = (MyItem) convertView.getTag();
            }
            myItem.title.setText(list_adapter_title.get(position));
            myItem.content.setText(list_adapter_content.get(position));
            return convertView;
        }

        private class MyItem {
            private TextView title, content;
        }

    }

    // 接收消息的Handler
    final Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_OUT:
                    //打断线程
                    thread.interrupt();
                    dialog.dismiss();
                    String content = (String) msg.obj;
                    if(content!=null){
                        showDialog(content);
                    }
                    setUserHand(phoneNumber);
                    but_queren.setEnabled(false);
                    break;
                case SUCCESS:
                    //取消定时器
                    timer.cancel();
                    dialog.dismiss();
//                    Toast.makeText(Dada_User_refund.this, "数据加载完成", Toast.LENGTH_SHORT).show();
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 拨打电话
     */
    private void showDialog(String mess, final String phoneno) {
        new AlertDialog.Builder(Dada_User_refund.this).setTitle("温馨提示").setMessage(mess)
                .setNegativeButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneno));
                        startActivity(intent);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    /**
     * 拨打电话
     */
    private void showDialog(String mess) {
        new AlertDialog.Builder(Dada_User_refund.this).setTitle("温馨提示").setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
