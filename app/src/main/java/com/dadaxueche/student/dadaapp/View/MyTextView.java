package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wpf on 8-26-0026.
 */
public class MyTextView extends TextView {
    private Date mDate;
    private Timer timer = new Timer();
    private TimerTask task;
    private Handler handler;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    private TimeOver timeOver;
    private Time mTime;
    private boolean cando = true;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTimer();
    }

    private void initTimer() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(cando && msg.what == 1) {
                    try {
                        mDate.setTime(mDate.getTime() - simpleDateFormat.parse("00:01").getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String time = simpleDateFormat.format(mDate);
                    setText(time);
                    mTime.Time(time);
                    if(time.equals("00:00")) {
                        task.cancel();
                        timeOver.TimeOver();
                    }
                } else {
                    task.cancel();
                }
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
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
        setText(simpleDateFormat.format(mDate));
        timer.schedule(task, 1000, 1000);
    }

    public void setTimeText(String Time) {
        setText(Time);
    }

    public void setCando(boolean cando) {
        this.cando = cando;
    }

    public void setTimeOver(TimeOver timeOver) {
        this.timeOver = timeOver;
    }

    public void setTime(Time time) {
        this.mTime = time;
    }

    public interface TimeOver {
        void TimeOver();
    }

    public interface Time {
        void Time(String Time);
    }
}
