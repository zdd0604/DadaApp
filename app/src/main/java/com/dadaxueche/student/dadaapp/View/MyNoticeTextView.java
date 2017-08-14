package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dada.mylibrary.GetData;
import com.dada.mylibrary.Gson.Notice;
import com.dadaxueche.student.dadaapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpf on 9-30-0030.
 */
public class MyNoticeTextView extends TextView implements GetData.GetResultCallBack {

    private int currentGG = 0;
    private int sleepTime = 3000;
    private GetData mGetData = new GetData();
    private List<Notice.RowsEntity> ggList = new ArrayList<>();
    private Thread mThread;
    private GetNotice getNotice;

    public MyNoticeTextView(Context context) {
        super(context);
    }

    public MyNoticeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGetData.setGetResultCallBack(this);
        mGetData.getNotice();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.turnup);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            setText(ggList.get(currentGG++).getContent());
                            if (currentGG == ggList.size())
                                currentGG = 0;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    startAnimation(animation);
                }
            }
        };

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!ggList.isEmpty()) {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public void getResultCallBack(String ID, Object object, String Message) {
        if(Message.contains("成功")) {
            switch (ID) {
                case "notice":
                    ggList.clear();
                    ggList.addAll(((Notice) object).getRows());
                    mThread.setDaemon(true);
                    mThread.start();
                    getNotice.getNotice(true);
                    break;
            }
        } else {
            if(getNotice != null)
                getNotice.getNotice(false);
            else
                Log.i("错误","未设置接口");
        }
    }

    @Override
    public void extractSuccess(String ID, String Message) {

    }

    public void setGetNotice(GetNotice getNotice) {
        this.getNotice = getNotice;
    }

    public interface GetNotice {
        void getNotice(boolean bool);

    }
}
