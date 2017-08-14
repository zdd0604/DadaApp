package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.ErrorInfo;
import com.dada.mylibrary.Gson.VoucherInfo;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.View.Tools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Dada_User_Voucher extends AppCompatActivity implements OnClickListener,
        GetInfo.GetResultCallBack {
    private LinearLayout toobarBack;
    private TextView toobarContent;
    public SharedPreferences LoginID;
    private GetInfo getInfo;
    private String phoneNumber;
    private VoucherInfo mVoucherInfo;
    private ListView voucher_list;
    private GlobalData mGlobalData;
    private Check cd;
    private ProgressDialog dialog = null;
    private Thread thread;
    private Timer timer;
    private static final int TIME_OUT = 0;
    private static final int SUCCESS = 1;
    // 超时的时限为5秒
    private static final int TIME_LIMIT = 5000;
    private EditText otherPhoneNumber;
    private View view;
    private voucherAdapter voucherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new Check(getApplicationContext());
        LoginID = getSharedPreferences("isLogin", 1);
        phoneNumber = LoginID.getString("user_mobile", "");
        setContentView(R.layout.activity_dada__user__voucher);
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        mGlobalData = (GlobalData) getApplication();
        init();
    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_Voucher.this, "提示", "正在拼命加载中...");
        toobarBack = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toobarContent = (TextView) findViewById(R.id.toolbar_lift_content);
        toobarContent.setText("我的代金券");
        voucher_list = (ListView) findViewById(R.id.voucher_list);
        if (cd.isConnectingToInternet()) {
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    getInfo.G08(phoneNumber, mGlobalData.mDEVICE_ID);
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
            Tools.showToast(Dada_User_Voucher.this, "请检查网络");
        }

        toobarBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_lift_back:
                finish();
                break;
        }
    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        Log.v("show",Message+"////");
        switch (id) {
            case 8:
                if (Message.contains("true")) {
                    try {
                        mVoucherInfo = (VoucherInfo) Result;
                        voucherAdapter = new voucherAdapter(mVoucherInfo.getRows());
                        voucher_list.setAdapter(voucherAdapter);
                        Message msgSuc = new Message();
                        msgSuc.what = SUCCESS;
                        myHandler.sendMessage(msgSuc);
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                } else {
                    try {
                        dialog.dismiss();
                        Tools.showToast(Dada_User_Voucher.this, "抱歉您没有优惠券");
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                }
                break;

            case 9:
                if (Message.contains("true")) {
                    try {
                        Message msgSuc = new Message();
                        msgSuc.what = SUCCESS;
                        myHandler.sendMessage(msgSuc);
                        Tools.showToast(Dada_User_Voucher.this, "转赠成功");

                        // 匿名内部线程
                        thread = new Thread() {
                            @Override
                            public void run() {
                                getInfo.G08(phoneNumber, mGlobalData.mDEVICE_ID);
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
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                } else {
                    try {
                        dialog.dismiss();
                        ErrorInfo errorInfo = (ErrorInfo) Result;
                        Tools.showToast(Dada_User_Voucher.this, errorInfo.getMsg());
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                }
                break;
        }
    }

    public class voucherAdapter extends BaseAdapter {
        private List<VoucherInfo.RowsEntity> voucherInfos;

        public voucherAdapter(List<VoucherInfo.RowsEntity> voucherInfos) {
            this.voucherInfos = voucherInfos;
        }

        @Override
        public int getCount() {
            return voucherInfos.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_adapter_voucher, null);
                viewHolder.begin_time = (TextView) convertView.findViewById(R.id.begin_time);
                viewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
                viewHolder.youhui_money = (TextView) convertView.findViewById(R.id.youhui_money);
                viewHolder.give_per = (Button) convertView.findViewById(R.id.give_per);
                viewHolder.state = (TextView) convertView.findViewById(R.id.state);
                viewHolder.daijinquan = (TextView) convertView.findViewById(R.id.daiijinquan);
                viewHolder.yx_time = (TextView) convertView.findViewById(R.id.yx_time);
                viewHolder.z_time = (TextView) convertView.findViewById(R.id.z_time);
                viewHolder.money_color = (TextView) convertView.findViewById(R.id.money_color);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (convertView != null) {
                viewHolder.begin_time.setText(voucherInfos.get(position).getGetTime());
                viewHolder.end_time.setText(voucherInfos.get(position).getEndTime());
                viewHolder.youhui_money.setText(voucherInfos.get(position).getMoney() + "");
                switch (voucherInfos.get(position).getState()) {
                    case 0:
                        viewHolder.state.setText("未使用");
                        viewHolder.give_per.setEnabled(true);
                        viewHolder.give_per.setVisibility(View.VISIBLE);
                        viewHolder.state.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.begin_time.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.end_time.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.youhui_money.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.daijinquan.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.yx_time.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.z_time.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        viewHolder.money_color.setTextColor(android.graphics.Color.parseColor("#f9a05f"));
                        break;
                    case 1:
                        viewHolder.give_per.setEnabled(false);
                        viewHolder.give_per.setVisibility(View.GONE);
                        viewHolder.state.setText("已使用");
                        viewHolder.state.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.begin_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.end_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.youhui_money.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.daijinquan.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.yx_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.z_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.money_color.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        break;
                    case 2:
                        viewHolder.state.setText("已过期");
                        viewHolder.give_per.setEnabled(false);
                        viewHolder.give_per.setVisibility(View.GONE);
                        viewHolder.state.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.begin_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.end_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.youhui_money.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.daijinquan.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.yx_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.z_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.money_color.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        break;
                    case 3:
                        viewHolder.state.setText("已转让");
                        viewHolder.give_per.setEnabled(false);
                        viewHolder.give_per.setVisibility(View.GONE);
                        viewHolder.state.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.begin_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.end_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.youhui_money.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.daijinquan.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.yx_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.z_time.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        viewHolder.money_color.setTextColor(android.graphics.Color.parseColor("#9e9e9e"));
                        break;

                }
                viewHolder.give_per.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showdialog(phoneNumber, voucherInfos.get(position).getId());
                    }
                });
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView begin_time, end_time, youhui_money, state, yx_time, z_time, daijinquan, money_color;
            private Button give_per;
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
//                    Toast.makeText(Dada_User_Voucher.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    //取消定时器
                    timer.cancel();
                    dialog.dismiss();
//                    Toast.makeText(Dada_User_Voucher.this, "数据加载完成", Toast.LENGTH_SHORT)
//                            .show();
                    break;
                case 3:
                    Tools.showToast(Dada_User_Voucher.this, "别闹了,自己怎么能转给自己");
                    break;
                case 4:
                    Tools.showToast(Dada_User_Voucher.this, "请输入正确的手机号");
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

    /**
     * 弹出框
     */
    private void showdialog(final String mobile, final int voucherId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dada_User_Voucher.this);
        alertDialog.setTitle("请输入对方手机号");
        view = getLayoutInflater().inflate(R.layout.transfer_alert, null);
        alertDialog.setView(view);
        alertDialog.setNegativeButton("确认赠送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 匿名内部线程
                thread = new Thread() {
                    @Override
                    public void run() {
                        otherPhoneNumber = (EditText) view.findViewById(R.id.transfer_phone);
                        String otMobile = otherPhoneNumber.getText().toString();
                        if (otherPhoneNumber.length() == 11) {
                            if (!phoneNumber.equals(otMobile)) {
                                getInfo.G09(mobile, mGlobalData.mDEVICE_ID, otMobile, voucherId);
                            } else {
                                Message message3 = new Message();
                                message3.what = 3;
                                myHandler.sendMessage(message3);
                            }
                        } else {
                            Message message4 = new Message();
                            message4.what = 4;
                            myHandler.sendMessage(message4);
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
                }, TIME_LIMIT);
            }
        }).show();
    }
}
