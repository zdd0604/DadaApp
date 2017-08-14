package com.dadaxueche.student.dadaapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_LoginCode;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_MyJx;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_falvTk;
import com.dadaxueche.student.dadaapp.Fragment.FragmentBM;
import com.dadaxueche.student.dadaapp.Fragment.FragmentExam;
import com.dadaxueche.student.dadaapp.Fragment.FragmentHome;
import com.dadaxueche.student.dadaapp.Fragment.FragmentYuYue;
import com.dadaxueche.student.dadaapp.Fragment.FragmentZhiNan;
import com.dadaxueche.student.dadaapp.Fragment.MainNavigationDrawerFragment;
import com.dadaxueche.student.dadaapp.Util.GetLocation;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.View.Tools;
import com.dadaxueche.student.dadaapp.WeiXin.Pay;
import com.dadaxueche.student.dadaapp.wxapi.WXPayEntryActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wpf on 10-16-0016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        FragmentHome.OnKMBuutonClicked,
        MainNavigationDrawerFragment.OnClickBM,
        WXPayEntryActivity.WeiXinBack {
    private Fragment[] mFragments;
    private RadioGroup bottomRg;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton radioButton_sy, radioButton_bm, radioButton_exam, radioButton_yy, radioButton_zn, radioButton_cur;
    public static MainNavigationDrawerFragment mMainNavigationDrawerFragment;
    private ImageView home_page_news_image, home_page_title_bao;
    private RelativeLayout home_title;
    private GlobalData mGlobalData;
    public SharedPreferences LoginID;
    public SharedPreferences.Editor editor;
    private String phoneNumber, mlongitude, mlatitude;
    private MyDataBase myDataBase = new MyDataBase();
    public static ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.openActivityDurationTrack(false);
//        MobclickAgent.updateOnlineConfig(this);
//        AnalyticsConfig.enableEncrypt(true);
        mGlobalData = (GlobalData) getApplication();
        mMainNavigationDrawerFragment = (MainNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_Main);
        mMainNavigationDrawerFragment.setUp(
                R.id.navigation_drawer_Main,
                (DrawerLayout) findViewById(R.id.drawer_layout_Main));
        LoginID = getSharedPreferences("isLogin", 0);
        phoneNumber = LoginID.getString("user_mobile", "");
        mGlobalData.mUser_Mobile = phoneNumber;
        editor = LoginID.edit();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.defaultlogo)
                .showImageForEmptyUri(R.mipmap.defaultlogo)
                .showImageOnFail(R.mipmap.defaultlogo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(new File("/sdcard/Dada/cacheimage")))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
        init();
    }

    private void init() {
        mFragments = new Fragment[5];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment2);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment3);
        mFragments[3] = fragmentManager.findFragmentById(R.id.fragment4);
        mFragments[4] = fragmentManager.findFragmentById(R.id.fragment5);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .hide(mFragments[3])
                .hide(mFragments[4]);
        fragmentTransaction.show(mFragments[0]).commit();
        setFragmentIndicator();
        ((FragmentHome) mFragments[0]).setOnKMBuutonClicked(this);
        mMainNavigationDrawerFragment.setmOnClickBM(this);
        TelephonyManager tm = (TelephonyManager) getSystemService(MainActivity.this.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        mGlobalData.mDEVICE_ID = DEVICE_ID;
        if (!phoneNumber.isEmpty()) {
            try {
                Cursor cursor = myDataBase.queryUserInfo(phoneNumber);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        mlongitude = cursor.getString(cursor.getColumnIndex("UserInfo_longitude"));
                        mlatitude = cursor.getString(cursor.getColumnIndex("UserInfo_latitude"));
                    }
                }
            } catch (NullPointerException n) {
                Log.v("show", "数据为空");
            }
        }
    }

    private void setFragmentIndicator() {
        bottomRg = (RadioGroup) findViewById(R.id.fragment_rp);
        radioButton_sy = (RadioButton) findViewById(R.id.radioButton_sy);
        radioButton_bm = (RadioButton) findViewById(R.id.radioButton_bm);
        radioButton_exam = (RadioButton) findViewById(R.id.radioButton_exam);
        radioButton_yy = (RadioButton) findViewById(R.id.radioButton_yy);
        radioButton_zn = (RadioButton) findViewById(R.id.radioButton_zn);
        home_page_news_image = (ImageView) findViewById(R.id.home_page_title_geren);
        home_page_title_bao = (ImageView) findViewById(R.id.home_page_title_bao);

        home_title = (RelativeLayout) findViewById(R.id.home_title);
        radioButton_cur = radioButton_sy;
        home_page_news_image.setOnClickListener(this);
        home_page_title_bao.setOnClickListener(this);
        bottomRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction()
                        .hide(mFragments[0])
                        .hide(mFragments[1])
                        .hide(mFragments[2])
                        .hide(mFragments[3])
                        .hide(mFragments[4]);
                switch (checkedId) {
                    case R.id.radioButton_sy:
                        radioButton_cur = radioButton_sy;
                        home_title.setVisibility(View.VISIBLE);
                        fragmentTransaction.show(mFragments[0]).commit();
                        ((FragmentHome) (mFragments[0])).refreshBanner();
                        break;
                    case R.id.radioButton_bm:
                        dialog = ProgressDialog.show(MainActivity.this, "提示", "正在拼命加载中...");
                        dialog.setCanceledOnTouchOutside(true);
                        radioButton_cur = radioButton_bm;
                        home_title.setVisibility(View.GONE);
                        phoneNumber = LoginID.getString("user_mobile", "");
                        fragmentTransaction.show(mFragments[1]).commit();
                        isLoaction();
                        break;
                    case R.id.radioButton_exam:
                        radioButton_cur = radioButton_exam;
                        home_title.setVisibility(View.VISIBLE);
                        fragmentTransaction.show(mFragments[2]).commit();
                        break;
                    case R.id.radioButton_yy:
                        if (LoginID.getInt("islonginId", 0) > 0) {
                            String schoolName = "";
                            Cursor cursor = myDataBase.queryUserInfo(phoneNumber);
                            while (cursor.moveToNext()) {
                                schoolName = cursor.getString(cursor.getColumnIndex("UserInfo_schoolName"));
                            }
                            if (schoolName != null) {
                                radioButton_cur = radioButton_yy;
                                home_title.setVisibility(View.VISIBLE);
                                fragmentTransaction.show(mFragments[3]).commit();
                                ((FragmentYuYue) (mFragments[3])).init();
                            } else {
                                showDialog();
                                radioButton_cur.performClick();
                            }
                        } else {
                            intentDialog();
                            radioButton_cur.performClick();
                        }
                        break;
                    case R.id.radioButton_zn:
                        dialog = ProgressDialog.show(MainActivity.this, "提示", "正在拼命加载中...");
                        dialog.setCanceledOnTouchOutside(true);
                        radioButton_cur = radioButton_zn;
                        home_title.setVisibility(View.VISIBLE);
                        fragmentTransaction.show(mFragments[4]).commit();
                        ((FragmentZhiNan) (mFragments[4])).init();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_page_title_geren:
                opencehua();
                TelephonyManager tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
                String DEVICE_ID = tm.getDeviceId();
                break;
            case R.id.home_page_title_bao:
                String baopath = DadaUrlPath.BAO;
                intentActivity(Dada_User_falvTk.class, baopath, "嗒嗒保障");
                break;
        }
    }

    private void intentActivity(Class c, String url, String content) {
        Intent intent = new Intent(MainActivity.this, c);
        intent.putExtra("webUrl", url);
        intent.putExtra("toobarbname", content);
        startActivity(intent);
    }

    public static void opencehua() {
        mMainNavigationDrawerFragment.open();
    }

    @Override
    public void onKMButtonClicked(int position) {
        radioButton_exam.performClick();
        ((FragmentExam) (mFragments[2])).setPosition(position);
    }

    @Override
    public void onKMButtonClicked(String url) {
        radioButton_bm.performClick();
        ((FragmentBM) (mFragments[1])).setWebUrl(url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((mFragments[2])).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /*  orderNo 订单编号    mobile    手机号码
    int channel
    支付渠道 0:支付宝网页 1:支付宝手机 2:银联网页 3:银联手机4:微信公众号 5:微信APP
    int state
    -1:交易关闭 0:待支付 1:已付款*/

    @JavascriptInterface
    public String notifyFromJs(String orderNo, String phone, int channel, int status) {
//        Toast.makeText(this, "返回的手机号！！！！" + phone+"..."+status, Toast.LENGTH_LONG).show();
//        Log.v("show","返回的手机号！！！！" + phone+"...status:"+status);
        if (status == 1) {
            //保存手机号
//            Toast.makeText(this, "报名成功待开发！！！！" + phone, Toast.LENGTH_LONG).show();
            editor.putInt("islonginId",1);
            editor.putString("user_mobile", phone);
            editor.commit();
//            Intent intent = new Intent(MainActivity.this, Dada_User_MyJx.class);
//            startActivity(intent);
        } else if (status == 0 && channel == 5) {
            //发起微信支付
            //appRegister.setWeiXinBack(this);
            WXPayEntryActivity.orderNo = orderNo;
            WXPayEntryActivity.phone = phone;
            WXPayEntryActivity.setWeiXinBack(this);
            new Pay(this, LoginID).getPrePay_id(orderNo);
        }
        return "JScallAndorid";
    }

    @Override
    public void OnClick(String url) {
        radioButton_bm.performClick();
        ((FragmentBM) (mFragments[1])).setWebUrl(url);
    }

    private void showDialog() {
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("尚未报名")
                .setNegativeButton("立即报名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        radioButton_bm.performClick();
                        isLoaction();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void intentDialog() {
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("尚未登录")
                .setNegativeButton("去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, Dada_User_LoginCode.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public void weixinBack(int errorCode) {
        if (errorCode == 0) {
            isLoaction();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("支付结果");
            if (errorCode == -1)
                builder.setMessage("支付失败:" + "未知错误");
            else if (errorCode == -2)
                builder.setMessage("支付失败:" + "用户取消");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    /**
     * 传送地理位置
     */
    private void isLoaction() {
        if (mlatitude != null && mlongitude != null) {
            ((FragmentBM) mFragments[1]).setWebUrl(DadaUrlPath.ZHAO_JX_JL + "?phoneId="
                    + mGlobalData.mDEVICE_ID
                    + "&mobile=" + phoneNumber
                    + "&type=" + 0
                    + "&latitude=" + mlatitude
                    + "&longitude=" + mlongitude);
        } else {
            double[] location = GetLocation.getGPS(MainActivity.this);
            ((FragmentBM) mFragments[1]).setWebUrl(DadaUrlPath.ZHAO_JX_JL + "?phoneId="
                    + mGlobalData.mDEVICE_ID
                    + "&mobile=" + phoneNumber
                    + "&type=" + 0
                    + "&latitude=" + location[0]
                    + "&longitude=" + location[1]);
        }
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
