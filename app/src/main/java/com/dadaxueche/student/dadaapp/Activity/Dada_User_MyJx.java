package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.DrivingSchool;
import com.dada.mylibrary.Gson.UserInfo;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Dada_User_MyJx extends AppCompatActivity implements View.OnClickListener,
        GetInfo.GetResultCallBack {
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private ListView my_jXiao_info;
    private GetInfo getInfo;
    private DrivingSchool mDrivingSchool;
    private ListView user_pingjia_listview;
    private GlobalData mGlobalData;
    private List<String> list_pJ_title = new ArrayList<>();
    private List<String> list_pJ_content = new ArrayList<>();
    private List<String> list_title = new ArrayList<>();
    private List<String> list_content = new ArrayList<>();
    private Button btn_myjx_commit, btn_myjx_shenqing;
    private String phoneNumber, schoolname, coachname,
            money, car, ClassName, LicenseType, appraise;
    public SharedPreferences LoginID;
    private MyDataBase myDataBase = new MyDataBase();
    public SharedPreferences.Editor editor;
    private Check cd;
    private ProgressDialog dialog = null;
    private Thread thread;
    private Timer timer;
    private static final int TIME_OUT = 0;
    private static final int SUCCESS = 1;
    // 超时的时限为5秒
    private static final int TIME_LIMIT = 15000;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__my_jx);
        cd = new Check(getApplicationContext());
        LoginID = getSharedPreferences("isLogin", 1);
        phoneNumber = LoginID.getString("user_mobile", "");
        editor = LoginID.edit();
        mGlobalData = (GlobalData) getApplication();
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        init();
    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_MyJx.this, "提示", "正在拼命加载中...");
        if (cd.isConnectingToInternet()) {
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    getInfo.G03(phoneNumber, mGlobalData.mDEVICE_ID, date);
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


        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        toolbar_content.setText("我的驾校");
        toobar_back.setOnClickListener(this);
        my_jXiao_info = (ListView) findViewById(R.id.my_jXiao_info);
        user_pingjia_listview = (ListView) findViewById(R.id.user_pingjia_listview);
        btn_myjx_commit = (Button) findViewById(R.id.btn_myjx_commit);
        btn_myjx_shenqing = (Button) findViewById(R.id.btn_myjx_shenqing);
        btn_myjx_shenqing.setOnClickListener(this);
        btn_myjx_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_lift_back:
                finish();
                break;
            case R.id.btn_myjx_commit:
                intentActivity(Dada_User_pingjia.class);
                break;
            case R.id.btn_myjx_shenqing:
                intentActivity(Dada_User_refund.class);
                break;
        }
    }

    private void intentActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
        thread.interrupt();
        finish();
    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 3:
                if (Message.contains("true")) {
                    try {
                        mUserInfo = (UserInfo) Result;
                        Cursor cursor = myDataBase.queryUserInfo(phoneNumber);
                        if (cursor.getCount() > 0) {
                            if (myDataBase.deleteUserInfo(phoneNumber)) {
                                insertUserInfoBase();
                            } else {
                                Log.v("show", "删除个人信息失败");
                            }
                        } else {
                            insertUserInfoBase();
                        }
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                }
                break;
            case 4:
                if (Message.contains("true")) {
                    try {
                        dialog.dismiss();
                        mDrivingSchool = (DrivingSchool) Result;
                        Cursor cursor = myDataBase.querySchoolInfo(phoneNumber);
                        if (cursor.getCount() > 0) {
                            if (myDataBase.deleteSchoolInfo(phoneNumber)) {
                                insertMyDataBase();
                            } else {
                                Log.v("show", "删除个人信息失败");
                            }
                        } else {
                            insertMyDataBase();
                        }
                        insertPjia(mDrivingSchool.getAppraise());
                        Message msgSuc = new Message();
                        msgSuc.what = SUCCESS;
                        myHandler.sendMessage(msgSuc);
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    } catch (NullPointerException n) {
                        Log.v("show", "数据为空");
                        Message msgSuc = new Message();
                        msgSuc.what = TIME_OUT;
                        myHandler.sendMessage(msgSuc);
                    }
                }
                break;
        }
    }

    /**
     * 将个人信息存入数据库
     */
    private void insertUserInfoBase() {
        /**个将个人信息存放在数据库*/
        myDataBase.insertUserInfo(mUserInfo.getEntity().getName(),
                mUserInfo.getEntity().getIdentityId(),
                mUserInfo.getEntity().getMobile(),
                mUserInfo.getEntity().getPhoto(),
                mUserInfo.getEntity().getSchoolName(),
                mUserInfo.getEntity().getLongitude(),
                mUserInfo.getEntity().getLatitude(),
                mUserInfo.getUrl());
        editor.putInt("islonginId", 1);
        editor.putString("user_mobile", mUserInfo.getEntity().getMobile());
        editor.commit();
    }


    /**
     * 将驾校信息存入数据库
     */
    private void insertMyDataBase() {
        myDataBase.insertSchoolInfo(phoneNumber, mDrivingSchool.getOrder().getId() + "",
                mDrivingSchool.getOrder().getCoachName(),
                mDrivingSchool.getOrder().getLicenseType(),
                mDrivingSchool.getOrder().getMoney() + "",
                mDrivingSchool.getOrder().getSchool() + "",
                mDrivingSchool.getOrder().getCar(),
                mDrivingSchool.getOrder().getClasses() + "",
                mDrivingSchool.getOrder().getClassName(),
                mDrivingSchool.getOrder().getSchoolName(),
                mDrivingSchool.getDada_tel(),
                mDrivingSchool.getSchool().getMobile(),
                mDrivingSchool.getSchool().getTelephone(),
                mDrivingSchool.getAppraise().toString());

        editor.putInt("schoolid", mDrivingSchool.getOrder().getSchool());
        editor.putInt("orderid", mDrivingSchool.getOrder().getId());
        editor.putInt("levelnumber", mDrivingSchool.getAppraise().size());
        editor.putInt("schoolid", mDrivingSchool.getSchool().getId());
        editor.commit();

        schoolname = mDrivingSchool.getOrder().getSchoolName();
        coachname = mDrivingSchool.getOrder().getCoachName();
        money = String.valueOf(mDrivingSchool.getOrder().getMoney());
        car = mDrivingSchool.getOrder().getCar();
        ClassName = mDrivingSchool.getOrder().getClassName();
        LicenseType = mDrivingSchool.getOrder().getLicenseType();
        initListData(schoolname, coachname, money, car, ClassName, LicenseType);
    }

    /**
     * 加载集合数据
     */
    private void initListData(String schoolname, String coachname, String money, String car, String classname, String LicenseType) {
        list_title.clear();
        list_content.clear();

        list_title.add("驾校：");
        if (coachname != null) {
            list_content.add(schoolname + "(" + coachname + ")");
        } else {
            list_content.add(schoolname);
        }

        list_title.add("价格：");
        list_content.add("$ " + money);

        list_title.add("驾照：");
        list_content.add(LicenseType);

        list_title.add("班级：");
        list_content.add(classname);

        list_title.add("车型：");
        list_content.add(car);
        if (list_content.size() > 0) {
            my_jXiao_info.setAdapter(new My_jx_Adapter(list_title, list_content));
        }
    }

    private void insertPjia(List<DrivingSchool.AppraiseEntity> appraiseEntities) {
        list_pJ_title.clear();
        list_pJ_content.clear();
        for (int i = 0; i < appraiseEntities.size(); i++) {
            switch (appraiseEntities.get(i).getSubject()) {
                case 0:
                    list_pJ_title.add("报名评价：");
                    list_pJ_content.add(appraiseEntities.get(i).getLevel() + "");
                    btn_myjx_commit.setText("科目一评价");
                    break;
                case 1:
                    list_pJ_title.add("科目一评价：");
                    list_pJ_content.add(appraiseEntities.get(i).getLevel() + "");
                    btn_myjx_commit.setText("科目二评价");
                    break;
                case 2:
                    list_pJ_title.add("科目二评价：");
                    list_pJ_content.add(appraiseEntities.get(i).getLevel() + "");
                    btn_myjx_commit.setText("科目三评价");
                    break;
                case 3:
                    list_pJ_title.add("科目三评价：");
                    list_pJ_content.add(appraiseEntities.get(i).getLevel() + "");
                    btn_myjx_commit.setText("科目四评价");
                    break;
                case 4:
                    list_pJ_title.add("科目四评价：");
                    list_pJ_content.add(appraiseEntities.get(i).getLevel() + "");
                    btn_myjx_commit.setText("综合评价");
                    break;
                case 5:
                    list_pJ_title.add("综合评价：");
                    list_pJ_content.add(appraiseEntities.get(i).getLevel() + "");
                    btn_myjx_commit.setText("评价完成");
                    break;
            }
        }

        user_pingjia_listview.setAdapter(new My_jx_pingjia_Adapter(list_pJ_title, list_pJ_content));
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
            return list_adapter_title.size();
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

    private class My_jx_pingjia_Adapter extends BaseAdapter {

        private List<String> list_adapter_title;
        private List<String> list_adapter_content;

        public My_jx_pingjia_Adapter(List<String> list_adapter_title, List<String> list_adapter_content) {
            this.list_adapter_title = list_adapter_title;
            this.list_adapter_content = list_adapter_content;
        }

        @Override
        public int getCount() {
            return list_adapter_title.size();
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pingjia_xingxing, null);
                myItem.kemu_content = (TextView) convertView.findViewById(R.id.kemu_content);
                myItem.kemu_number = (RatingBar) convertView.findViewById(R.id.kemu_number);
                convertView.setTag(myItem);
            } else {
                myItem = (MyItem) convertView.getTag();
            }
            myItem.kemu_content.setText(list_adapter_title.get(position));
            myItem.kemu_number.setNumStars(Integer.valueOf(list_adapter_content.get(position)));
            return convertView;
        }

        private class MyItem {
            private TextView kemu_content;
            private RatingBar kemu_number;
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
                    Toast.makeText(Dada_User_MyJx.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                    setUserHand(phoneNumber);
                    btn_myjx_commit.setEnabled(false);
                    btn_myjx_shenqing.setEnabled(false);
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 加载数据库数据
     */
    private void setUserHand(String photoNumber) {
        Cursor cursor = myDataBase.querySchoolInfo(photoNumber);
        List<DrivingSchool.AppraiseEntity> appraiseEntities = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                schoolname = cursor.getString(cursor.getColumnIndex("Order_schoolName"));
                coachname = cursor.getString(cursor.getColumnIndex("Order_coachName"));
                money = cursor.getString(cursor.getColumnIndex("Order_money"));
                car = cursor.getString(cursor.getColumnIndex("Order_car"));
                ClassName = cursor.getString(cursor.getColumnIndex("Order_className"));
                LicenseType = cursor.getString(cursor.getColumnIndex("Order_licenseType"));
                appraise = cursor.getString(cursor.getColumnIndex("School_appraise"));
                initListData(schoolname, coachname, money, car, ClassName, LicenseType);
                try {

                    JSONArray arr = new JSONArray(appraise);
                    for (int i = 0; i < arr.length(); i++) {
                        DrivingSchool.AppraiseEntity appraiseEntity = new DrivingSchool.AppraiseEntity();
                        JSONObject temp = (JSONObject) arr.get(i);
                        int subject = temp.getInt("subject");
                        int level = temp.getInt("level");
                        appraiseEntity.setSubject(subject);
                        appraiseEntity.setLevel(level);
                        appraiseEntities.add(appraiseEntity);
                    }
                    insertPjia(appraiseEntities);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
