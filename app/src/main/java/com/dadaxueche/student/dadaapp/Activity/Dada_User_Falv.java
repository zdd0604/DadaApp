package com.dadaxueche.student.dadaapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

public class Dada_User_Falv extends AppCompatActivity implements View.OnClickListener{
    private WebView user_falv;
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private Button dada_fwxy,dada_xyht,dada_mzsm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__falv);
        init();
    }

    private void init(){
        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toobar_back.setOnClickListener(this);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        toolbar_content.setText("法律条款");
        dada_fwxy = (Button) findViewById(R.id.dada_fwxy);
        dada_xyht = (Button) findViewById(R.id.dada_xyht);
        dada_mzsm = (Button) findViewById(R.id.dada_mzsm);
        dada_mzsm.setOnClickListener(this);
        dada_xyht.setOnClickListener(this);
        dada_fwxy.setOnClickListener(this);

    }

    private void intentActivity(Class c,String webUrl,String toobarbname){
        Intent intent = new Intent(Dada_User_Falv.this,c);
        intent.putExtra("webUrl", webUrl);
        intent.putExtra("toobarbname", toobarbname);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dada_fwxy:
                intentActivity(Dada_User_falvTk.class, DadaUrlPath.DADA_FWXY,"嗒嗒学车服务协议");
                break;
            case R.id.dada_xyht:
                intentActivity(Dada_User_falvTk.class,DadaUrlPath.DADA_XYHT,"嗒嗒学车学员合同");
                break;
            case R.id.dada_mzsm:
                intentActivity(Dada_User_falvTk.class,DadaUrlPath.DADA_FFSM,"嗒嗒学车免责声明");
                break;
            case R.id.toobar_lift_back:
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
