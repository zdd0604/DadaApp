package com.dadaxueche.student.dadaapp.Activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.Gson.ExamResult;
import com.dada.mylibrary.Gson.QuestionBank;
import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.Adapter.QuestionListViewAdapter;
import com.dadaxueche.student.dadaapp.Fragment.ExamFragment;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.Download;
import com.dadaxueche.student.dadaapp.Util.Exam;
import com.dadaxueche.student.dadaapp.Util.GetExam;
import com.dadaxueche.student.dadaapp.Util.GetExamQuestionList;
import com.dadaxueche.student.dadaapp.Util.SaveExamResult;
import com.dadaxueche.student.dadaapp.View.MyButton;
import com.dadaxueche.student.dadaapp.View.MyTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MNKSExamActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener ,
        View.OnClickListener ,
        MyTextView.TimeOver ,
        MyTextView.Time ,
        QuestionListViewAdapter.onItemClick ,
        Download.DownloadSuccess ,
        Download.DownloadFail ,
        Download.DownloadSize {

    private ImageButton mImageButton_Return;
    private TextView mTextView_Title;
    private ImageView mImageView_Logo;
    private Button mButton_Menu;
    private MyTextView myTextView;
    private TextView mTextView_mark;
    private LinearLayout mLinearLayout_exam;
    private ViewPager mViewPager;
    private MyButton button_1,button_2,button_3;
    private PopupWindow mPopupWindow_Question = null;

    private Download mDownload;
    private Date mBeginDateTime;
    private SaveExamResult saveExamResult = new SaveExamResult(this);
    private MyDataBase myDataBase = new MyDataBase();
    private GetExamQuestionList mGetExamQuestionList;
    private ExamResult.STResultsEntity resultsEntity = new ExamResult.STResultsEntity();
    private ExamFragment mExamFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private QuestionListViewAdapter questionListViewAdapter = new QuestionListViewAdapter();
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<QuestionBank.TKListDataEntity> mQuestionList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> mResultList = new ArrayList<>();
    private List<QuestionBank.TKListDataEntity> errorQuestionList = new ArrayList<>();
    private List<String> rightList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> errorResultList = new ArrayList<>();
    private List<Integer> lastPositionList = new ArrayList<>();

    private String filePath = "";
    private String KM = "";
    private String CX = "";
    private String KM1Time = "45:00";
    private String KM4Time = "30:00";
    private String KMTime;
    private String Select = "MNKS";
    private int currentAllPosition = 0;
    private int reviewErrorNum = 0;
    private int lastPosition = 0;
    private String exam_Result = "";
    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Select_CX_Type = "CX_Type";
    private String flag_KSCJ = "KSCJ";
    private String flag_KSSJ = "KSSJ";
    private String flag_JSSJ = "JSSJ";
    private boolean isCollect,isStart,canStart;

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
        mBeginDateTime = new Date(System.currentTimeMillis());
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dada/Exam/";
        Check.CheckDir(filePath);

        mImageButton_Return = (ImageButton) findViewById(R.id.imageButton_return);
        mTextView_Title = (TextView) findViewById(R.id.actionbar_Title);
        mTextView_Title.setText("模拟考试");
        mImageView_Logo = (ImageView) findViewById(R.id.imageView_Logo);
        mImageView_Logo.setVisibility(View.GONE);
        mButton_Menu = (Button) findViewById(R.id.actionbar_Menu);

        mImageButton_Return.setOnClickListener(this);
        mButton_Menu.setOnClickListener(this);

        myTextView = (MyTextView) findViewById(R.id.time);
        if(KM.equals("1"))
            myTextView.setTimeText(KM1Time);
        else if(KM.equals("4"))
            myTextView.setTimeText(KM4Time);
        myTextView.setTextColor(Color.RED);
        myTextView.setTimeOver(this);
        myTextView.setTime(this);
        mTextView_mark = (TextView) findViewById(R.id.textView_df);

        mLinearLayout_exam = (LinearLayout) findViewById(R.id.exam);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        button_1 = (MyButton) findViewById(R.id.button_1);
        button_2 = (MyButton) findViewById(R.id.button_2);
        button_3 = (MyButton) findViewById(R.id.button_3);

        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
    }

    private void initViewInfo() {
        button_1.setImageView(R.mipmap.position);
        button_2.setImageView(R.mipmap.paichu);
        button_3.setImageView(R.mipmap.shangchuan);
        button_1.setText(getAlreadyText());
        button_2.setText("收藏");
        button_3.setText("交卷");
        mButton_Menu.setVisibility(View.GONE);
        mLinearLayout_exam.setVisibility(View.VISIBLE);
        if(myDataBase.isEmptyMNKSResult(KM,CX)) {
            saveMark("0");
            if(KM.equals("1"))
                saveTime(KM1Time);
            else
                saveTime(KM4Time);
            getQuestion();
        } else
            loadOldState();
    }

    private void initView() {
        if (KM.equals("1")) {
            try {
                KMTime = getTime();
                myTextView.setmDate(new SimpleDateFormat("mm:ss").parse(KMTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(KM.equals("4")) {
            try {
                KMTime = getTime();
                myTextView.setmDate(new SimpleDateFormat("mm:ss").parse(KMTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mTextView_mark.setText("得分:" + getMark());
        button_1.setText(getAlreadyText());
        if(!mQuestionList.isEmpty())
            buttonCollectChange(mQuestionList.get(0).getStxh());
        mTextView_Title.setText("考试:" + getCXTitle() + ",科" + getKMTitle() + ",共" + mQuestionList.size() + "题");
    }

    private void loadOldState() {
        canStart = false;
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("检测到上次有保存记录，请问恢复吗?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDataBase.deleteMNKSResult(KM, CX);
                if (KM.equals("1"))
                    saveTime(KM1Time);
                else if (KM.equals("4"))
                    saveTime(KM4Time);
                saveMark("0");
                canStart = true;
                getQuestion();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                canStart = true;
                initSelectState();
            }
        });
        builder.show();
    }

    private void initSelectState() {
        Cursor cursor = myDataBase.queryMNKSResult(KM,CX);
        while (cursor.moveToNext()) {
            mQuestionList = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("MNKS_questionList")),
                    new TypeToken<List<QuestionBank.TKListDataEntity>>() {
            }.getType());
            mResultList = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("MNKS_resultList")),
                    new TypeToken<List<ExamResult.STResultsEntity>>() {
            }.getType());
        }
        cursor.close();
        if(canStart)
            initView();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        lastPosition = getLastPosition();
        mViewPager.setCurrentItem(lastPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(100);
        saveState();
        myTextView.setCando(false);
    }

    private void saveState() {
        if(!myDataBase.insertMNKSResult(KM, CX, new Gson().toJson(mQuestionList), new Gson().toJson(mResultList)))
            myDataBase.updateMNKSResult(KM, CX, new Gson().toJson(mQuestionList), new Gson().toJson(mResultList));
        saveLastPosition(currentAllPosition);
        saveTime(KMTime);
        saveMark(String.valueOf(saveExamResult.getKSCJ()));
    }

    private void getQuestion() {
        if(mGetExamQuestionList == null) {
            mGetExamQuestionList = new GetExamQuestionList();
            mGetExamQuestionList.setCX(CX);
            mGetExamQuestionList.setKM(KM);
        }
        mQuestionList.clear();
        mQuestionList.addAll(mGetExamQuestionList.getMNKSQuestionDataArrayList());
        canStart = true;
        if(mQuestionList.isEmpty())
            showNoQuestionDialog(this);
        else if(!Check.CheckDirNoMkdir(filePath + "km" + KM)) {
            showNoImageDialog(this);
        } else
            initSelectList();
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

    private void initSelectList() {
        if(!mQuestionList.isEmpty()) {
            mResultList.clear();
            for (int i = 0; i < mQuestionList.size(); ++i) {
                ExamResult.STResultsEntity stResultsEntity = new ExamResult.STResultsEntity();
                stResultsEntity.setStid(Integer.valueOf(mQuestionList.get(i).getStid()));
                stResultsEntity.setBzda(getSortString(mQuestionList.get(i).getStda()));
                mResultList.add(stResultsEntity);
                rightList.add("");
            }
            if(canStart)
                initView();
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void showNoQuestionDialog(Context context) {
        canStart = false;
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
        canStart = false;
        int Type = Check.getConnectedType(context);
        if (Type == ConnectivityManager.TYPE_WIFI) {
            mDownload.downloadImage();
        } else if (Type == ConnectivityManager.TYPE_MOBILE) {
            showNotWIFIDialog();
        } else {
            Toast.makeText(context,"没有网络,请先连接网络",Toast.LENGTH_SHORT).show();
            close();
        }
    }

    private void close() {
        finish();
    }

    private void showNotWIFIDialog() {
        canStart = false;
        mDownload.getZipInfo();
    }

    private String getAlreadyText() {
        return getResultSelectSize() + "/" + String.valueOf(mQuestionList.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                showPopWindow();
                break;
            case R.id.button_2:
                collect();
                break;
            case R.id.button_3:
                uploadExamResult();
                break;
            case R.id.imageButton_return:
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    private void dimBackground(final float from, final float to) {
        final Window window = getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();
    }

    @SuppressLint("NewApi")
    private void showPopWindow() {
        if(mPopupWindow_Question == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.group_question, null, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_question_list);
            recyclerView.setHasFixedSize(true);
            questionListViewAdapter.setResult_Selects(mResultList);
            questionListViewAdapter.addOnItemClickListener(this);
            questionListViewAdapter.setCanPoint(true);
            recyclerView.setAdapter(questionListViewAdapter);
            recyclerView.setLayoutManager(gridLayoutManager);
            ImageButton imageButton_Close = (ImageButton) view.findViewById(R.id.imageButton_close);
            imageButton_Close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow_Question.showAsDropDown(v);
                    mPopupWindow_Question.dismiss();
                }
            });
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            mPopupWindow_Question = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, point.y / 10 * 6);
        }
        gridLayoutManager.scrollToPosition(currentAllPosition);
        questionListViewAdapter.setCurrentPosition(currentAllPosition);
        questionListViewAdapter.notifyDataSetChanged();
        mPopupWindow_Question.setFocusable(true);
        mPopupWindow_Question.setOutsideTouchable(true);
        mPopupWindow_Question.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow_Question.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow_Question.setAnimationStyle(R.style.MyExamTheme);
        mPopupWindow_Question.showAtLocation(this.findViewById(R.id.view_bottom), Gravity.BOTTOM, 0, 0);
        dimBackground(1.0f,0.3f);
        mPopupWindow_Question.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dimBackground(0.3f,1.0f);
            }
        });
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
        button_2.setImageView(R.mipmap.shouchangdianji);
        isCollect = true;
    }

    private void setButtonNoCollect() {
        button_2.setImageView(R.mipmap.shouchang);
        isCollect = false;
    }

    private void buttonCollectChange(String stxh) {
        if(myDataBase.isCollect(stxh))
            setButtonCollect();
        else
            setButtonNoCollect();
    }

    private void uploadExamResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认提交");
        builder.setMessage("总共" + mQuestionList.size() + "题，完成" + getResultSelectSize() + "题确定交卷吗?");
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadResult();
            }
        });
        builder.show();

    }

    private int getResultSelectSize() {
        int i = 0;
        for(ExamResult.STResultsEntity resultsEntity : mResultList) {
            if(resultsEntity.getXzda().contains("A")
                    || resultsEntity.getXzda().contains("B")
                    || resultsEntity.getXzda().contains("C")
                    || resultsEntity.getXzda().contains("D")
                    || resultsEntity.getXzda().contains("Y")
                    || resultsEntity.getXzda().contains("N")) {
                i++;
            }
        }
        return i;
    }

    private void uploadResult() {
        saveExamResult.setCX(CX);
        saveExamResult.setKM(KM);
        saveExamResult.setmBeginDateTime(mBeginDateTime);
        Date EndDateTime =new Date(System.currentTimeMillis());
        saveExamResult.setmEndDateTime(EndDateTime);
        saveExamResult.setExam_Result(exam_Result);
        saveExamResult.setQuestionList(mQuestionList);
        saveExamResult.setResult_Selects(mResultList);
        saveExamResult.saveExamResult();

        finish();
        Intent intent_ph = new Intent(this,ExamResultActivity.class);
        intent_ph.putExtra(flag_Select_KM_Type,KM);
        intent_ph.putExtra(flag_Select_CX_Type,CX);
        intent_ph.putExtra(flag_KSCJ,saveExamResult.getKSCJ());
        intent_ph.putExtra(flag_KSSJ,simpleDateFormat.format(mBeginDateTime));
        intent_ph.putExtra(flag_JSSJ,simpleDateFormat.format(EndDateTime));
        startActivity(intent_ph);
    }

    @Override
    public void onClicked(int position) {
        mPopupWindow_Question.dismiss();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void downloadSuccess() {
        canStart = true;
        getQuestion();
    }

    @Override
    public void downloadFail() {
        mQuestionList.clear();
        mResultList.clear();
        finish();
    }

    @Override
    public void downloadSize(String Size) {
        canStart = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告");
        builder.setMessage("非Wi-Fi网络环境，\n下载此题库可能会产生" + "流量,\n要继续下载吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                canStart = true;
                initView();
            }
        });
        builder.setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mQuestionList.isEmpty())
                    mDownload.downloadQuestionBank();
                else if(!Check.CheckDirNoMkdir(filePath + "km" + KM))
                    mDownload.downloadImage();
            }
        });
        builder.show();
    }

    @Override
    public void Time(String Time) {
        KMTime = Time;
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter implements
            ExamFragment.OnRadioButtonClicked ,
            ExamFragment.OnButtonClicked {
        private List<QuestionBank.TKListDataEntity> mQuestionList;
        private List<ExamResult.STResultsEntity> mResultList;

        public SectionsPagerAdapter(FragmentManager fm,List<QuestionBank.TKListDataEntity> questionList,List<ExamResult.STResultsEntity> resultList) {
            super(fm);
            this.mQuestionList = questionList;
            this.mResultList = resultList;
        }

        @Override
        public ExamFragment getItem(int position) {
            ExamFragment mExamFragment = new ExamFragment();
            mExamFragment.setPosition(position);
            mExamFragment.setExamResult(this.mResultList.get(position));
            if(position < reviewErrorNum) {
                mExamFragment.setIsReviewError(true);
                mExamFragment.setExam(DataAndImageToExam(true, position + 1, this.mQuestionList.get(position)));
            }
            else {
                mExamFragment.setIsReviewError(false);
                mExamFragment.setExam(DataAndImageToExam(false,position + 1, this.mQuestionList.get(position)));
            }
            mExamFragment.setOnRadioButtonClick(this);
            mExamFragment.setOnButtonClicked(this);
            return mExamFragment;
        }

        @Override
        public int getCount() {
            return this.mQuestionList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mExamFragment = (ExamFragment)object;
        }

        @Override
        public void onRadioButtonClicked(int sttx,String result_Select,int position) {
            dealResult(sttx,result_Select,position);
        }

        @Override
        public void onButtonClicked() {
            dealButtonClicked();
        }
    }

    private void saveTime(String time) {
        SharedPreferences settings = getSharedPreferences("Exam_Time"+Select +"_"+ KM +"_"+ CX, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("exam_Time", time);
        editor.apply();
    }

    private String getTime() {
        SharedPreferences settings = getSharedPreferences("Exam_Time" + Select + "_" + KM + "_" + CX, 0);
        if (KM.equals("1"))
            return settings.getString("exam_Time", KM1Time);
        else if (KM.equals("4"))
            return settings.getString("exam_Time", KM4Time);
        return settings.getString("exam_Time", KM1Time);
    }

    private void saveMark(String mark) {
        SharedPreferences settings = getSharedPreferences("Exam_Mark"+Select +"_"+ KM +"_"+ CX, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("exam_Mark", mark);
        editor.apply();
    }

    private String getMark() {
        SharedPreferences settings = getSharedPreferences("Exam_Mark"+Select +"_"+ KM +"_"+ CX, 0);
        return settings.getString("exam_Mark", "0");
    }

    private void saveLastPosition(int lastPosition) {
        SharedPreferences settings = getSharedPreferences("lastPosition_"+Select +"_"+ KM +"_"+ CX, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("lastPosition", lastPosition);
        editor.apply();
    }

    private int getLastPosition() {
        SharedPreferences settings = getSharedPreferences("lastPosition_"+Select +"_"+ KM +"_"+ CX, 0);
        return settings.getInt("lastPosition", 0);
    }

    private void dealButtonClicked() {
        String result_Select = resultsEntity.getXzda();
        if(isSelect(result_Select) && !mExamFragment.isSelect()) {
            mResultList.set(currentAllPosition, resultsEntity);
            String Bzda = mResultList.get(currentAllPosition).getBzda();
            judgeError(result_Select,Bzda,0);
            button_1.setText(getAlreadyText());
            mExamFragment.setData(Bzda);
            mTextView_mark.setText("得分:"+ saveExamResult.getKSCJ());
        }
        if (currentAllPosition < mQuestionList.size()) {
            mViewPager.setCurrentItem(currentAllPosition + 1);
            resultsEntity = new ExamResult.STResultsEntity();
        }
    }

    private void dealResult(int sttx,String result_Select,int position) {
        result_Select = getSortString(result_Select);
        String Bzda = mQuestionList.get(position).getStda();
        Bzda = getSortString(Bzda);
        ExamResult.STResultsEntity resultsEntity = new ExamResult.STResultsEntity();
        resultsEntity.setStid(Integer.valueOf(mQuestionList.get(position).getStid()));
        resultsEntity.setBzda(Bzda);
        resultsEntity.setXzda(result_Select);
        resultsEntity.setSj(new Date(System.currentTimeMillis()));
        resultsEntity.setEvl((resultsEntity.getBzda().equals(resultsEntity.getXzda()) ? "T" : "F"));
        resultsEntity.getXyw();
        this.resultsEntity = resultsEntity;
        if (!mResultList.isEmpty()) {
            if (sttx != 3) {
                mResultList.set(position, resultsEntity);
                saveExamResult.setResult_Selects(mResultList);
                button_1.setText(getAlreadyText());
                judgeError(result_Select, Bzda,position);
                mTextView_mark.setText("得分:" + saveExamResult.getKSCJ());
            }
        }
    }

    private void judgeError(String result_Select,String Bzda,int position) {
        boolean error = !result_Select.equals(Bzda);
        if (error) {
            errorResultList.add(resultsEntity);
            errorQuestionList.add(mQuestionList.get(currentAllPosition));
            myDataBase.insertError(mQuestionList.get(currentAllPosition).getStxh(), KM);
            myDataBase.deleteRight(mQuestionList.get(currentAllPosition).getStxh(), KM);
            if(errorResultList.size()==10)
                showErrorExceedTen();
        } else {
            myDataBase.insertRight(mQuestionList.get(currentAllPosition).getStxh(), KM);
            if (getDOTrueDeleteError())
                myDataBase.deleteError(mQuestionList.get(currentAllPosition).getStxh(), KM);
        }
        delay(position);
    }

    private boolean getDOTrueDeleteError() {
        SharedPreferences sharedPreferences = getSharedPreferences("error",
                Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean("delete_state", false);
    }

    private int delaySendTime = 1000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                if(msg.arg1 == currentAllPosition)
                mViewPager.setCurrentItem(msg.arg1 + 1);
            }
        }
    };

    private void delay(int position) {
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.arg1 = position;
        handler.sendMessageDelayed(msg, delaySendTime);
    }

    private void showErrorExceedTen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("错题达到10分，自动结束考试");
        builder.setNegativeButton("继续做题", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("查看成绩", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadResult();
            }
        });
        builder.show();
    }

    private String getSortString(String string) {
        char[] result = string.toCharArray();
        Arrays.sort(result);
        return Arrays.toString(result);
    }

    private boolean isSelect(String result) {
        return (result.contains("A")
                || result.contains("B")
                || result.contains("C")
                || result.contains("D")
                || result.contains("Y")
                || result.contains("N"));
    }

    private Exam DataAndImageToExam(boolean isReviewError,int current,QuestionBank.TKListDataEntity data) {
        GetExam getExam = new GetExam();
        getExam.setExamByDada(isReviewError, current, data);
        return getExam.getExam();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exam, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 0:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        resultsEntity = new ExamResult.STResultsEntity();
        if (mExamFragment != null && !mExamFragment.isSelect())
            mExamFragment.clearGroup();
        buttonCollectChange(mQuestionList.get(i).getStxh());
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if(i == 0) {
            isStart = false;
        }
    }

    @Override
    public void TimeOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("考试时间结束，请提交试卷");
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadResult();
            }
        });
        builder.show();
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
