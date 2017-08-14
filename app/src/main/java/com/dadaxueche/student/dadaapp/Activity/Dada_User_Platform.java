package com.dadaxueche.student.dadaapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

public class Dada_User_Platform extends AppCompatActivity implements View.OnClickListener {
    private TextView toolbar_content;
    private LinearLayout toobar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__platform);
        init();
    }

    private void init() {
        toobar_back = (LinearLayout) findViewById(R.id.toobar_back);
        toolbar_content = (TextView) findViewById(R.id.toolbar_content);
        toolbar_content.setText("嗒嗒承若");
        toobar_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_back:
                finish();
                break;
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
