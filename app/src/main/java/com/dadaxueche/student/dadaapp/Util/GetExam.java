package com.dadaxueche.student.dadaapp.Util;

import com.dada.mylibrary.Gson.QuestionBank;

import java.util.ArrayList;

/**
 * Created by wpf on 8-10-0010.
 */
public class GetExam {

    private Exam mExam;

    public Exam getExam() {
        return mExam;
    }

    public void setExamByDada(boolean isReviewError,int current,QuestionBank.TKListDataEntity data) {
        mExam = new Exam();
        if(isReviewError)
            mExam.setmTitle(data.getStnr());
        else
            mExam.setmTitle(String.valueOf(current) + "、"+ data.getStnr());
        ArrayList<String[]> answerList = new ArrayList<>();
        if("1".equals(data.getSttx())) {
            answerList.add(new String[]{"Y","正确"});
            answerList.add(new String[]{"N","错误"});
        }else {
            answerList.add(new String[]{"A",data.getXzdaa()});
            answerList.add(new String[]{"B",data.getXzdab()});
            answerList.add(new String[]{"C",data.getXzdac()});
            answerList.add(new String[]{"D",data.getXzdad()});
        }
        mExam.setmArray_Answer(answerList);
        mExam.setmResult(data.getStda());
        mExam.setmSttx(Integer.valueOf(data.getSttx()));

        mExam.setmZplx(Integer.valueOf(data.getZplx()));
        mExam.setFileName("km"+data.getTklx()+"/"+data.getTxlj());

    }
}
