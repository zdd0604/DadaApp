package com.dadaxueche.student.dadaapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dadaxueche.student.dadaapp.Adapter.GradeBean;
import com.dadaxueche.student.dadaapp.Adapter.LGradeEditAdapter;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class LEditGrade extends AppCompatActivity implements View.OnClickListener {
    private TextView title, edit;
    private ListView mlistView;
    private LGradeEditAdapter adapter;
    private LinearLayout edit_linear;
    private MyDataBase database = new MyDataBase();
    private ArrayList<GradeBean> info = new ArrayList<>();
    private String flag_Select_KM_Type = "KM";
    private String KM = "";
    private GradeBean bean;
    private int evaluate;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledit_grade);
        KM = getIntent().getStringExtra(flag_Select_KM_Type);
        init();
    }

    void init() {
        getExamResultList();
        title = (TextView) findViewById(R.id.details_content);
        edit_linear = (LinearLayout) findViewById(R.id.edit_linear);
        title.setText(R.string.title_activity_my_grades_details);
        mlistView = (ListView) findViewById(R.id.edit_grade_list);
        adapter = new LGradeEditAdapter(this, info);
        edit = (TextView) findViewById(R.id.edit);
        edit.setOnClickListener(this);
        mlistView.setAdapter(adapter);
        findViewById(R.id.selectBtn).setOnClickListener(this);
        findViewById(R.id.deleteBtn).setOnClickListener(this);
        findViewById(R.id.details_back).setOnClickListener(this);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                if (GlobalData.getInstance().flag) {
                    edit.setText(R.string.title_activity_my_grades_edit);
                    edit_linear.setVisibility(View.GONE);
                    GlobalData.getInstance().flag = false;
                    adapter.notifyDataSetChanged();
                } else {
                    edit.setText(R.string.title_activity_my_grades_edit_cancle);
                    edit_linear.setVisibility(View.VISIBLE);
                    GlobalData.getInstance().flag = true;
                    for (int i = 0; i < info.size(); i++) {
                        GradeBean bean = info.get(i);
                        bean.canRemove = true;
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.selectBtn:
                for (int i = 0; i < info.size(); i++) {
                    adapter.getIsSelected().put(i, true);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.deleteBtn:
                Map<Integer, Boolean> map = adapter.getIsSelected();
                int count = adapter.getCount();
                for (int i = 0; i < count; i++) {
                    int position = i - (count - adapter.getCount());
                    if (map.get(i) != null && map.get(i)) {
//                        GradeBean bean = (GradeBean) adapter.getItem(position);
//                        if (bean.isCanRemove()) {
                        if(database.deleteExamResult(info.get(i).getBegintime())){
                            adapter.getIsSelected().remove(i);
                            adapter.remove(position);
                        }
//                        } else {
//                            map.put(position, false);
//                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.details_back:
                GlobalData.getInstance().flag = false;
                Intent intent = new Intent(LEditGrade.this, LGradesActivity.class);
                this.setResult(123,intent);
                finish();
                break;
        }
    }

    public void getExamResultList() {
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
            return df2.format(df.parse(str1).getTime()-df.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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
