package com.dadaxueche.student.dadaapp.Util;

import java.util.ArrayList;

public class Exam {
    private String mTitle;
    private String fileName;
    private ArrayList<String[]> mArray_Answer;
    private String mResult;
    private int mZplx;
    private int mSttx;

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public ArrayList<String[]> getArray_Answer() {
        return mArray_Answer;
    }

    public void setmArray_Answer(ArrayList<String[]> mArray_Answer) {
        this.mArray_Answer = mArray_Answer;
    }

    public String getResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getmZplx() {
        return mZplx;
    }

    public void setmZplx(int mZplx) {
        this.mZplx = mZplx;
    }

    public int getmSttx() {
        return mSttx;
    }

    public void setmSttx(int mSttx) {
        this.mSttx = mSttx;
    }
}
