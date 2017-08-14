package com.dadaxueche.student.dadaapp.Activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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
import com.dadaxueche.student.dadaapp.Util.Exam;
import com.dadaxueche.student.dadaapp.Util.GetExam;
import com.dadaxueche.student.dadaapp.View.MyButton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class CTJXExamActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener ,
        View.OnClickListener ,
        QuestionListViewAdapter.onItemClick {

    private ImageButton mImageButton_Return;
    private LinearLayout mLinearLayout_no_exam;
    private Button mButton_error_analyze,mButton_all_analyze;
    private ViewPager mViewPager;
    private MyButton button_1,button_2,button_3;
    private TextView mTextView_Title;
    private ImageView mImageView_Logo;
    private PopupWindow mPopupWindow_Question = null;

    private MyDataBase myDataBase = new MyDataBase();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private QuestionListViewAdapter questionListViewAdapter = new QuestionListViewAdapter();
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);

    private List<QuestionBank.TKListDataEntity> mQuestionList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> mResultList = new ArrayList<>();
    private List<QuestionBank.TKListDataEntity> mErrorQuestionList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> mErrorResultList = new ArrayList<>();
    private List<Integer> lastPositionList = new ArrayList<>();

    private String filePath = "";
    private String KM = "";
    private String CX = "";
    private int currentAllPosition = 0;
    private int currentErrorPosition = 0;
    private int oldAllPosition = 0;
    private int oldErrorPosition = 0;
    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Select_CX_Type = "CX_Type";
    private boolean isStart,isCollect;

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
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dada/Exam/";
        Check.CheckDir(filePath);
        mImageButton_Return = (ImageButton) findViewById(R.id.imageButton_return);
        mTextView_Title = (TextView) findViewById(R.id.actionbar_Title);
        mTextView_Title.setText("错题解析");
        mImageView_Logo = (ImageView) findViewById(R.id.imageView_Logo);
        mImageView_Logo.setVisibility(View.GONE);
        mImageButton_Return.setOnClickListener(this);
        mLinearLayout_no_exam = (LinearLayout) findViewById(R.id.no_exam);
        mButton_error_analyze = (Button) findViewById(R.id.button_error_analyze);
        mButton_all_analyze = (Button) findViewById(R.id.button_all_analyze);
        mButton_error_analyze.setOnClickListener(this);
        mButton_all_analyze.setOnClickListener(this);
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
        button_1.setImageView(R.mipmap.position);
        button_2.setImageView(R.mipmap.shouchang);
        button_3.setImageView(R.mipmap.paichu);
        button_1.setText(getAlreadyText());
        button_2.setText("收藏");
        button_3.setText("排除");
        mLinearLayout_no_exam.setVisibility(View.VISIBLE);
        setCurrentAnalyzeButton(mButton_error_analyze, false);
        getQuestion();
        mButton_error_analyze.setText(getErrorPointTitle());
        if(mErrorQuestionList.isEmpty())
            setCurrentAnalyzeButton(mButton_all_analyze,false);
    }

    private void setCurrentAnalyzeButton(Button button,boolean canTurn) {
        switch (button.getId()) {
            case R.id.button_error_analyze:
                //oldAllPosition = mViewPager.getCurrentItem();
                button.setTextColor(getResources().getColor(R.color.color_button_select));
                mButton_all_analyze.setTextColor(getResources().getColor(R.color.color_button_no_select));
                if(canTurn)
                    mViewPager.setCurrentItem(oldErrorPosition);
                button.setText(getErrorPointTitle());
                break;
            case R.id.button_all_analyze:
                //oldErrorPosition = mViewPager.getCurrentItem();
                button.setTextColor(getResources().getColor(R.color.color_button_select));
                mButton_error_analyze.setTextColor(getResources().getColor(R.color.color_button_no_select));
                if(canTurn)
                    mViewPager.setCurrentItem(oldAllPosition);
                break;
        }
    }

    private String getErrorPointTitle() {
        if(!mErrorQuestionList.isEmpty())
            return "错题解析("+ (currentErrorPosition+1) + "/" + mErrorQuestionList.size()+")";
        return "错题解析";
    }

    private void initSelectState() {
        Cursor cursor = myDataBase.queryMNKSResult(KM,CX);
        mQuestionList.clear();
        mResultList.clear();
        while (cursor.moveToNext()) {
            mQuestionList = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("MNKS_questionList")),
                    new TypeToken<List<QuestionBank.TKListDataEntity>>() {
                    }.getType());
            mResultList = new Gson().fromJson(cursor.getString(cursor.getColumnIndex("MNKS_resultList")),
                    new TypeToken<List<ExamResult.STResultsEntity>>() {
                    }.getType());
        }
        cursor.close();
        mErrorQuestionList.clear();
        mErrorResultList.clear();

        int i = 0;
        for(ExamResult.STResultsEntity result : mResultList) {
            if("F".equals(result.getEvl())) {
                mErrorQuestionList.add(mQuestionList.get(i));
                mErrorResultList.add(result);
            }
            i++;
        }
        List<QuestionBank.TKListDataEntity> questionList = new ArrayList<>();
        questionList.addAll(mErrorQuestionList);
        questionList.addAll(mQuestionList);
        List<ExamResult.STResultsEntity> resultList = new ArrayList<>();
        resultList.addAll(mErrorResultList);
        resultList.addAll(mResultList);

        oldErrorPosition = 0;
        oldAllPosition = mErrorQuestionList.size();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), questionList, resultList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void getQuestion() {
        initSelectState();
        button_1.setText(getAlreadyText());
        if(!mQuestionList.isEmpty())
            buttonCollectChange(mQuestionList.get(0).getStxh());
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
                ignore();
                break;
            case R.id.button_error_analyze:
                if(!mErrorQuestionList.isEmpty())
                    setCurrentAnalyzeButton((Button) v,true);
                else
                    Toast.makeText(this,"没有错题",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_all_analyze:
                setCurrentAnalyzeButton((Button) v,true);
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
        if (mPopupWindow_Question == null) {
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
                    mPopupWindow_Question.dismiss();
                }
            });
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            mPopupWindow_Question = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, point.y / 10 * 6);

        }
        gridLayoutManager.scrollToPosition(currentAllPosition - mErrorQuestionList.size());
        questionListViewAdapter.setCurrentPosition(currentAllPosition - mErrorQuestionList.size());
        questionListViewAdapter.notifyDataSetChanged();
        mPopupWindow_Question.setFocusable(true);
        mPopupWindow_Question.setOutsideTouchable(true);
        mPopupWindow_Question.setBackgroundDrawable(new BitmapDrawable(getResources(),(Bitmap) null));
        mPopupWindow_Question.setAnimationStyle(R.style.MyExamTheme);
        mPopupWindow_Question.showAtLocation(this.findViewById(R.id.view_bottom), Gravity.CENTER, 0, 500);
        dimBackground(1.0f, 0.3f);
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

    private void ignore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("真的会了,要排除此题?");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("果断排除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteError(mQuestionList.get(currentAllPosition).getStxh());
                saveIgnore(mQuestionList.get(currentAllPosition).getStxh());
                mQuestionList.remove(currentAllPosition);
                mResultList.remove(currentAllPosition);
                mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList));
                mViewPager.setCurrentItem(currentAllPosition <= mQuestionList.size() ? currentAllPosition : mQuestionList.size());
                button_1.setText(getAlreadyText());
                if(currentAllPosition < mErrorQuestionList.size()) {
                    mErrorQuestionList.remove(currentAllPosition);
                    mErrorResultList.remove(currentAllPosition);
                    currentErrorPosition = currentAllPosition;
                    mButton_error_analyze.setText(getErrorPointTitle());
                }

            }
        });
        builder.show();
    }

    private boolean deleteError(String stxh) {
        return myDataBase.deleteError(stxh,KM);
    }

    private void saveIgnore(String stxh) {
        if (myDataBase.insertIgnore(stxh,KM)) {
            Toast.makeText(this,"排除本题成功",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"排除本题失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClicked(int position) {
        mPopupWindow_Question.dismiss();
        mViewPager.setCurrentItem(mErrorQuestionList.size()+position);
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
            mExamFragment.setIsReviewError(false);
            mExamFragment.setIsShowJX(true);
            mExamFragment.setIsCTJX(true);
            if(position < mErrorQuestionList.size())
                mExamFragment.setExam(DataAndImageToExam(false, position + 1, this.questionList.get(position)));
            else
                mExamFragment.setExam(DataAndImageToExam(false, position + 1 - mErrorQuestionList.size(), this.questionList.get(position)));
            return mExamFragment;
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }

    private int getResultSelectSize() {
        int i = 0;
        for(ExamResult.STResultsEntity resultsEntity : mResultList) {
            if(isSelect(resultsEntity.getXzda())) {
                i++;
            }
        }
        return i;
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
            if(!isStart && i == (mQuestionList.size()+mErrorQuestionList.size())-1 && v == 0 && i1 == 0) {
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
        if(i < mErrorResultList.size()) {
            currentErrorPosition = i;
            oldErrorPosition = i;
            setCurrentAnalyzeButton(mButton_error_analyze,false);
            buttonCollectChange(mQuestionList.get(i).getStxh());
        } else {
            oldAllPosition = i;
            setCurrentAnalyzeButton(mButton_all_analyze, false);
            buttonCollectChange(mQuestionList.get(i-mErrorQuestionList.size()).getStxh());
        }
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