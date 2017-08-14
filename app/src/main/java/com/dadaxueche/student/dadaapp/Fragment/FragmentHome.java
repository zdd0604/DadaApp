package com.dadaxueche.student.dadaapp.Fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.Festival;
import com.dada.mylibrary.Gson.HomeBanner;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.Activity.Dada_User_falvTk;
import com.dadaxueche.student.dadaapp.Adapter.MainViewPagerAdapter;
import com.dadaxueche.student.dadaapp.MainActivity;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GetLocation;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.View.MyView;
import com.dadaxueche.student.dadaapp.WeiXin.simcpux.Constants;
import com.dadaxueche.student.dadaapp.WeiXin.simcpux.Util;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdongdong on 2015/9/10.
 */
public class FragmentHome extends Fragment implements View.OnClickListener, GetInfo.GetResultCallBack,
        GetInfo.PostInfoCallBack {
    private View view;
    private ViewPager pager;
    private MyView myView;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private List<MainViewPagerImageFragment> mainViewPagerImageFragments = new ArrayList<>();
    private int mCurrentItem = 0;
    private int mItem;
    private Runnable mPagerAction;
    private boolean isFrist = true;
    private GetInfo getInfo;
    private static GlobalData mGlobalData;
    public SharedPreferences LoginID;
    public SharedPreferences.Editor editor;
    private MyDataBase myDataBase = new MyDataBase();
    private String phoneNumber, mlongitude, mlatitude;
    private Check cd;
    private PopupWindow mPopupWindow_Share = null;
    private PopupWindow mEleven = null;
    private LinearLayout layout_zjx,
            layout_zjl,
            layout_zjj, layout_k1,
            layout_k2, layout_k3,
            layout_k4, layout_shequ,
            layout_tuijian, layout_cwh,
            layout_geren;
    private Handler handler = new Handler();

    private OnKMBuutonClicked onKMBuutonClicked;
    private String weixin_str = "有一种学车叫“钱少保过教练好”——好东西，一定要分享出去";
    private String URL_WeriXin = DadaUrlPath.Pulic_URL + "fenXiang.html";
    private IWXAPI api;
    private SendMessageToWX.Req req;
    private Thread thread;
    public static ProgressDialog dialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        view = inflater.inflate(R.layout.fragment_home, container, false);
        cd = new Check(getActivity());
        mGlobalData = (GlobalData) getActivity().getApplication();
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        LoginID = getActivity().getSharedPreferences("isLogin", 0);
        phoneNumber = LoginID.getString("user_mobile", "");
        editor = LoginID.edit();
        if (cd.isConnectingToInternet()) {
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    getInfo.G10();
                }
            };
            thread.start();
        } else {
            mainViewPagerImageFragments.clear();
            mainViewPagerImageFragments.add(new MainViewPagerImageFragment("", "", ""));
        }
        init();
        findid();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

    }

    private boolean isTime() {
        return new SimpleDateFormat("MM-dd").format(System.currentTimeMillis()).equals("10-22");
    }

    private void init() {
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
        pager = (ViewPager) view.findViewById(R.id.tuijian_pager);
        myView = (MyView) view.findViewById(R.id.view_group);
        mainViewPagerAdapter = new MainViewPagerAdapter(getChildFragmentManager(), mainViewPagerImageFragments);
        pager.setAdapter(mainViewPagerAdapter);

        mPagerAction = new Runnable() {
            @Override
            public void run() {
                if (mItem != 0) {
                    if (isFrist) {
                        mCurrentItem = 0;
                        isFrist = false;
                    } else {
                        if (mCurrentItem == mainViewPagerImageFragments.size() - 1) {
                            mCurrentItem = 0;
                        } else {
                            mCurrentItem++;
                        }
                    }
                    pager.setCurrentItem(mCurrentItem);
                    myView.setCurrent(mCurrentItem);

                }
                pager.postDelayed(mPagerAction, 5000);
            }
        };
        pager.postDelayed(mPagerAction, 1000);

        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getInfo.G11();
        }
    };

    private void findid() {
        layout_zjx = (LinearLayout) view.findViewById(R.id.layout_zjx);
        layout_zjl = (LinearLayout) view.findViewById(R.id.layout_zjl);
        layout_k1 = (LinearLayout) view.findViewById(R.id.layout_k1);
        layout_k2 = (LinearLayout) view.findViewById(R.id.layout_k2);
        layout_k3 = (LinearLayout) view.findViewById(R.id.layout_k3);
        layout_k4 = (LinearLayout) view.findViewById(R.id.layout_k4);
        layout_shequ = (LinearLayout) view.findViewById(R.id.layout_shequ);
        layout_tuijian = (LinearLayout) view.findViewById(R.id.layout_tuijian);
        layout_cwh = (LinearLayout) view.findViewById(R.id.layout_cwh);
        layout_geren = (LinearLayout) view.findViewById(R.id.layout_geren);

        layout_zjx.setOnClickListener(this);
        layout_zjl.setOnClickListener(this);
        layout_k1.setOnClickListener(this);
        layout_k2.setOnClickListener(this);
        layout_k3.setOnClickListener(this);
        layout_k4.setOnClickListener(this);
        layout_shequ.setOnClickListener(this);
        layout_tuijian.setOnClickListener(this);
        layout_cwh.setOnClickListener(this);
        layout_geren.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_zjx:
                dialog = ProgressDialog.show(getActivity(), "提示", "正在拼命加载中...");
                dialog.setCanceledOnTouchOutside(true);
                if (mlatitude != null && mlongitude != null && mlatitude.trim().length() > 0 && mlongitude.trim().length() > 0) {
                    onKMBuutonClicked.onKMButtonClicked(DadaUrlPath.ZHAO_JX_JL
                            + "?phoneId=" + mGlobalData.mDEVICE_ID
                            + "&mobile=" + phoneNumber
                            + "&type=" + 0
                            + "&latitude=" + mlatitude
                            + "&longitude=" + mlongitude);
                } else {
                    double[] location = GetLocation.getGPS(getActivity());
                    onKMBuutonClicked.onKMButtonClicked(DadaUrlPath.ZHAO_JX_JL
                            + "?phoneId=" + mGlobalData.mDEVICE_ID
                            + "&mobile=" + phoneNumber
                            + "&type=" + 0
                            + "&latitude=" + location[0]
                            + "&longitude=" + location[1]);
                }

                break;
            case R.id.layout_zjl:
                dialog = ProgressDialog.show(getActivity(), "提示", "正在拼命加载中...");
                dialog.setCanceledOnTouchOutside(true);
                if (mlatitude != null && mlongitude != null && mlatitude.trim().length() > 0 && mlongitude.trim().length() > 0) {
                    onKMBuutonClicked.onKMButtonClicked(DadaUrlPath.ZHAO_JX_JL
                            + "?phoneId=" + mGlobalData.mDEVICE_ID
                            + "&mobile=" + phoneNumber
                            + "&type=" + 1
                            + "&latitude=" + mlatitude
                            + "&longitude=" + mlongitude);
                } else {
                    double[] location = GetLocation.getGPS(getActivity());
                    onKMBuutonClicked.onKMButtonClicked(DadaUrlPath.ZHAO_JX_JL
                            + "?phoneId=" + mGlobalData.mDEVICE_ID
                            + "&mobile=" + phoneNumber
                            + "&type=" + 1
                            + "&latitude=" + location[0]
                            + "&longitude=" + location[1]);
                }
                break;
            case R.id.layout_k1:
                onKMBuutonClicked.onKMButtonClicked(0);
                break;
            case R.id.layout_k2:
                onKMBuutonClicked.onKMButtonClicked(1);
                break;
            case R.id.layout_k3:
                onKMBuutonClicked.onKMButtonClicked(2);
                break;
            case R.id.layout_k4:
                onKMBuutonClicked.onKMButtonClicked(3);
                break;
            case R.id.layout_shequ:
                String path = DadaUrlPath.DADA_SHEQU + "?mobile=" + phoneNumber + "&phoneId=" + mGlobalData.mDEVICE_ID;
                intentActivity(Dada_User_falvTk.class, path, "嗒嗒社区");
                break;
            case R.id.layout_tuijian:
                showShare();
                break;
            case R.id.layout_cwh:
                String cwhpath = DadaUrlPath.CHE_WEN_HUA;
                intentActivity(Dada_User_falvTk.class, cwhpath, "车文化");
                break;
            case R.id.layout_geren:
                MainActivity.opencehua();
                break;
        }
    }

    /**
     * 双十一
     */
    private void showPupWind(String imageUrl, final String title, final String festivalUrl) {
        if (mEleven == null) {
            initWeiXin();
            View view_menu = LayoutInflater.from(getActivity()).inflate(R.layout.eleven, null, false);
            SimpleDraweeView eleven = (SimpleDraweeView) view_menu.findViewById(R.id.eleven_image);
            eleven.setImageURI(Uri.parse(imageUrl));
            eleven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEleven.dismiss();
                    intentActivity(Dada_User_falvTk.class, festivalUrl + "?mobile=" + phoneNumber
                            + "&phoneId=" + mGlobalData.mDEVICE_ID, title);
                }
            });
            mEleven = new PopupWindow(view_menu, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        mEleven.setFocusable(true);
        mEleven.setOutsideTouchable(true);
        mEleven.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mEleven.setAnimationStyle(R.style.MyExamTheme);
        mEleven.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEleven.showAtLocation(getActivity().findViewById(R.id.layout_tuijian), Gravity.BOTTOM, 0, 0);
        dimBackground(1.0f, 0.3f);
        mEleven.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dimBackground(0.3f, 1.0f);
            }
        });
    }

    @SuppressLint("NewApi")
    private void dimBackground(final float from, final float to) {
        final Window window = getActivity().getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();
    }

    private void showShare() {
        if (mPopupWindow_Share == null) {
            initWeiXin();
            View view_menu = LayoutInflater.from(getActivity()).inflate(R.layout.weixin, null, false);
            ImageButton imageButton_pyq = (ImageButton) view_menu.findViewById(R.id.imageButton_pyq);
            ImageButton imageButton_hy = (ImageButton) view_menu.findViewById(R.id.imageButton_hy);
            imageButton_pyq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow_Share.dismiss();
                    shareByPYQ();
                }
            });
            imageButton_hy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow_Share.dismiss();
                    shareByHY();
                }
            });
            mPopupWindow_Share = new PopupWindow(view_menu, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow_Share.setFocusable(true);
        mPopupWindow_Share.setOutsideTouchable(true);
        mPopupWindow_Share.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow_Share.setAnimationStyle(R.style.MyExamTheme);
        mPopupWindow_Share.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow_Share.showAtLocation(view.findViewById(R.id.view_home), Gravity.BOTTOM, 0, 0);
        dimBackground(1.0f, 0.3f);
        mPopupWindow_Share.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dimBackground(0.3f, 1.0f);
            }
        });
    }

    private void initWeiXin() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        String mobile = LoginID.getString("user_mobile", "");
        if (!mobile.isEmpty())
            URL_WeriXin += "?mobile=" + mobile;
        webpage.webpageUrl = URL_WeriXin;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = weixin_str;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.weixin_share);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
    }

    private void shareByPYQ() {
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private void shareByHY() {
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void intentActivity(Class c, String url, String content) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("webUrl", url);
        intent.putExtra("toobarbname", content);
        startActivity(intent);
    }

    private void intentActivity2(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    public void setOnKMBuutonClicked(OnKMBuutonClicked onKMBuutonClicked) {
        this.onKMBuutonClicked = onKMBuutonClicked;
    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 10:
                if (Message.contains("true")) {
                    mainViewPagerImageFragments.clear();
                    try {
                        HomeBanner homeBanner = (HomeBanner) Result;
                        List<HomeBanner.RowsEntity> rowsEntities = homeBanner.getRows();
                        mItem = rowsEntities.size();
                        for (HomeBanner.RowsEntity rowsEntity : rowsEntities) {
                            if (mlatitude != null && mlongitude != null && mlatitude.trim().length() > 0 && mlongitude.trim().length() > 0) {
                                mainViewPagerImageFragments.add(new MainViewPagerImageFragment(rowsEntity.getPhoto(),
                                        rowsEntity.getUrl()
                                                + "?phoneId=" + mGlobalData.mDEVICE_ID
                                                + "&mobile=" + phoneNumber
                                                + "&latitude=" + mlatitude
                                                + "&longitude=" + mlongitude,
                                        rowsEntity.getTitle()));
                            } else {
                                double[] location = GetLocation.getGPS(getActivity());
                                mainViewPagerImageFragments.add(new MainViewPagerImageFragment(rowsEntity.getPhoto(),
                                        rowsEntity.getUrl()
                                                + "?phoneId=" + mGlobalData.mDEVICE_ID
                                                + "&mobile=" + phoneNumber
                                                + "&latitude=" + location[0]
                                                + "&longitude=" + location[1],
                                        rowsEntity.getTitle()));
                            }
                        }
                        myView.setLength(mItem);
                        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(final int arg0) {
                                mCurrentItem = arg0 % mainViewPagerImageFragments.size();
                                myView.setCurrent(mCurrentItem);
                            }

                            @Override
                            public void onPageScrolled(int arg0, float arg1, int arg2) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int arg0) {

                            }
                        });
                        mainViewPagerAdapter = new MainViewPagerAdapter(getChildFragmentManager(), mainViewPagerImageFragments);
                        pager.setAdapter(mainViewPagerAdapter);

                    } catch (ClassCastException c) {
                        Log.v("show", "类转换异常");
                    }
                }
                break;


            case 11:
                if (Message.contains("true")) {
                    try {
                        Festival festival = (Festival) Result;
                        if(festival.getIsShow()){
                            showPupWind(festival.getPhoto(),festival.getTitle(),festival.getUrl());
                        }
                    } catch (ClassCastException c) {
                        Log.v("show", "类转换异常");
                    }
                }

                break;
        }
    }

    public void refreshBanner() {
        getInfo.G10();
    }

    @Override
    public void postInfoCallBack(Integer id, String Message) {
        
    }

    public interface OnKMBuutonClicked {
        void onKMButtonClicked(int position);

        void onKMButtonClicked(String url);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("home"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("home");
    }
}
