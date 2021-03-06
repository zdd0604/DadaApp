package com.dadaxueche.student.dadaapp.Activity;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class LK1CollectActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDataBase database = new MyDataBase();
    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Index = "Index";
    private String flag_Type = "Type";
    private String KM = "";
    private String Type = "我的收藏";
    private TextView m0, m1, m2, m3, m4, m5;
    private TextView[] textViews = new TextView[6];
    private TextView t1, t2, t3, t4, t5, t6, clear;
    private List<String> mList = new ArrayList<>();
    private Intent intent;
    int[] count = new int[]{0, 0, 0, 0, 0};
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lk1_collect);
        KM = getIntent().getStringExtra(flag_Select_KM_Type);
        init();
        getData();
    }

    void init() {
        t1 = (TextView) findViewById(R.id.k1public_text1);
        t2 = (TextView) findViewById(R.id.k1public_text2);
        t3 = (TextView) findViewById(R.id.k1public_text3);
        t4 = (TextView) findViewById(R.id.k1public_text4);
        t5 = (TextView) findViewById(R.id.k1public_text5);
        t6 = (TextView) findViewById(R.id.k1public_text6);
        m0 = (TextView) findViewById(R.id.k1public_num1);
        m1 = (TextView) findViewById(R.id.k1public_num2);
        m2 = (TextView) findViewById(R.id.k1public_num3);
        m3 = (TextView) findViewById(R.id.k1public_num4);
        m4 = (TextView) findViewById(R.id.k1public_num5);
        m5 = (TextView) findViewById(R.id.k1public_num6);
        clear = (TextView) findViewById(R.id.clear_my_collect);
        t1.setText(R.string.all_my_collect_topic);
        t2.setText(R.string.stfl8);
        t3.setText(R.string.stfl9);
        t4.setText(R.string.stfl10);
        t5.setText(R.string.stfl11);
        t6.setText(R.string.stfl12);
        textViews[0] = m1;
        textViews[1] = m2;
        textViews[2] = m3;
        textViews[3] = m4;
        textViews[4] = m5;
        title = (TextView) findViewById(R.id.toolbar_content);
        title.setText(R.string.my_collect);
        findViewById(R.id.toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
    }


    void getData() {
        count = new int[]{0, 0, 0, 0, 0};
        mList.clear();
        Cursor cursor = database.queryCollect(KM);
        while (cursor.moveToNext()) {
            mList.add(cursor.getString(0));
        }
        cursor.close();
        for (String stxh : mList) {
            cursor = database.queryExam(stxh);
            cursor.moveToFirst();
            int i = Integer.valueOf(cursor.getString(cursor.getColumnIndex("Exam_stfl"))) - 1;
            count[i]++;
        }
        cursor.close();
        m0.setText("（" + String.valueOf(mList.size()) + "）");
        for (int i = 0; i < count.length; ++i) {
            textViews[i].setText("（" + String.valueOf(count[i]) + "）");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.k1layout1:
                if (mList.size() > 0) {
                    intent = new Intent(LK1CollectActivity.this, ExamActivity.class);
                    intent.putExtra(flag_Type, Type);
                    intent.putExtra(flag_Index, 0);
                    intent.putExtra(flag_Select_KM_Type, KM);
                    startActivity(intent);
                    break;
                }
                Toast.makeText(this, "没有此类型的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.k1layout2:
                if (setIntent(0)) {
                    break;
                }
                Toast.makeText(this, "没有此类型的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.k1layout3:
                if (setIntent(1)) {
                    break;
                }
                Toast.makeText(this, "没有此类型的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.k1layout4:
                if (setIntent(2)) {
                    break;
                }
                Toast.makeText(this, "没有此类型的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.k1layout5:
                if (setIntent(3)) {
                    break;
                }
                Toast.makeText(this, "没有此类型的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.k1layout6:
                if (setIntent(4)) {
                    break;
                }
                Toast.makeText(this, "没有此类型的收藏", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LK1CollectActivity.this);
        builder.setMessage("确认清空吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (database.deleteAllCollect(KM)) {
                    Toast.makeText(LK1CollectActivity.this, "清空成功", Toast.LENGTH_SHORT).show();
                    mList.clear();
                    for (int i = 0; i < count.length; i++) {
                        count[i] = 0;
                    }
                    getData();
                } else {
                    Toast.makeText(LK1CollectActivity.this, "清空失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public boolean getSize(int k) {
        return k > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(1000);
    }

    public boolean setIntent(int l) {
        if (getSize(count[l])) {
            intent = new Intent(LK1CollectActivity.this, ExamActivity.class);
            intent.putExtra(flag_Type, Type);
            intent.putExtra(flag_Index, l + 1);
            intent.putExtra(flag_Select_KM_Type, KM);
            startActivityForResult(intent, 0);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            getData();
        }
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