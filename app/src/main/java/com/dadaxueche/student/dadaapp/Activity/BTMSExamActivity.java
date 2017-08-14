package com.dadaxueche.student.dadaapp.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.Gson.ExamResult;
import com.dada.mylibrary.Gson.QuestionBank;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.Fragment.ExamFragment;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.Download;
import com.dadaxueche.student.dadaapp.Util.Exam;
import com.dadaxueche.student.dadaapp.Util.GetExam;
import com.dadaxueche.student.dadaapp.Util.GetExamQuestionList;
import com.dadaxueche.student.dadaapp.View.MyButton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class
        BTMSExamActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener ,
        View.OnClickListener ,
        Download.DownloadSuccess ,
        Download.DownloadFail ,
        Download.DownloadSize {

    private ImageButton mImageButton_Return;
    private TextView mTextView_Title;
    private ImageView mImageView_Logo;
    private Button mButton_Menu;
    private ViewPager mViewPager;
    private MyButton button_1,button_2,button_3;

    private Download mDownload;
    private MyDataBase myDataBase = new MyDataBase();
    private GetExamQuestionList mGetExamQuestionList;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private List<QuestionBank.TKListDataEntity> mQuestionList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> mResultList = new ArrayList<>();
    private List<Integer> lastPositionList = new ArrayList<>();

    private String Select = "BTMS";
    private String filePath = "";
    private String KM = "";
    private String CX = "";
    private int currentAllPosition = 0;
    private int lastPosition = 0;
    private int ignorePosition = 0;
    private boolean isCollect,isStart;
    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Select_CX_Type = "CX_Type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_exam);
        Intent intent = getIntent();
        KM = intent.getStringExtra(flag_Select_KM_Type);
        CX = intent.getStringExtra(flag_Select_CX_Type);
        init();
        initViewInfo();
    }

    private void init() {
        mDownload = new Download(this,KM);
        mDownload.setDownloadSuccess(this);
        mDownload.setDownloadFail(this);
        mDownload.setDownloadSize(this);
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dada/Exam/";
        Check.CheckDir(filePath);

        mImageButton_Return = (ImageButton) findViewById(R.id.imageButton_return);
        mImageButton_Return.setOnClickListener(this);
        mTextView_Title = (TextView) findViewById(R.id.actionbar_Title);
        mTextView_Title.setText("背题模式");
        mImageView_Logo = (ImageView) findViewById(R.id.imageView_Logo);
        mImageView_Logo.setVisibility(View.GONE);
        mButton_Menu = (Button) findViewById(R.id.actionbar_Menu);
        mButton_Menu.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(this);

        button_1 = (MyButton) findViewById(R.id.button_1);
        button_2 = (MyButton) findViewById(R.id.button_2);
        button_3 = (MyButton) findViewById(R.id.button_3);

        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
    }

    private void initViewInfo() {
        button_1.setImageView(R.mipmap.shouchang);
        button_2.setImageView(R.mipmap.paichu);
        button_3.setImageView(R.mipmap.jiexi);
        button_3.setEnabled(false);
        button_3.setTextViewColor(Color.GRAY);
        mButton_Menu.setVisibility(View.VISIBLE);
        button_1.setText("收藏");
        button_2.setText("排除");
        button_3.setText("解析");
        getQuestion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveLastPosition(currentAllPosition);
        setResult(100);
    }

    private void getQuestion() {
        if (mGetExamQuestionList == null) {
            mGetExamQuestionList = new GetExamQuestionList();
            mGetExamQuestionList.setCX(CX);
            mGetExamQuestionList.setKM(KM);
        }
        if (mQuestionList.isEmpty())
            mQuestionList.addAll(mGetExamQuestionList.getBTMSQuestionDataArrayList());
        if (mQuestionList.isEmpty())
            showNoQuestionDialog(this);
        else if (!Check.CheckDirNoMkdir(filePath + "km" + KM))
            showNoImageDialog(this);
        else {
            initView();
        }
    }

    private void initView() {
        if (!mQuestionList.isEmpty()) {
            mResultList.clear();
            for (int i = 0; i < mQuestionList.size(); ++i) {
                ExamResult.STResultsEntity stResultsEntity = new ExamResult.STResultsEntity();
                stResultsEntity.setStid(Integer.valueOf(mQuestionList.get(i).getStid()));
                stResultsEntity.setBzda(getSortString(mQuestionList.get(i).getStda()));
                stResultsEntity.setXzda(getSortString(mQuestionList.get(i).getStda()));
                stResultsEntity.getXyw();
                mResultList.add(stResultsEntity);
            }
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTextView_Title.setText("背题:" + getCXTitle() + ",科" + getKMTitle() + ",共" + mQuestionList.size() + "题");
        if (!mQuestionList.isEmpty())
            buttonCollectChange(mQuestionList.get(0).getStxh());
        resumeToLastPosition();
    }

    private void showNoQuestionDialog(Context context) {
        if (Check.getConnectedType(context) == ConnectivityManager.TYPE_WIFI) {
            mDownload.downloadQuestionBank();
        } else if (Check.getConnectedType(context) == ConnectivityManager.TYPE_MOBILE) {
            showNotWIFIDialog();
        } else {
            Toast.makeText(context,"没有网络,请先连接网络",Toast.LENGTH_SHORT).show();
            close();
        }
    }

    private void showNoImageDialog(final Context context) {
        int Type = Check.getConnectedType(context);
        if (Type == ConnectivityManager.TYPE_WIFI) {
            mDownload.downloadImage();
        } else if (Type == ConnectivityManager.TYPE_MOBILE) {
            showNotWIFIDialog();
        } else {
            Toast.makeText(context, "没有网络,请先连接网络", Toast.LENGTH_SHORT).show();
            close();
        }
    }

    private String getCXTitle() {
        if(CX.contains("C1")) {
            return "小车";
        } else if (CX.contains("B2")) {
            return "货车";
        } else if(CX.contains("A1")) {
            return "客车";
        }
        return "小车";
    }

    private String getKMTitle() {
        if(KM.equals("1"))
            return "一";
        else if(KM.equals("4"))
            return "四";
        return "一";
    }

    private void close() {
        finish();
    }

    private void showNotWIFIDialog() {
        mDownload.getZipInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                collect();
                break;
            case R.id.button_2:
                ignorePosition = currentAllPosition;
                ignore();
                break;
            case R.id.button_3:
                analyze();
                break;
            case R.id.imageButton_return:
                finish();
                break;
            case R.id.actionbar_Menu:
                showMenuWindow();
                break;
            case R.id.button_menu_wdsc:
                if("1".equals(KM)) {
                    Intent intent_wdsc = new Intent(this, LK1CollectActivity.class);
                    intent_wdsc.putExtra(flag_Select_KM_Type, KM);
                    startActivity(intent_wdsc);
                } else if("4".equals(KM)) {
                    Intent intent_wdsc = new Intent(this, LCollectActivity.class);
                    intent_wdsc.putExtra(flag_Select_KM_Type, KM);
                    startActivity(intent_wdsc);
                }
                break;
            case R.id.button_menu_wdct:
                if("1".equals(KM)) {
                    Intent intent_wdct = new Intent(this, LK1ErrorActivity.class);
                    intent_wdct.putExtra(flag_Select_KM_Type, KM);
                    startActivity(intent_wdct);
                } else if("4".equals(KM)) {
                    Intent intent_wdct = new Intent(this, LErrorActivity.class);
                    intent_wdct.putExtra(flag_Select_KM_Type, KM);
                    startActivity(intent_wdct);
                }
            case R.id.radioButton_T_1:
                break;
            case R.id.radioButton_T_2:
                break;
            case R.id.radioButton_T_3:
                break;

        }
    }

    private void showMenuWindow() {
        View view_menu = LayoutInflater.from(this).inflate(R.layout.layout_exam_menu, null, false);
        Button button_wdsc = (Button) view_menu.findViewById(R.id.button_menu_wdsc);
        Button button_woct = (Button) view_menu.findViewById(R.id.button_menu_wdct);
        view_menu.findViewById(R.id.popWindow_Set).setVisibility(View.GONE);

        button_wdsc.setOnClickListener(this);
        button_woct.setOnClickListener(this);

//            checkBox_menu_error.setOnClickListener(this);
//            checkBox_menu_auto.setOnClickListener(this);
//            checkBox_menu_night.setOnClickListener(this);
//            radioButton_T_1.setOnClickListener(this);
//            radioButton_T_2.setOnClickListener(this);
//            radioButton_T_3.setOnClickListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view_menu);
        builder.show();
    }

    private void collect() {
        String stxh = mQuestionList.get(currentAllPosition).getStxh();
        if(isCollect) {
            if (myDataBase.isCollect(stxh) && myDataBase.deleteCollect(stxh, KM))
                setButtonNoCollect();
        } else {
            if (!myDataBase.isCollect(stxh) && myDataBase.insertCollect(stxh, KM))
                setButtonCollect();
        }
    }

    private void setButtonCollect() {
        button_1.setImageView(R.mipmap.shouchangdianji);
        isCollect = true;
    }

    private void setButtonNoCollect() {
        button_1.setImageView(R.mipmap.shouchang);
        isCollect = false;
    }

    private void buttonCollectChange(String stxh) {
        if(isCollect = myDataBase.isCollect(stxh))
            setButtonCollect();
        else
            setButtonNoCollect();
    }

    private void ignore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("真的会了,要排除此题?");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("果断排除", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteError(mQuestionList.get(ignorePosition).getStxh());
                saveIgnore(mQuestionList.get(ignorePosition).getStxh());
                mQuestionList.remove(ignorePosition);
                mResultList.remove(ignorePosition);
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mTextView_Title.setText("背题:" + getCXTitle() + ",科" + getKMTitle() + ",共" + mQuestionList.size() + "题");
                if(!mQuestionList.isEmpty())
                    buttonCollectChange(mQuestionList.get(0).getStxh());
                mViewPager.setCurrentItem(ignorePosition <= mQuestionList.size() ? ignorePosition : mQuestionList.size());
            }
        });
        builder.show();
    }

    private boolean deleteError(String stxh) {
        return myDataBase.deleteError(stxh,KM);
    }

    private void analyze() {
        showAnalyze();
    }

    private void showAnalyze() {
        View view = LayoutInflater.from(this).inflate(R.layout.exam_jiexi,null,false);
        final ImageButton mImageButton_up = (ImageButton)view.findViewById(R.id.imageButton_open);
        final TextView mTextView_jiexi = (TextView) view.findViewById(R.id.textView_jiexi);
//        final ScrollView mScrollView_exam = (ScrollView) view.findViewById(R.id.scrollView_exam);

        mImageButton_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (mTextView_jiexi.getMaxLines() == 5) {
                        mTextView_jiexi.setMaxLines(100);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                //mScrollView_exam.fullScroll(ScrollView.FOCUS_DOWN);
                                mImageButton_up.setImageResource(R.mipmap.xiangxia);
                            }
                        });
                    } else {
                        mTextView_jiexi.setMaxLines(5);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                //mScrollView_exam.fullScroll(ScrollView.FOCUS_UP);
                                mImageButton_up.setImageResource(R.mipmap.xiangshang);
                            }
                        });
                    }
                }
            }
        });

        PopupWindow popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.MyExamTheme);
        popupWindow.showAtLocation(findViewById(R.id.view_bottom), Gravity.BOTTOM,0,0);
        final int color_Back = findViewById(R.id.window).getSolidColor();
        findViewById(R.id.window).setBackgroundColor(Color.GRAY);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                findViewById(R.id.window).setBackgroundColor(color_Back);
            }
        });
    }

    private void saveIgnore(String stxh) {
        if (myDataBase.insertIgnore(stxh,KM)) {
            Toast.makeText(this,"排除本题成功",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"排除本题失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void downloadSuccess() {
        getQuestion();
    }

    @Override
    public void downloadFail() {
//        Toast.makeText(this,"服务器异常,下载失败",Toast.LENGTH_SHORT).show();
        mQuestionList.clear();
        mResultList.clear();
        finish();
    }

    @Override
    public void downloadSize(String Size) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告");
        builder.setMessage("非Wi-Fi网络环境，\n下载此题库可能会产生" + "流量,\n要继续下载吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mQuestionList.isEmpty())
                    mDownload.downloadQuestionBank();
                else
                    mDownload.downloadImage();
            }
        });
        builder.show();
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<QuestionBank.TKListDataEntity> questionList;
        private List<ExamResult.STResultsEntity> resultList;

        public SectionsPagerAdapter(FragmentManager fm,List<QuestionBank.TKListDataEntity> questionList,List<ExamResult.STResultsEntity> resultList) {
            super(fm);
            this.questionList = questionList;
            this.resultList = resultList;
        }

        @Override
        public ExamFragment getItem(int position) {
            ExamFragment mExamFragment = new ExamFragment();
            mExamFragment.setPosition(position);
            mExamFragment.setExamResult(resultList.get(position));
            mExamFragment.setExam(DataAndImageToExam(false, position + 1, questionList.get(position)));
            return mExamFragment;
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }

    private void saveLastPosition(int lastPosition) {
        SharedPreferences settings = getSharedPreferences("lastPosition_" + Select, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("lastPosition", lastPosition);
        editor.apply();
    }

    private int getLastPosition() {
        SharedPreferences settings = getSharedPreferences("lastPosition_" + Select, 0);
        return settings.getInt("lastPosition", 0);
    }

    private void resumeToLastPosition() {
        lastPosition = getLastPosition();
        mViewPager.setCurrentItem(lastPosition);
    }

    private String getSortString(String string) {
        char[] result = string.toCharArray();
        Arrays.sort(result);
        return Arrays.toString(result);
    }


    private Exam DataAndImageToExam(boolean isReviewError,int current,QuestionBank.TKListDataEntity data) {
        GetExam getExam = new GetExam();
        getExam.setExamByDada(isReviewError, current, data);
        return getExam.getExam();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        if(lastPositionList.size() >= 2) {
            if(!isStart && i == mQuestionList.size()-1 && v == 0 && i1 == 0) {
                Toast.makeText(this,"没有更多题目了",Toast.LENGTH_SHORT).show();
                lastPositionList.clear();
                isStart = true;
            }
        } else {
            lastPositionList.add(i);
        }
        if(i != currentAllPosition) {
            lastPositionList.clear();
        }
    }

    @Override
    public void onPageSelected(int i) {
        currentAllPosition = i;
        buttonCollectChange(mQuestionList.get(i).getStxh());
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if(i == 0) {
            isStart = false;
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