package com.dadaxueche.student.dadaapp.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.View.PieGraph;
import com.dadaxueche.student.dadaapp.View.PieSlice;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LPracticeStatisticalActivity extends AppCompatActivity {
    private TextView percent1, percent2, percent3, percent4, topicnum1, topicnum2, topicnum3, topicnum4, title, finishbfb;
    private MyDataBase database = new MyDataBase();
    private String flag_Select_KM_Type = "KM_Type";
    private String KM = "";
    private float yzts, cwts, zqts, wzts, zts;//总体书/已做题数/错误题数/正确题数/未做题数/
    private float yzbfb, cwbfb, zqbfb, wzbfb;//百分比
    private Cursor cursor;
    private DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpractice_statistical);
        KM = getIntent().getStringExtra(flag_Select_KM_Type);
        df.applyPattern("#.#%");
        init();
    }

    private void init() {
        finishbfb = (TextView) findViewById(R.id.finishbfb);
        topicnum1 = (TextView) findViewById(R.id.topicnum1);
        topicnum2 = (TextView) findViewById(R.id.topicnum2);
        topicnum3 = (TextView) findViewById(R.id.topicnum3);
        topicnum4 = (TextView) findViewById(R.id.topicnum4);
        percent1 = (TextView) findViewById(R.id.percent1);
        percent2 = (TextView) findViewById(R.id.percent2);
        percent3 = (TextView) findViewById(R.id.percent3);
        percent4 = (TextView) findViewById(R.id.percent4);
        title = (TextView) findViewById(R.id.toolbar_content);
        title.setText(R.string.my_practice);
        findViewById(R.id.toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cursor = database.queryExamByKM(KM);
        zts = cursor.getCount();
        cursor.close();
        cursor = database.queryRight(KM);
        zqts = cursor.getCount();
        cursor.close();
        cursor = database.queryError(KM);
        cwts = cursor.getCount();
        yzts = zqts + cwts;
        wzts = zts - yzts;
        if (zts != 0) {
            yzbfb = (yzts / zts);
            wzbfb = (wzts / zts);
        } else {
            yzbfb = 0;
            wzbfb = 0;
        }
        if (yzts != 0) {
            cwbfb = (cwts / zts);
            zqbfb = (zqts / zts);
        } else {
            cwbfb = 0;
            zqbfb = 0;
        }
        topicnum1.setText(getStringByInt(yzts));
        topicnum2.setText(getStringByInt(zqts));
        topicnum3.setText(getStringByInt(cwts));
        topicnum4.setText(getStringByInt(wzts));
        percent1.setText(df.format(yzbfb));
        percent2.setText(df.format(zqbfb));
        percent3.setText(df.format(cwbfb));
        percent4.setText(df.format(wzbfb));
        finishbfb.setText("已完成" + df.format(yzbfb));
        float wz = wzbfb * 1000;
        float zq = zqbfb * 1000;
        float cw = cwbfb * 1000;
        setPieGraph(wz, zq, cw);
    }

    void setPieGraph(float nodo, float yes, float no) {
        PieGraph pg = (PieGraph) findViewById(R.id.piegraph);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.rgb(212, 211, 211));
        slice.setValue(nodo);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.rgb(114, 206, 61));
        slice.setValue(yes);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.rgb(252, 134, 49));
        slice.setValue(no);
        pg.addSlice(slice);
    }

    public String getStringByInt(float i) {
        int k = (int) i;
        String str = String.valueOf(k);
        return str;
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