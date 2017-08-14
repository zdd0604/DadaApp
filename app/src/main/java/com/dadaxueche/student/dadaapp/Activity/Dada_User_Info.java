package com.dadaxueche.student.dadaapp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetInfo;
import com.dada.mylibrary.Gson.ErrorInfo;
import com.dada.mylibrary.Gson.UserInfo;
import com.dada.mylibrary.Util.CHttpClient;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.View.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Dada_User_Info extends AppCompatActivity implements View.OnClickListener,
        GetInfo.GetResultCallBack {


    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private ListView listview_my_info;
    private List<String> list_title = new ArrayList<>();
    private List<String> list_content = new ArrayList<>();
    private ImageView user_info_hand;



    private GetInfo getInfo;
    private GlobalData mGlobalData;
    private UserInfo mUserInfo;
    private MyDataBase myDataBase = new MyDataBase();
    private String name, mobile, identityId, UserInfo_photo;
    private Bitmap bgetHttpBitmap;
    public SharedPreferences LoginID;
    public static SharedPreferences.Editor editor;
    private String usermobile;
    public static String server_user_hand;
    private Check cd;
    private Button user_out_login;
    private boolean sdCardExist = Environment.getExternalStorageState()
            .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    private ProgressDialog dialog = null;
    private Thread thread;
    private Timer timer;
    private static final int TIME_OUT = 0;
    private static final int SUCCESS = 1;
    // 超时的时限为5秒
    private static final int TIME_LIMIT = 15000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__info);
        LoginID = getSharedPreferences("isLogin", 0);
        editor = LoginID.edit();
        usermobile = LoginID.getString("user_mobile", "");
        cd = new Check(getApplicationContext());
        mGlobalData = (GlobalData) getApplication();
        getInfo = new GetInfo();
        getInfo.setGetResultCallBack(this);
        init();
    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_Info.this, "提示", "正在拼命加载中...");
        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        toolbar_content.setText("个人资料");
        toobar_back.setOnClickListener(this);
        listview_my_info = (ListView) findViewById(R.id.listview_my_info);
        //用户头像
        user_info_hand = (ImageView) findViewById(R.id.user_info_hand);
        user_info_hand.setOnClickListener(this);
        user_out_login = (Button) findViewById(R.id.user_out_login);
        user_out_login.setOnClickListener(this);
        if (cd.isConnectingToInternet()) {
            // 匿名内部线程
            thread = new Thread() {
                @Override
                public void run() {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    getInfo.G03(usermobile, mGlobalData.mDEVICE_ID, date);
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
            String handpath = LoginID.getString("iamgeLocation", "");
            File file = new File(handpath);
            if (file.exists()) {
                Bitmap userhand = getLoacalBitmap(handpath);
                user_info_hand.setImageBitmap(userhand);
            } else {
                user_info_hand.setImageResource(R.mipmap.wodech);
            }
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
    }


    private void setUserHand(String photoNuber) {
        Cursor cursor = myDataBase.queryUserInfo(photoNuber);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("UserInfo_name"));
                mobile = cursor.getString(cursor.getColumnIndex("UserInfo_mobile"));
                identityId = cursor.getString(cursor.getColumnIndex("UserInfo_identityId"));
                UserInfo_photo = cursor.getString(cursor.getColumnIndex("UserInfo_photo"));
            }

            if (UserInfo_photo == null) {
                user_info_hand.setImageResource(R.mipmap.wodech); //设置Bitmap
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
            initListData(identityId, name, mobile);
            dialog.dismiss();
            cursor.close();
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
                    user_info_hand.setImageBitmap(data);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_hand:
                ShowPickDialog();
                break;
            case R.id.toobar_lift_back:
                finish();
                break;
            case R.id.user_out_login:
                editor.putInt("islonginId", 0);
                myDataBase.deleteUserInfo(usermobile);
                myDataBase.deleteSchoolInfo(usermobile);
                editor.clear();
                editor.commit();
                MobclickAgent.onProfileSignOff();
                finish();
                break;
        }
    }

    /**
     * 选择提示对话框
     */
    private void ShowPickDialog() {
        new AlertDialog.Builder(this)
                .setTitle("设置头像...")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 1);

                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(DadaUrlPath.SDCARD_PHOTO);
                        if (file.exists()) {
                            //下面这句指定调用相机拍照后的照片存储的路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DadaUrlPath.SDCARD_PHOTO + "/dada.jpg")));
                            startActivityForResult(intent, 2);
                        } else {
                            file.mkdirs();
                            //下面这句指定调用相机拍照后的照片存储的路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DadaUrlPath.SDCARD_PHOTO + "/dada.jpg")));
                            startActivityForResult(intent, 2);
                        }

                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                startPhotoZoom(data.getData());
                break;
            // 如果是调用相机拍照时
            case 2:
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {
                    File filePhoto = new File(DadaUrlPath.SDCARD_PHOTO + "/dada.jpg");
                    startPhotoZoom(Uri.fromFile(filePhoto));
                } else {
                    Tools.showToast(this, "SD卡不能用");
                }

                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            //获取最后截图
            Bitmap photo = extras.getParcelable("data");
            File file = new File(DadaUrlPath.SDCARD_PHOTO + "/user_hand.png");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                setBitmapUpload(DadaUrlPath.SDCARD_PHOTO + "/user_hand.png");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Drawable drawable = new BitmapDrawable(photo);
            user_info_hand.setImageDrawable(drawable);
        }
    }

    /*
   * 上传图片
   * */
    public void setBitmapUpload(final String imgfile) {
        RequestParams mParams = new RequestParams();
        //URL参数
        mParams.addBodyParameter("mobile", usermobile);
        mParams.addBodyParameter("phoneId", mGlobalData.mDEVICE_ID);
        mParams.addBodyParameter("ufile", new File(imgfile));
        HttpUtils mHttpUtils = new HttpUtils();
        mHttpUtils.send(HttpRequest.HttpMethod.POST, DadaUrlPath.CHANG_PHOTO_POST,
                mParams, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        Tools.showToast(Dada_User_Info.this, "开始上传");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        Tools.showToast(Dada_User_Info.this, "上传中");
                        if (!arg0.result.equals("")) {
                            String c = arg0.result;
                            ErrorInfo mErrorInfo = new Gson().fromJson(c, new TypeToken<ErrorInfo>() {
                            }.getType());
                            Tools.showToast(Dada_User_Info.this, "上传成功");
                            File filePhoto = new File(DadaUrlPath.SDCARD_PHOTO + "/dada.png");
                            if (filePhoto.exists()) {
                                filePhoto.delete();
                            }
                        } else {
                            Log.e("show", "没有返回值");
                        }
                    }
                });
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Bitmap转换成照片
     */

    public static void getBitmapImageFile(Bitmap bitmap, String imageName) {
        server_user_hand = DadaUrlPath.SDCARD_PHOTO + "/" + imageName + ".png";
        editor.putString("iamgeLocation", server_user_hand);
        editor.commit();
        File bitmapFile = new File(server_user_hand);
        File fileFile = new File(DadaUrlPath.SDCARD_PHOTO);
        FileOutputStream bitmapWtriter = null;
        try {
            if (fileFile.exists()) {
                bitmapWtriter = new FileOutputStream(bitmapFile);
            } else {
                fileFile.mkdirs();
                bitmapWtriter = new FileOutputStream(bitmapFile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmapFile.exists()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bitmapWtriter);
        }
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() !=
                PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    @Override
    public void getResultCallBack(Integer id, Object Result, String Message) {
        switch (id) {
            case 3:
                if (Message.contains("true")) {
                    try {
                        dialog.dismiss();
                        mUserInfo = (UserInfo) Result;

                        initListData(mUserInfo.getEntity().getIdentityId(),
                                mUserInfo.getEntity().getName(),
                                mUserInfo.getEntity().getMobile());

                        String handpath = LoginID.getString("iamgeLocation", "");
                        File file = new File(handpath);
                        if (file.exists()) {
                            Bitmap userhand = Dada_User_Info.getLoacalBitmap(handpath);
                            user_info_hand.setImageBitmap(userhand);
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    bgetHttpBitmap = CHttpClient.getHttpBitmap(DadaUrlPath.Pulic_URL + mUserInfo.getEntity().getPhoto());
                                    if (mUserInfo.getEntity().getPhoto() != null) {
                                        if (sdCardExist) {
                                            if (bgetHttpBitmap != null) {
                                                getBitmapImageFile(bgetHttpBitmap, "user_hand");
                                            }
                                        } else {
                                            Tools.showToast(Dada_User_Info.this, "未检测到SD卡");
                                        }
                                    } else {
//                                      user_info_hand.setImageResource(R.mipmap.wode);
                                    }

                                    //需要数据传递，用下面方法；
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = bgetHttpBitmap;//可以是基本类型，可以是对象，可以是List、map等；
                                    mHandler.sendMessage(msg);
                                }
                            }).start();
                        }

                        Message msgSuc = new Message();
                        msgSuc.what = SUCCESS;
                        myHandler.sendMessage(msgSuc);

                    } catch (ClassCastException e) {
                        Log.v("show", "类转换异常：" + Result.getClass().getName());
                    }
                } else {
                    setUserHand(usermobile);
                }
                break;
        }
    }

    private void initListData(String useridentityId, String username, String usermobile) {
        list_title.clear();
        list_content.clear();

        list_title.add("身份证");
        list_content.add(useridentityId);

        list_title.add("姓名");
        list_content.add(username);

        list_title.add("手机");
        list_content.add(usermobile);
        listview_my_info.setAdapter(new MyAdapter(list_title, list_content));
    }

    private class MyAdapter extends BaseAdapter {

        private List<String> list_ada_title;
        private List<String> list_ada_content;

        public MyAdapter(List<String> list_ada_title, List<String> list_ada_content) {
            this.list_ada_title = list_ada_title;
            this.list_ada_content = list_ada_content;
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
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_adapter_myinfo, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.user_ada_title);
                viewHolder.content = (TextView) convertView.findViewById(R.id.user_ada_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.title.setText(list_ada_title.get(position));
            viewHolder.content.setText(list_ada_content.get(position));

            return convertView;
        }

        private class ViewHolder {
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
                    Toast.makeText(Dada_User_Info.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                    String handpath = LoginID.getString("iamgeLocation", "");
                    File file = new File(handpath);
                    if (file.exists()) {
                        Bitmap userhand = getLoacalBitmap(handpath);
                        user_info_hand.setImageBitmap(userhand);
                    } else {
                        user_info_hand.setImageResource(R.mipmap.wodech);
                    }
                    setUserHand(usermobile);
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
}
