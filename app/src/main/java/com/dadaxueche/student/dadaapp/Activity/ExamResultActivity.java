package com.dadaxueche.student.dadaapp.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetData;
import com.dada.mylibrary.Gson.Mark;
import com.dada.mylibrary.Util.Get;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExamResultActivity extends AppCompatActivity implements View.OnClickListener ,
        GetData.PostResultCalBack {

    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Select_CX_Type = "CX_Type";
    private String flag_KSCJ = "KSCJ";
    private String flag_KSSJ = "KSSJ";
    private String flag_JSSJ = "JSSJ";

    private String CX,KM,str_KSSJ,str_JSSJ;
    private int KSCJ;
    private Date KSSJ,JSSJ;
    private String[] CXS = new String[]{"小车","货车","客车"};

    private TextView mTextView_KM,mTextView_CX,mTextView_KSCJ,mTextView_KSSJ,mTextView_Title,mTextView_Ranking;
    private CircleImageView mCircleImageView;
    private Button mButton_px,mButton_ctjx,mButton_lsjl,mButton_again;
    private ImageButton mImageButton_Retuen;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GetData getData = new GetData();
    private GlobalData mGlobalData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);
        mGlobalData = (GlobalData) getApplication();
        getData.setmPostResultCalBack(this);
        mTextView_Ranking = (TextView) findViewById(R.id.textView_ranking);
        mTextView_Title = (TextView) findViewById(R.id.actionbar_Title);
        mTextView_Title.setVisibility(View.GONE);
        mImageButton_Retuen = (ImageButton) findViewById(R.id.imageButton_return);
        mImageButton_Retuen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        KM = intent.getStringExtra(flag_Select_KM_Type);
        String km = "1",cx;
        if(KM.equals("1")) {
            km = "科目一";
        } else if(KM.equals("4")) {
            km = "科目四";
        }
        CX = intent.getStringExtra(flag_Select_CX_Type);
        cx = CXS[getCX(CX)];
        KSCJ = intent.getIntExtra(flag_KSCJ, 0);

        str_KSSJ = intent.getStringExtra(flag_KSSJ);
        str_JSSJ = intent.getStringExtra(flag_JSSJ);
        try {
            this.KSSJ = simpleDateFormat.parse(str_KSSJ);
            this.JSSJ = simpleDateFormat.parse(str_JSSJ);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        init();

        mTextView_CX = (TextView) findViewById(R.id.textView_CX);
        mTextView_KM = (TextView) findViewById(R.id.textView_KM);
        mTextView_KSCJ = (TextView) findViewById(R.id.textView_mark);
        mTextView_KSSJ = (TextView) findViewById(R.id.textView_ExamTime);
        mCircleImageView = (CircleImageView) findViewById(R.id.imageView_mark);
        mButton_px = (Button) findViewById(R.id.button_px);
        mButton_ctjx = (Button) findViewById(R.id.button_ctjx);
        mButton_lsjl = (Button) findViewById(R.id.button_lsjl);
        mButton_again = (Button) findViewById(R.id.button_again);
        mButton_px.setOnClickListener(this);
        mButton_ctjx.setOnClickListener(this);
        mButton_lsjl.setOnClickListener(this);
        mButton_again.setOnClickListener(this);

        mTextView_KM.setText(km);
        mTextView_CX.setText(cx);
        mTextView_KSCJ.setText(String.valueOf(KSCJ));
        mTextView_KSSJ.setText("用时" + getSubTime(this.KSSJ,this.JSSJ));

        if(KSCJ == 100) {
            mCircleImageView.setImageResource(R.mipmap.fen100);
        } else if(KSCJ <=99 && KSCJ >=90) {
            mCircleImageView.setImageResource(R.mipmap.fen90);
        } else if(KSCJ >=0 && KSCJ <=89) {
            mCircleImageView.setImageResource(R.mipmap.bujige);
        }
    }

    private void init() {
        if(isLogin()) {
            Log.e("1",""+mGlobalData.mUser_Mobile);
            if("1".equals(KM)) {
                getData.uploadMark(Get.getPhoneID(this), mGlobalData.mUser_Mobile , KSCJ, 0 , str_KSSJ, str_JSSJ);
            } else if ("4".equals(KM)) {
                getData.uploadMark(Get.getPhoneID(this), mGlobalData.mUser_Mobile , KSCJ, 1 , str_KSSJ, str_JSSJ);
            }
        }
    }

    private String getSubTime(Date BeginTime,Date EndTime) {
        return new SimpleDateFormat("m分ss秒").format(new Date(EndTime.getTime() - BeginTime.getTime()));
    }

    private int getCX(String CX) {
        if(CX.contains("C1")) {
            return 0;
        } else if(CX.contains("B2")){
            return 1;
        } else if(CX.contains("A1")){
            return 2;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_px:
                showPXDialog();
                break;
            case R.id.button_ctjx:
                Intent intent_ctjs = new Intent(this,CTJXExamActivity.class);
                intent_ctjs.putExtra(flag_Select_KM_Type,KM);
                intent_ctjs.putExtra(flag_Select_CX_Type,CX);
                startActivity(intent_ctjs);
                break;
            case R.id.button_lsjl:
                Intent intent_lsjl = new Intent(this, LGradesActivity.class);
                intent_lsjl.putExtra(flag_Select_KM_Type,KM);
                startActivity(intent_lsjl);
                break;
            case R.id.button_again:
                new MyDataBase().deleteMNKSResult(KM, CX);
                Intent intent_again = new Intent(this, MNKSExamActivity.class);
                intent_again.putExtra(flag_Select_KM_Type,KM);
                intent_again.putExtra(flag_Select_CX_Type,CX);
                startActivity(intent_again);
                finish();
                break;
        }
    }

    private void showPXDialog() {
        if(!isLogin()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("登陆后可查看自己在全国的排名");
            builder.setNegativeButton("马上登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent_login = new Intent(ExamResultActivity.this, Dada_User_LoginCode.class);
                    startActivityForResult(intent_login, 200);
                }
            });
            builder.setPositiveButton("先看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent_phb = new Intent(ExamResultActivity.this, H5Activity.class);
                    intent_phb.putExtra("url", "http://www.dadaxueche.com/m/phb.html");
                    intent_phb.putExtra("content", "排行榜");
                    intent_phb.putExtra("KM",KM);
                    startActivity(intent_phb);
                }
            });
            builder.show();
        } else {
            Intent intent_phb = new Intent(ExamResultActivity.this, H5Activity.class);
            intent_phb.putExtra("url", "http://www.dadaxueche.com/m/phb.html");
            intent_phb.putExtra("content", "排行榜");
            intent_phb.putExtra("KM",KM);
            startActivity(intent_phb);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }

    private boolean isLogin() {
        return getSharedPreferences("isLogin", 0).getInt("islonginId", 0) != 0;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new MyDataBase().deleteMNKSResult(KM, CX);
    }

    @Override
    public void postResultCallBack(String ID, Object object, String Message) {
        switch (ID) {
            case "0":
                if(Message.equals("成功")) {
                    findViewById(R.id.view_nomark).setVisibility(View.GONE);
                    mTextView_Ranking.setText(String.valueOf(((Mark) object).getTodayRank()));
                    findViewById(R.id.view_NationalRankings).setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this,Message,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
