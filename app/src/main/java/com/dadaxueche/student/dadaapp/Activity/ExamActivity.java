package com.dadaxueche.student.dadaapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ScrollView;
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
import com.dadaxueche.student.dadaapp.Util.GetExamQuestionList;
import com.dadaxueche.student.dadaapp.Util.SaveExamResult;
import com.dadaxueche.student.dadaapp.View.MyButton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExamActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener ,
        View.OnClickListener ,
        QuestionListViewAdapter.onItemClick  {

    private ImageButton mImageButton_Return;
    private TextView mTextView_Title;
    private Button mButton_Menu;
    private ViewPager mViewPager;
    private MyButton button_1,button_2,button_3;
    private PopupWindow mPopupWindow_Question = null;

    private SaveExamResult saveExamResult = new SaveExamResult(this);
    private MyDataBase myDataBase = new MyDataBase();
    private GetExamQuestionList mGetExamQuestionList;
    private ExamResult.STResultsEntity resultsEntity = new ExamResult.STResultsEntity();
    private ExamFragment mExamFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private QuestionListViewAdapter questionListViewAdapter = new QuestionListViewAdapter();
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);

    private List<QuestionBank.TKListDataEntity> mQuestionList = new ArrayList<>();
    private List<ExamResult.STResultsEntity> mResultList = new ArrayList<>();
    private List<Integer> lastPositionList = new ArrayList<>();

    private String filePath = "";
    private String KM = "";
    private int location  = 0;
    private String Select;
    private int currentAllPosition = 0;
    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Index = "Index";
    private String flag_Type = "Type";
    private boolean isCollect,isDoDeleteError,isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_exam);
        Intent intent = getIntent();
        Select = intent.getStringExtra(flag_Type);
        KM = intent.getStringExtra(flag_Select_KM_Type);
        location = intent.getIntExtra(flag_Index, 0);
        isDoDeleteError = getDOTrueDeleteError();
        init();
        initViewInfo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(1000);
    }

    private void init() {
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dada/Exam/";
        Check.CheckDir(filePath);

        mImageButton_Return = (ImageButton) findViewById(R.id.imageButton_return);
        mTextView_Title = (TextView) findViewById(R.id.actionbar_Title);
        mButton_Menu = (Button) findViewById(R.id.actionbar_Menu);

        mImageButton_Return.setOnClickListener(this);

        mButton_Menu.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList);
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
        button_1.setImageView(R.mipmap.shouchang);
        button_2.setImageView(R.mipmap.paichu);
        button_3.setImageView(R.mipmap.jiexi);
        button_3.setEnabled(false);
        button_3.setTextViewColor(Color.GRAY);
        button_1.setText("收藏");
        button_2.setText("排除");
        if(Select.contains("排除")) {
            button_2.setText("恢复");
        }
        button_3.setText("解析");
        mTextView_Title.setVisibility(View.GONE);
        getQuestion();
    }

    private void getQuestion() {
        if(mGetExamQuestionList == null) {
            mGetExamQuestionList = new GetExamQuestionList();
            mGetExamQuestionList.setKM(KM);
            mGetExamQuestionList.setType(Select);
            mGetExamQuestionList.setQuestionType(location);
        }
        mQuestionList.clear();
        mQuestionList.addAll(mGetExamQuestionList.getMyQuestionDataArrayList());
        initSelectList();
    }

    private void initSelectList() {
        if(!mQuestionList.isEmpty()) {
            mResultList.clear();
            for (int i = 0; i < mQuestionList.size(); ++i) {
                ExamResult.STResultsEntity stResultsEntity = new ExamResult.STResultsEntity();
                stResultsEntity.setStid(Integer.valueOf(mQuestionList.get(i).getStid()));
                stResultsEntity.setBzda(getSortString(mQuestionList.get(i).getStda()));
                mResultList.add(stResultsEntity);
            }
            buttonCollectChange(mQuestionList.get(0).getStxh());
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                collect();
                break;
            case R.id.button_2:
                if(button_2.getText().equals("排除")) {
                    ignore();
                } else if(button_2.getText().equals("恢复")) {
                    notIgnore();
                }
                break;
            case R.id.button_3:
                analyze();
                break;
            case R.id.imageButton_return:
                finish();
                break;

        }
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
            mPopupWindow_Question = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, point.y / 10 * 7);

        }
        gridLayoutManager.scrollToPosition(currentAllPosition);
        questionListViewAdapter.setCurrentPosition(currentAllPosition);
        questionListViewAdapter.notifyDataSetChanged();
        mPopupWindow_Question.setFocusable(true);
        mPopupWindow_Question.setOutsideTouchable(true);
        mPopupWindow_Question.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow_Question.setAnimationStyle(R.style.MyExamTheme);
        mPopupWindow_Question.showAtLocation(this.findViewById(R.id.view_bottom), Gravity.CENTER, 0, 500);
        final int color_Back = findViewById(R.id.window).getSolidColor();
        findViewById(R.id.window).setBackgroundColor(Color.GRAY);
        mPopupWindow_Question.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                findViewById(R.id.window).setBackgroundColor(color_Back);
            }
        });
    }

    private void collect() {
        String stxh = mQuestionList.get(currentAllPosition).getStxh();
        if(isCollect) {
            if (myDataBase.deleteCollect(stxh, KM))
                setButtonNoCollect();
        } else {
            if (myDataBase.insertCollect(stxh, KM))
                setButtonCollect();
        }
    }

    private void setButtonCollect() {
        button_1.setImageView(R.mipmap.shouchangdianji);
        //button_1.setText("收藏");
        isCollect = true;
    }

    private void setButtonNoCollect() {
        button_1.setImageView(R.mipmap.shouchang);
        //button_1.setText("收藏");
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
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                deleteError(mQuestionList.get(currentAllPosition).getStxh());
                saveIgnore(mQuestionList.get(currentAllPosition).getStxh());
                mQuestionList.remove(currentAllPosition);
                mResultList.remove(currentAllPosition);
                mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList));
                mViewPager.setCurrentItem(currentAllPosition <= mQuestionList.size() ? currentAllPosition : mQuestionList.size());
            }
        });
        builder.show();
    }

    private void notIgnore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要取消排除此题？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("果断恢复", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteIgnore(mQuestionList.get(currentAllPosition).getStxh());
                mQuestionList.remove(currentAllPosition);
                mResultList.remove(currentAllPosition);
                mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionList, mResultList));
                mViewPager.setCurrentItem(currentAllPosition <= mQuestionList.size() ? currentAllPosition : mQuestionList.size());
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
        final ScrollView mScrollView_exam = (ScrollView) view.findViewById(R.id.scrollView_exam);

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
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
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

    private void deleteIgnore(String stxh) {
        if (myDataBase.deleteIgnore(stxh, KM)) {
            Toast.makeText(this,"恢复本题成功",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"恢复本题失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClicked(int position) {
        mPopupWindow_Question.dismiss();
        mViewPager.setCurrentItem(position);
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
            mExamFragment.setIsReviewError(false);
            mExamFragment.setExam(DataAndImageToExam(false,position + 1, this.mQuestionList.get(position)));
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

    private void dealButtonClicked() {
        String result_Select = resultsEntity.getXzda();
        if(isSelect(result_Select) && !mExamFragment.isSelect()) {
            mResultList.set(currentAllPosition, resultsEntity);
            String Bzda = mResultList.get(currentAllPosition).getBzda();
            judgeError(result_Select,Bzda,0);
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
                judgeError(result_Select, Bzda,position);
            }
        }
    }

    private void judgeError(String result_Select,String Bzda,int position) {
        boolean error = !result_Select.equals(Bzda);
        if (error) {
            myDataBase.deleteRight(mQuestionList.get(currentAllPosition).getStxh(),KM);
            myDataBase.insertError(mQuestionList.get(currentAllPosition).getStxh(),KM);
        } else {
            myDataBase.insertRight(mQuestionList.get(currentAllPosition).getStxh(),KM);
            if (isDoDeleteError)
                myDataBase.deleteError(mQuestionList.get(currentAllPosition).getStxh(), KM);
            if(position == currentAllPosition)
                delay(position);
        }
    }

    private int delaySendTime = 1000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
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

    private String getSortString(String string) {
        char[] result = string.toCharArray();
        Arrays.sort(result);
        return Arrays.toString(result);
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

    private boolean getDOTrueDeleteError() {
        SharedPreferences sharedPreferences = getSharedPreferences("error",
                Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean("delete_state", false);
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
        buttonCollectChange(mQuestionList.get(i).getStxh());
//        if(rightList.get(i).isEmpty()) {
//            mPointButton_1.setmMessage(String.valueOf(getResultSelectSize()));
//            mPointButton_1.setmCenterColor(Color.GRAY);
//        } else {
//            if(rightList.get(i).equals("正确")) {
//                mPointButton_1.setmMessage("正确");
//                mPointButton_1.setmCenterColor(Color.GREEN);
//            } else {
//                mPointButton_1.setmMessage("错误");
//                mPointButton_1.setmCenterColor(Color.RED);
//            }
//        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if(i == 0) {
            isStart = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
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