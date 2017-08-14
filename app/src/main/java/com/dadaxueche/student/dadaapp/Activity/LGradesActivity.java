package com.dadaxueche.student.dadaapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dadaxueche.student.dadaapp.Adapter.GradeBean;
import com.dadaxueche.student.dadaapp.Adapter.LGradeAdapter;
import com.dadaxueche.student.dadaapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 我的成绩
 */
public class LGradesActivity extends AppCompatActivity {
    private MyDataBase database = new MyDataBase();
    private ArrayList<GradeBean> info = new ArrayList<>();
    private String flag_Select_KM_Type = "KM_Type";
    private String KM = "";
    private GradeBean bean;
    private int evaluate;
    private ListView mlistView;
    private LGradeAdapter adapter;
    private Button seemoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_grades);
        KM = getIntent().getStringExtra(flag_Select_KM_Type);
        // 制作7个数据点（沿x坐标轴）
        init();
        LineChart chart = (LineChart) findViewById(R.id.chart);
        LineData mLineData = makeLineData(info.size());
        setChartStyle(chart, mLineData, Color.rgb(118, 212, 66));

    }

    private void init() {
        getExamResultList();
        mlistView = (ListView) findViewById(R.id.grade_list);
        TextView title = (TextView) findViewById(R.id.toolbar_content);
        title.setText(R.string.title_activity_my_grades_title);
        adapter = new LGradeAdapter(this, info);
        mlistView.setAdapter(adapter);
        findViewById(R.id.toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seemoreBtn = (Button) findViewById(R.id.see_more_btn);
        seemoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LGradesActivity.this, LEditGrade.class);
                intent.putExtra("KM", KM);
                startActivityForResult(intent, 123);
            }
        });
    }

    // 设置chart显示的样式
    private void setChartStyle(LineChart mLineChart, LineData lineData,
                               int color) {
        mLineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 让x轴在下面
        mLineChart.setDescription("");// 数据描述
        mLineChart.getXAxis().setDrawGridLines(false);//设置x轴线消失
//        mLineChart.getAxisLeft().setDrawGridLines(false);//设置y轴线消失
        mLineChart.getAxisLeft().setGridColor(Color.WHITE);//y轴上的线的颜色
        mLineChart.getAxisLeft().setAxisLineColor(Color.WHITE);//y轴颜色
        mLineChart.getXAxis().setAxisLineColor(Color.WHITE);//X轴颜色
//        mLineChart.getXAxis().setGridColor(Color.WHITE);

        mLineChart.setDrawGridBackground(false);
        // 触摸
        mLineChart.setTouchEnabled(true);
        // 拖拽
        mLineChart.setDragEnabled(true);
        // 缩放
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(false);
        // 设置背景
        mLineChart.setBackgroundColor(color);
        // 设置x,y轴的数据
        mLineChart.setData(lineData);
        // 设置比例图标示，就是那个一组y的value的
//        Legend mLegend = mLineChart.getLegend();
//        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//        mLegend.setForm(Legend.LegendForm.SQUARE);// 样式
//        mLegend.setFormSize(15.0f);// 字体
//        mLegend.setTextColor(Color.BLUE);// 颜色
        // 沿x轴动画，时间2000毫秒。
        mLineChart.animateX(2000);
//        mLineChart.animateY(2000);
    }

    /**
     * @param count 数据点的数量。
     * @return
     */
    private LineData makeLineData(int count) {
        ArrayList<String> x = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            // x轴显示的数据
            x.add("" + i);
        }
        // y轴的数据
        ArrayList<Entry> y = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = Float.parseFloat(info.get(i).getGrade());
//            fenshu = new ArrayList<>();
            Entry entry = new Entry(val, i);
            y.add(entry);
        }
        // y轴数据集
        LineDataSet mLineDataSet = new LineDataSet(y, "最近十次成绩记录");
        // 用y轴的集合来设置参数
        // 线宽
        mLineDataSet.setLineWidth(1.0f);
        // 显示的圆形大小
        mLineDataSet.setCircleSize(3.0f);
        // 折线的颜色
        mLineDataSet.setColor(Color.WHITE);
        // 圆球的颜色
        mLineDataSet.setCircleColor(Color.WHITE);
        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
//         mLineDataSet.set
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
//        mLineDataSet.setDrawHighlightIndicators(true);
        // 按击后，十字交叉线的颜色
        mLineDataSet.setHighLightColor(Color.CYAN);
        // 设置这项上显示的数据点的字体大小。
//        mLineDataSet.setValueTextSize(10.0f);
        // mLineDataSet.setDrawCircleHole(true);
        // 改变折线样式，用曲线。
        mLineDataSet.setDrawCubic(true);
        // 默认是直线
        // 曲线的平滑度，值越大越平滑。
        // mLineDataSet.setCubicIntensity(0.2f);
        // 填充曲线下方的区域，红色，半透明。
        // mLineDataSet.setDrawFilled(true);
        // mLineDataSet.setFillAlpha(128);
        // mLineDataSet.setFillColor(Color.RED);
        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
//        mLineDataSet.setCircleColorHole(Color.YELLOW);
        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
//        mLineDataSet.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                int n = (int) value;
//                String s = "y:" + n;
//                return s;
//            }
//        });

        ArrayList<LineDataSet> mLineDataSets = new ArrayList<>();
        mLineDataSets.add(mLineDataSet);
        LineData mLineData = new LineData(x, mLineDataSets);
        return mLineData;
    }


    public void getExamResultList() {
        info.clear();
        Cursor cursor = database.queryExamResult(KM);
        while (cursor.moveToNext()) {
            bean = new GradeBean();
            bean.setBegintime(cursor.getString(2));
            bean.setUsetime(getusettime(cursor.getString(2), cursor.getString(3)));
            bean.setGrade(cursor.getString(6));
            evaluate = Integer.valueOf(cursor.getString(6));
            if (evaluate < 30) {
                bean.setEvaluate("学沫");
            } else if (30 <= evaluate && evaluate < 60) {
                bean.setEvaluate("学渣");
            } else if (60 <= evaluate && evaluate < 90) {
                bean.setEvaluate("学民");
            } else if (90 <= evaluate && evaluate < 100) {
                bean.setEvaluate("学民");
            }
            info.add(bean);
        }
    }


    private String getusettime(String str, String str1) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("m分ss秒");
        try {
            return df2.format(df.parse(str1).getTime() - df.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            getExamResultList();
            adapter.notifyDataSetChanged();
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }}