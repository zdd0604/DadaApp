package com.dadaxueche.student.dadaapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.UserInfo;
import com.dada.mylibrary.Util.CHttpClient;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_Falv;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_Info;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_LoginCode;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_MyJx;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_Voucher;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_falvTk;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_pingjia;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_refund;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GetLocation;
import com.dadaxueche.student.dadaapp.Util.GlobalData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainNavigationDrawerFragment extends Fragment implements View.OnClickListener,
        ListView.OnItemClickListener,
        GetInfo.PostInfoCallBack,
        GetInfo.GetResultCallBack {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer = true;
    private List<String> list_Set_Title = new ArrayList<>();
    private List<String> list_Set_Info = new ArrayList<>();
    private List<Integer> list_Set_Image = new ArrayList<>();
    private View view;
    private ListView nav_info_list;
    private LinearLayout fragment_nav_info;
    private ImageView fragment_nav_anuser_hand_image;
    private TextView fragment_nav_user_name, fragment_nav_user_mobile, fragment_nav_isLogin;
    private GlobalData mGlobalData;
    private GetInfo getInfo;
    private MyDataBase myDataBase = new MyDataBase();
    private String name, phoneNumber,
            schoolName,
            UserInfo_photo,
            user_mobile,
            mlongitude,
            mlatitude;
    private Bitmap bgetHttpBitmap;
    public SharedPreferences LoginID;
    private MyListViewAdapter adapter;
    private UserInfo mUserInfo;
    private Check cd;
    private int isLoginID;
    private OnClickBM mOnClickBM;
    private boolean sdCardExist = Environment.getExternalStorageState()
            .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    private Thread thread;
    private Timer timer;
    private static final int TIME_OUT = 0;
    private static final int SUCCESS = 1;
    // 超时的时限为5秒
    private static final int TIME_LIMIT = 15000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new Check(getActivity().getApplicationContext());
        mGlobalData = (GlobalData) getActivity().getApplication();
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        LoginID = getActivity().getSharedPreferences("isLogin", 0);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setmOnClickBM(OnClickBM mOnClickBM) {
        this.mOnClickBM = mOnClickBM;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation_drawer_main, container, false);
        init();
        return view;
    }

    private void init() {
        nav_info_list = (ListView) view.findViewById(R.id.nav_info_list);
        fragment_nav_info = (LinearLayout) view.findViewById(R.id.fragment_nav_info);
        //个人头像
        fragment_nav_anuser_hand_image = (ImageView) view.findViewById(R.id.fragment_nav_anuser_hand_image);
        //用户姓名
        fragment_nav_user_name = (TextView) view.findViewById(R.id.fragment_nav_user_name);
        //用户手机号
        fragment_nav_user_mobile = (TextView) view.findViewById(R.id.fragment_nav_user_mobile);

        fragment_nav_isLogin = (TextView) view.findViewById(R.id.fragment_nav_isLogin);
        adapter = new MyListViewAdapter(list_Set_Info, list_Set_Image, list_Set_Title);
        nav_info_list.setAdapter(adapter);
        fragment_nav_info.setOnClickListener(this);
        nav_info_list.setOnItemClickListener(this);
        getDataBases();
    }

    /**
     * 获取数据库数据
     */
    private void getDataBases() {
        user_mobile = LoginID.getString("user_mobile", "");
        isLoginID = LoginID.getInt("islonginId", 0);

        if (cd.isConnectingToInternet()) {
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    getInfo.G03(user_mobile, mGlobalData.mDEVICE_ID, date);
                }
            };
            thread.start();
            // 设定定时器
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendTimeOutMsg(1);
                }
            }, TIME_LIMIT);

        } else {
            selectUserInfo();
        }
    }


    private void selectUserInfo() {
        Cursor cursor = myDataBase.queryUserInfo(user_mobile);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("UserInfo_name"));
                fragment_nav_user_name.setText(name);
                phoneNumber = cursor.getString(cursor.getColumnIndex("UserInfo_mobile"));
                mGlobalData.mUser_Mobile = phoneNumber;
                fragment_nav_user_mobile.setText(phoneNumber);
                schoolName = cursor.getString(cursor.getColumnIndex("UserInfo_schoolName"));
                UserInfo_photo = cursor.getString(cursor.getColumnIndex("UserInfo_photo"));
                mlongitude = cursor.getString(cursor.getColumnIndex("UserInfo_longitude"));
                mlatitude = cursor.getString(cursor.getColumnIndex("UserInfo_latitude"));
            }
            setUserHand();
            cursor.close();
        } else {
            if (cd.isConnectingToInternet()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bgetHttpBitmap = CHttpClient.getHttpBitmap(DadaUrlPath.Pulic_URL + UserInfo_photo);
                        //需要数据传递，用下面方法；
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = bgetHttpBitmap;//可以是基本类型，可以是对象，可以是List、map等；
                        mHandler.sendMessage(msg);
                    }
                }).start();
            } else {
                fragment_nav_anuser_hand_image.setImageResource(R.mipmap.wodech);
            }
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    Bitmap data = (Bitmap) msg.obj;
                    fragment_nav_anuser_hand_image.setImageBitmap(data);
                    break;
                case 1:
                    fragment_nav_anuser_hand_image.setImageResource(R.mipmap.wodech);
                    selectUserInfo();
                    break;
                case 2:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    phoneNumber = userInfo.getEntity().getMobile();
                    UserInfo_photo = userInfo.getEntity().getPhoto();
                    fragment_nav_user_name.setText(userInfo.getEntity().getName());
                    fragment_nav_user_mobile.setText(userInfo.getEntity().getMobile());
                    adapter.notifyDataSetChanged();
                    setUserHand();
                    initList();
                    break;
                default:
                    break;
            }
        }

    };


    private void setUserHand() {
        String handpath = LoginID.getString("iamgeLocation", "");
        File file = new File(handpath);
        if (cd.isConnectingToInternet()) {
            if (UserInfo_photo == null) {
                if (file.exists()) {
                    Bitmap userhand = Dada_User_Info.getLoacalBitmap(handpath);
                    fragment_nav_anuser_hand_image.setImageBitmap(userhand);
                } else {
                    fragment_nav_anuser_hand_image.setImageResource(R.mipmap.wodech);
                }
            } else {
                if (file.exists()) {
                    Bitmap userhand = Dada_User_Info.getLoacalBitmap(handpath);
                    fragment_nav_anuser_hand_image.setImageBitmap(userhand);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bgetHttpBitmap = CHttpClient.getHttpBitmap(DadaUrlPath.Pulic_URL + UserInfo_photo);
                            //需要数据传递，用下面方法；
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = bgetHttpBitmap;//可以是基本类型，可以是对象，可以是List、map等；
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        } else {
            if (file.exists()) {
                Bitmap userhand = Dada_User_Info.getLoacalBitmap(handpath);
                fragment_nav_anuser_hand_image.setImageBitmap(userhand);
            } else {
                fragment_nav_anuser_hand_image.setImageResource(R.mipmap.wodech);
            }
        }
    }


    //侧滑添加数据
    private void initList() {
        list_Set_Title.clear();
        list_Set_Info.clear();
        list_Set_Image.clear();

        list_Set_Title.add("我的驾校");
        isLoginID = LoginID.getInt("islonginId", 0);
        if (isLoginID != 0) {
            if (schoolName == null || schoolName.trim().length() == 0) {
                list_Set_Info.add("(未报名驾校)");
            } else {
                list_Set_Info.add("(" + schoolName + ")");
            }
        } else {
            list_Set_Info.add("(未报名驾校)");
        }

        list_Set_Image.add(R.mipmap.jiaxiao);

        list_Set_Title.add("优惠券");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.heart);

        list_Set_Title.add("我要评价");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.heart);

        list_Set_Title.add("申请退款");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.tuikuan);

        list_Set_Title.add("地理位置");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.gps);

        list_Set_Title.add("嗒嗒社区");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.shequ);

        list_Set_Title.add("驾校加盟");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.lianmen);

        list_Set_Title.add("法律条款");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.falv);

        list_Set_Title.add("关于嗒嗒");
        list_Set_Info.add("");
        list_Set_Image.add(R.mipmap.guanyu);

    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 3:
                if (Message.contains("true")) {
                    try {
                        mUserInfo = (UserInfo) Result;
                        Cursor cursor = myDataBase.queryUserInfo(user_mobile);
                        if (cursor.getCount() > 0) {
                            if (myDataBase.deleteUserInfo(user_mobile)) {
                                insertUserInofoDataBase();
                            }
                        } else {
                            insertUserInofoDataBase();
                        }

                        Message msgSuc = new Message();
                        msgSuc.what = SUCCESS;
                        mHandler.sendMessage(msgSuc);

                        Message message = new Message();
                        message.what = 2;
                        message.obj = mUserInfo;
                        mHandler.sendMessage(message);
                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                } else {
                    initList();
                }
                break;
        }
    }

    @Override

    public void postInfoCallBack(Integer id, String Message) {

    }


    /**
     * 将个人信息添加在数据库
     */
    private void insertUserInofoDataBase() {
        myDataBase.insertUserInfo(mUserInfo.getEntity().getName(),
                mUserInfo.getEntity().getIdentityId(),
                mUserInfo.getEntity().getMobile(),
                mUserInfo.getEntity().getPhoto(),
                mUserInfo.getEntity().getSchoolName(),
                mUserInfo.getEntity().getLongitude(),
                mUserInfo.getEntity().getLatitude(),
                mUserInfo.getUrl());
        schoolName = mUserInfo.getEntity().getSchoolName();
        adapter.notifyDataSetChanged();
        selectUserInfo();
        initList();
    }

    private class MyListViewAdapter extends BaseAdapter {
        private List<String> nav_name;
        private List<String> nav_content;
        private List<Integer> nav_image;

        public MyListViewAdapter(List<String> nav_content, List<Integer> nav_image, List<String> nav_name) {
            this.nav_content = nav_content;
            this.nav_image = nav_image;
            this.nav_name = nav_name;
        }

        @Override
        public int getCount() {
            return list_Set_Title.size();
        }

        @Override
        public Object getItem(int position) {
            return list_Set_Title.get(position);
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_nav_item, parent, false);
                myItem.nav_title_image = (ImageView) convertView.findViewById(R.id.nav_item_iamge);
                myItem.nav_title_name = (TextView) convertView.findViewById(R.id.nav_item_name);
                myItem.nav_title_content = (TextView) convertView.findViewById(R.id.nav_item_content);
                convertView.setTag(myItem);
            } else {
                myItem = (MyItem) convertView.getTag();
            }
            myItem.nav_title_image.setImageResource(nav_image.get(position));
            myItem.nav_title_name.setText(nav_name.get(position));
            myItem.nav_title_content.setText(nav_content.get(position));
            return convertView;
        }

        //侧滑布局
        private class MyItem {
            ImageView nav_title_image;
            TextView nav_title_name;
            TextView nav_title_content;
        }
    }

    //打开侧滑
    public void open() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                refFragmentData();

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                }
                getActivity().supportInvalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * 重新刷新数据
     */
    public void refFragmentData() {
        phoneNumber = LoginID.getString("user_mobile", "");
        if (phoneNumber.length() > 0) {
            fragment_nav_user_name.setVisibility(View.VISIBLE);
            fragment_nav_user_mobile.setVisibility(View.VISIBLE);
            fragment_nav_isLogin.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            getDataBases();
            initList();
        } else {
            fragment_nav_anuser_hand_image.setImageResource(R.mipmap.wodech); //设置Bitmap
            fragment_nav_user_name.setVisibility(View.GONE);
            fragment_nav_user_mobile.setVisibility(View.GONE);
            fragment_nav_isLogin.setText("点击登录");
            fragment_nav_isLogin.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            initList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_nav_info:
                isLoginID = LoginID.getInt("islonginId", 0);
                if (isLoginID != 0) {
                    intentActivity(Dada_User_Info.class);
                } else {
                    intentActivity(Dada_User_LoginCode.class);
                }
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:                                                                                 //我的驾校
                isLoginID = LoginID.getInt("islonginId", 0);
                if (isLoginID != 0) {
                    if (schoolName == null || schoolName.trim().length() == 0) {
                        showDialog("尚未报名,是否到报名页面");
                    } else {
                        intentActivity(Dada_User_MyJx.class);
                    }
                } else {
                    intentDialog("尚未登录,是否到登录页面", Dada_User_LoginCode.class);
                }
                break;
            case 1:                                                                                 //优惠券
                isLoginID = LoginID.getInt("islonginId", 0);
                if (isLoginID != 0) {
                    intentActivity(Dada_User_Voucher.class);
                } else {
                    intentDialog("尚未登录,是否到登录页面", Dada_User_LoginCode.class);
                }
                break;
            case 2:                                                                                 //我要评价
                isLoginID = LoginID.getInt("islonginId", 0);
                if (isLoginID != 0) {
                    if (schoolName == null || schoolName.trim().length() == 0) {
                        showDialog("尚未报名,是否到报名页面");
                    } else {
                        intentActivity(Dada_User_pingjia.class);
                    }
                } else {
                    intentDialog("尚未登录,是否到登录页面", Dada_User_LoginCode.class);
                }

                break;
            case 3:                                                                                 //申请退款
                isLoginID = LoginID.getInt("islonginId", 0);
                if (isLoginID != 0) {
                    if (schoolName == null || schoolName.trim().length() == 0) {
                        showDialog("尚未报名,是否到报名页面");
                    } else {
                        intentActivity(Dada_User_refund.class);
                    }
                } else {
                    intentDialog("尚未登录,是否到登录页面", Dada_User_LoginCode.class);
                }

                break;
            case 4:                                                                                 //地理位置
                isLoginID = LoginID.getInt("islonginId", 0);
                if (isLoginID != 0) {
                    double[] location = GetLocation.getGPS(getActivity());
                    String url = DadaUrlPath.MY_MAP
                            + "?phoneId=" + mGlobalData.mDEVICE_ID
                            + "&mobile=" + user_mobile
                            + "&latitude=" + location[0]
                            + "&longitude=" + location[1];
                    intentActivity(Dada_User_falvTk.class, url, "地理位置");
                } else {
                    intentDialog("尚未登录,是否到登录页面", Dada_User_LoginCode.class);
                }

                break;
            case 5:                                                                                 //嗒嗒社区
                String path = DadaUrlPath.DADA_SHEQU + "?mobile=" + user_mobile + "&phoneId=" + mGlobalData.mDEVICE_ID;
                intentActivity(Dada_User_falvTk.class, path, "嗒嗒社区");
                break;
            case 6:                                                                                 //驾校加盟
                intentActivity(Dada_User_falvTk.class, DadaUrlPath.DADA_JIAMENG, "驾校加盟");
                break;
            case 7:                                                                                 //法律条款
                intentActivity(Dada_User_Falv.class);
                break;
            case 8:                                                                                 //关于嗒嗒
                intentActivity(Dada_User_falvTk.class, DadaUrlPath.DADA_INFO, "关于嗒嗒");
                break;

        }
    }

    //向handler发送超时信息
    private void sendTimeOutMsg(int zt) {
        Message timeOutMsg = new Message();
        timeOutMsg.what = zt;
        mHandler.sendMessage(timeOutMsg);
    }

    private void intentActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    private void intentActivity(Class c, String url, String content) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("webUrl", url);
        intent.putExtra("toobarbname", content);
        startActivity(intent);
    }

    private void showDialog(String mess) {
        new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage(mess)
                .setNegativeButton("立即报名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDrawerLayout.closeDrawers();
                        double[] location = GetLocation.getGPS(getActivity());
                        mOnClickBM.OnClick(DadaUrlPath.ZHAO_JX_JL
                                + "?phoneId=" + mGlobalData.mDEVICE_ID
                                + "&mobile=" + phoneNumber
                                + "&type=" + 0
                                + "&latitude=" + location[0]
                                + "&longitude=" + location[1]);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    /**
     * 跳转activity
     */
    private void intentDialog(String mess, final Class c) {
        new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage(mess)
                .setNegativeButton("去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), c);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public interface OnClickBM {
        void OnClick(String url);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
