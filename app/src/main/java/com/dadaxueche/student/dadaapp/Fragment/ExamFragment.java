package com.dadaxueche.student.dadaapp.Fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dada.mylibrary.Gson.ExamResult;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.Exam;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamFragment extends Fragment implements View.OnClickListener {
    private Exam mExam = new Exam();
    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private String filePath;
    private int mPosition = 0;
    private int sttx = 0;
    private int image_widght;
    private int image_height;
    private String bzda = "";
    private String result_Select = "";
    private ExamResult.STResultsEntity mExamResult = new ExamResult.STResultsEntity();
    private boolean isSelect ,isReviewError,isShowJX,isCTJX;
    private Button mButton_EnsureAnswer;

    private int[] ids_image = new int[] {R.id.image_exam_answer_1,
            R.id.image_exam_answer_2,
            R.id.image_exam_answer_3,
            R.id.image_exam_answer_4};
    private int[] ids_radioButton = new int[] {R.id.radioButton_exam_answer_1,
            R.id.radioButton_exam_answer_2,
            R.id.radioButton_exam_answer_3,
            R.id.radioButton_exam_answer_4};

    private OnRadioButtonClicked mOnRadioButtonClick;
    private OnButtonClicked onButtonClicked;

    public ExamFragment() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        filePath = sdcardDir.getAbsolutePath() + "/Dada/Exam/";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        image_widght = (int)getResources().getDimension(R.dimen.image_size);
        image_height = (int)getResources().getDimension(R.dimen.image_size);
        return inflater.inflate(R.layout.exam,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyExam myExam = new MyExam();

        myExam.textView_Stlx = (TextView) view.findViewById(R.id.textView_stlx);
        myExam.textView_Title = (TextView) view.findViewById(R.id.textView_Title);
        myExam.imageView = (SimpleDraweeView) view.findViewById(R.id.frsco_imageView);
        myExam.videoView = (VideoView) view.findViewById(R.id.videoView);
        myExam.videoView.setVisibility(View.GONE);
        myExam.group_Answer = (LinearLayout) view.findViewById(R.id.group_Answer);
        myExam.button_EnsureAnswer = (Button) view.findViewById(R.id.button_EnsureAnswer);
        myExam.button_EnsureAnswer.setOnClickListener(this);
        mButton_EnsureAnswer = myExam.button_EnsureAnswer;
        myExam.textView_ReviewError = (TextView) view.findViewById(R.id.textView_review_error);
        myExam.view_jixi = (LinearLayout) view.findViewById(R.id.view_ctjx);

        if(isShowJX) {
            myExam.view_jixi.setVisibility(View.VISIBLE);
            //final ImageButton mImageButton_up = (ImageButton)view.findViewById(R.id.imageButton_open);
            final TextView mTextView_jiexi = (TextView) view.findViewById(R.id.textView_jiexi);
            final ScrollView mScrollView_exam = (ScrollView) view.findViewById(R.id.scrollView_exam);

//            mImageButton_up.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mTextView_jiexi.getMaxLines() == 5) {
//                        mTextView_jiexi.setMaxLines(100);
//                        new Handler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //mScrollView_exam.fullScroll(ScrollView.FOCUS_DOWN);
//                                mImageButton_up.setImageResource(R.mipmap.xiangxia);
//                            }
//                        });
//                    } else {
//                        mTextView_jiexi.setMaxLines(5);
//                        new Handler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //mScrollView_exam.fullScroll(ScrollView.FOCUS_UP);
//                                mImageButton_up.setImageResource(R.mipmap.xiangshang);
//                            }
//                        });
//                    }
//                }
//            });
        }

        if(isReviewError) {
            myExam.textView_ReviewError.setVisibility(View.VISIBLE);
            myExam.textView_ReviewError.setText("复习错题第" + (mPosition+1) + "道");
        } else {
            myExam.textView_ReviewError.setVisibility(View.INVISIBLE);
        }
        myExam.textView_Title.setText(mExam.getmTitle());
        if(mExam.getmZplx() == 1) {
            myExam.imageView.setVisibility(View.VISIBLE);
            myExam.imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 450));
            String fileName = mExam.getFileName();
            if(fileName.contains(".")) {
                File file = new File(filePath + fileName);
                if (!file.exists()) {
                    Toast.makeText(getActivity(), "图片不存在，请下载", Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = Uri.parse("file://" + file);
                    ImageRequest request = ImageRequestBuilder
                            .newBuilderWithSource(uri)
                            .setResizeOptions(new ResizeOptions(400, 200))
                            .build();
                    PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                            .setOldController(myExam.imageView.getController())
                            .setImageRequest(request)
                            .build();
                    myExam.imageView.setController(controller);
                }
            }
        } else if (mExam.getmZplx() == 3) {
            String fileName = mExam.getFileName();
            myExam.imageView.setVisibility(View.GONE);
            myExam.videoView.setVisibility(View.VISIBLE);
            myExam.videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,450));

            myExam.videoView.setVideoPath(filePath  + fileName);
            myExam.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });

        } else if(mExam.getmZplx() == 0) {
            myExam.imageView.setVisibility(View.VISIBLE);
            myExam.imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 200));
        }
        sttx = mExam.getmSttx();
        switch (sttx) {
            case 1:
                myExam.textView_Stlx.setText("判断");
                setGroup();
                myExam.button_EnsureAnswer.setVisibility(View.GONE);
                break;
            case 2:
                myExam.textView_Stlx.setText("单选");
                if(linearLayouts.isEmpty())
                    Collections.shuffle(mExam.getArray_Answer());
                setGroup();
                myExam.button_EnsureAnswer.setVisibility(View.GONE);
                break;
            case 3:
                myExam.textView_Stlx.setText("多选");
                if(linearLayouts.isEmpty())
                    Collections.shuffle(mExam.getArray_Answer());
                setGroup();
                myExam.button_EnsureAnswer.setVisibility(View.VISIBLE);
                break;
        }

        bzda = mExamResult.getBzda();
        result_Select = mExamResult.getXzda();
        if (savedInstanceState != null) {
            bzda = savedInstanceState.getString("bzda");
            result_Select = savedInstanceState.getString("xzda");
            if(result_Select==null) {
                bzda = "";
                result_Select = "";
            }
        }
        setData(bzda);

        int i = 0;
        for (LinearLayout linearLayout : linearLayouts) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout mLinearLayout_VERTICAL = new LinearLayout(getActivity());
            mLinearLayout_VERTICAL.setLayoutParams(layoutParams);
            mLinearLayout_VERTICAL.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            textView.setBackgroundColor(getActivity().getResources().getColor(R.color.color_radioButton_line));
            mLinearLayout_VERTICAL.addView(textView);
            mLinearLayout_VERTICAL.addView(linearLayout);
            if(i == linearLayouts.size()-1) {
                textView = new TextView(getActivity());
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                textView.setBackgroundColor(getActivity().getResources().getColor(R.color.color_radioButton_line));
                mLinearLayout_VERTICAL.addView(textView);
            }
            myExam.group_Answer.addView(mLinearLayout_VERTICAL);
            i++;
        }
    }

    private void setGroup() {
        int i = 0;
        for (String[] str : mExam.getArray_Answer()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout mLinearLayout_HORIZONTAL = new LinearLayout(getActivity());
            mLinearLayout_HORIZONTAL.setLayoutParams(layoutParams);
            mLinearLayout_HORIZONTAL.setHorizontalGravity(LinearLayout.HORIZONTAL);
            mLinearLayout_HORIZONTAL.setGravity(Gravity.CENTER_VERTICAL);

            ImageView imageView = new ImageView(getActivity());
            imageView.setId(ids_image[i]);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(image_widght, image_height));
            imageView.setPadding(0, 0, 20, 0);

            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(str[1]);
            radioButton.setTag(R.id.tag_first, str[0]);
            radioButton.setTag(R.id.tag_second, false);
            radioButton.setId(ids_radioButton[i]);
            radioButton.setOnClickListener(this);

            mLinearLayout_HORIZONTAL.addView(imageView);
            mLinearLayout_HORIZONTAL.addView(radioButton);
            mLinearLayout_HORIZONTAL.setPadding((int) getResources().getDimension(R.dimen.image_size), (int) getResources().getDimension(R.dimen.text_size)/2, (int) getResources().getDimension(R.dimen.image_size), (int) getResources().getDimension(R.dimen.text_size)/2);
            linearLayouts.add(mLinearLayout_HORIZONTAL);
            i++;
        }
    }

    public void clearGroup() {
        int i= 0;
        for (LinearLayout linearLayout : linearLayouts) {
            ImageView imageView = (ImageView) linearLayout.findViewById(ids_image[i]);
            RadioButton radioButton = (RadioButton) linearLayout.findViewById(ids_radioButton[i]);
            imageView.setImageBitmap(null);
            radioButton.setChecked(false);
            radioButton.setTag(R.id.tag_second,false);
            i++;
        }
    }

    @Override
    public void onClick(View v) {
        if(mOnRadioButtonClick != null) {
            if (v.getId() == R.id.button_EnsureAnswer) {
                if (onButtonClicked != null) {
                    onButtonClicked.onButtonClicked();
                    v.setVisibility(View.GONE);
                } else {
                    Log.i("错误", "未设置接口");
                }
            } else {
                RadioButton radioButton = (RadioButton) v;
                int i = 0;
                for (LinearLayout linearLayout : linearLayouts) {
                    RadioButton radioButton1 = (RadioButton) linearLayout.findViewById(ids_radioButton[i]);
                    if (radioButton1 == radioButton) {
                        linearLayout.setBackgroundColor(getResources().getColor(R.color.color_radioButtonBack));
                    } else {
                        linearLayout.setBackgroundColor(getResources().getColor(R.color.White));
                    }
                }
                switch (sttx) {
                    case 1:
                    case 2:
                        result_Select = radioButton.getTag(R.id.tag_first).toString();
                        setData(mExamResult.getBzda());
                        break;
                    case 3:
                        if ((boolean) radioButton.getTag(R.id.tag_second)) {
                            radioButton.setChecked(false);
                            result_Select = result_Select.replace(String.valueOf(radioButton.getTag(R.id.tag_first)), "");
                        } else {
                            radioButton.setChecked(true);
                            result_Select += (radioButton.getTag(R.id.tag_first));
                        }
                        radioButton.setTag(R.id.tag_second, radioButton.isChecked());
                        break;
                }
                if (result_Select.length() > 1) {
                    mButton_EnsureAnswer.setEnabled(true);
                } else
                    mButton_EnsureAnswer.setEnabled(false);
                mOnRadioButtonClick.onRadioButtonClicked(sttx, result_Select, mPosition);
            }
        } else {
            clearGroup();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isCTJX) {
            outState.putString("xzda", result_Select);
            outState.putString("bzda", mExamResult.getBzda());
        } else {
            if(isSelect && !result_Select.isEmpty()) {
                outState.putString("xzda", result_Select);
                outState.putString("bzda", mExamResult.getBzda());
            } else {
                clearGroup();
            }
        }
    }

    public void setData(String bzda) {
        if((isCTJX && !bzda.isEmpty()) || (!isCTJX && !bzda.isEmpty() && !result_Select.isEmpty())) {
            isSelect = true;
            int i = 0;
            for (LinearLayout linearLayout : linearLayouts) {
                ImageView imageView = (ImageView) linearLayout.findViewById(ids_image[i]);
                RadioButton radioButton = (RadioButton) linearLayout.findViewById(ids_radioButton[i]);
                if(bzda.contains(radioButton.getTag(R.id.tag_first).toString())) {
                    imageView.setImageResource(R.mipmap.zhengque);
                } else {
                    imageView.setImageResource(R.mipmap.cuowu);
                }
                if (result_Select.contains(radioButton.getTag(R.id.tag_first).toString())) {
                    radioButton.setChecked(true);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.color_radioButtonBack));
                } else {
                    radioButton.setChecked(false);
                }
                radioButton.setClickable(false);
                i++;
            }
        }
    }

    public void setIsShowJX(boolean isShowJX) {
        this.isShowJX = isShowJX;
    }

    public void setIsCTJX(boolean isCTJX) {
        this.isCTJX = isCTJX;
    }

    private class MyExam {
        TextView textView_Stlx;
        TextView textView_ReviewError;
        TextView textView_Title;
        SimpleDraweeView imageView;
        VideoView videoView;
        LinearLayout group_Answer;
        Button button_EnsureAnswer;
        LinearLayout view_jixi;
        ImageButton imageButton_Up;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public void setOnButtonClicked(OnButtonClicked onButtonClicked) {
        this.onButtonClicked = onButtonClicked;
    }

    public void setIsReviewError(boolean isReviewError) {
        this.isReviewError = isReviewError;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setExam(Exam mExam) {
        this.mExam = mExam;
    }

    public void setExamResult(ExamResult.STResultsEntity mExamResult) {
        this.mExamResult = mExamResult;
    }

    public void setOnRadioButtonClick(OnRadioButtonClicked onRadioButtonClick) {
        this.mOnRadioButtonClick = onRadioButtonClick;
    }

    public interface OnRadioButtonClicked {
        void onRadioButtonClicked(int sttx, String result_Select, int position);
    }

    public interface OnButtonClicked {
        void onButtonClicked();
    }
}