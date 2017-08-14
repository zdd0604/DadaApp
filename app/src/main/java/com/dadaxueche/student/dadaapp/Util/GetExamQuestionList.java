package com.dadaxueche.student.dadaapp.Util;

import android.database.Cursor;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.Gson.QuestionBank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetExamQuestionList {

    private MyDataBase mDataBase = new MyDataBase();
    private String KM = "";
    private String CX = "";
    private String Type = "";
    private int QuestionType = 0;
    private int maxQuestionCountCurrent = 0;
    private int maxQuestionCount[][] = {{33, 17, 22, 11, 17}, {33, 33, 22, 12},{},{6,13,5,7,11,6,2}};
    private List<QuestionBank.TKListDataEntity> QuestionList = new ArrayList<>();
    private List<String> ignoreList = new ArrayList<>();

    private boolean isAlreadyAdd(String stxh) {
        for(QuestionBank.TKListDataEntity question : QuestionList) {
            if(question.getStxh().equals(stxh))
                return true;
        }
        return false;
    }

    public List<QuestionBank.TKListDataEntity> getSXLXQuestionDataArrayList() {
        getIgnore();
        QuestionList = new ArrayList<>();
        String[] cxs = CX.split(",");
        for(String CX : cxs) {
            Cursor cursor = mDataBase.queryExam(KM, CX);
            while (cursor.moveToNext()) {
                if (!isIgnore(cursor.getString(cursor.getColumnIndex("Exam_stxh")))
                        && !isAlreadyAdd(cursor.getString(cursor.getColumnIndex("Exam_stxh")))) {
                    QuestionList.add(getQuestionBank(cursor));
                }
            }
            cursor.close();
        }
        return QuestionList;
    }

    public List<QuestionBank.TKListDataEntity> getSJLXQuestionDataArrayList() {
        getSXLXQuestionDataArrayList();
        Collections.shuffle(QuestionList);
        return QuestionList;
    }

    public List<QuestionBank.TKListDataEntity> getBTMSQuestionDataArrayList() {
        getSXLXQuestionDataArrayList();
        return QuestionList;
    }

    public List<QuestionBank.TKListDataEntity> getMNKSQuestionDataArrayList() {
        QuestionList = new ArrayList<>();
        String[] cxs = CX.split(",");
        for(String CX : cxs) {
            if (KM.equals("1")) {
                if ("A1A2A3B1B2".contains(CX)) {
                    maxQuestionCountCurrent = 0;
                } else if ("C1C2C3C4C5".contains(CX)) {
                    maxQuestionCountCurrent = 1;
                }
            } else if (KM.equals("4")) {
                maxQuestionCountCurrent = 3;
            }
        }

        ArrayList<ArrayList<QuestionBank.TKListDataEntity>> arrayLists = new ArrayList<>();
        for (int i = 0; i < maxQuestionCount[maxQuestionCountCurrent].length; ++i) {
            arrayLists.add(new ArrayList<QuestionBank.TKListDataEntity>());
        }
        for(String CX : cxs) {
            Cursor cursor = mDataBase.queryExam(KM, CX);
            while (cursor.moveToNext()
                    && !isAlreadyAdd(cursor.getString(cursor.getColumnIndex("Exam_stxh")))) {
                QuestionBank.TKListDataEntity data = new QuestionBank.TKListDataEntity();
                String[] strings = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount(); ++i) {
                    strings[i] = cursor.getString(i);
                }
                data.setDatas(strings);
                if (cursor.getInt(cursor.getColumnIndex("Exam_stfl")) < arrayLists.size())
                    arrayLists.get(cursor.getInt(cursor.getColumnIndex("Exam_stfl")) - 1).add(data);
                else {
                    arrayLists.get(arrayLists.size() - 1).add(data);
                }
            }
            for (int i = 0; i < arrayLists.size(); ++i) {
                Collections.shuffle(arrayLists.get(i));
                for (int j = 0; j < maxQuestionCount[maxQuestionCountCurrent][i]; ++j) {
                    if (!arrayLists.get(i).isEmpty() && ((KM.equals("1") && QuestionList.size() < 100) || (KM.equals("4") && QuestionList.size() < 50)))
                        QuestionList.add(arrayLists.get(i).get(j));
                }
            }
            cursor.close();
        }
        Collections.sort(QuestionList);
        return QuestionList;
    }

    public List<QuestionBank.TKListDataEntity> getMyQuestionDataArrayList() {
        QuestionList = new ArrayList<>();
        switch (Type) {
            case "我的错题":
                Cursor cursor_error = mDataBase.queryError(KM);
                while (cursor_error.moveToNext()) {
                    if(QuestionType != 0) {
                        Cursor cursor = mDataBase.queryExamBySTFL(
                                cursor_error.getString(cursor_error.getColumnIndex("Error_stxh")),
                                String.valueOf(QuestionType));
                        while (cursor.moveToNext()) {
                            QuestionList.add(getQuestionBank(cursor));
                        }
                        cursor.close();
                    } else {
                        Cursor cursor = mDataBase.queryExam(
                                cursor_error.getString(cursor_error.getColumnIndex("Error_stxh")));
                        while (cursor.moveToNext()) {
                            QuestionList.add(getQuestionBank(cursor));
                        }
                        cursor.close();
                    }
                }
                cursor_error.close();
                break;
            case "我的收藏":
                Cursor cursor_collect = mDataBase.queryCollect(KM);
                while (cursor_collect.moveToNext()) {
                    if(QuestionType != 0) {
                        Cursor cursor = mDataBase.queryExamBySTFL(
                                cursor_collect.getString(cursor_collect.getColumnIndex("Collect_stxh")),
                                String.valueOf(QuestionType));
                        while (cursor.moveToNext()) {
                            QuestionList.add(getQuestionBank(cursor));
                        }
                        cursor.close();
                    } else {
                        Cursor cursor = mDataBase.queryExam(
                                cursor_collect.getString(cursor_collect.getColumnIndex("Collect_stxh")));
                        while (cursor.moveToNext()) {
                            QuestionList.add(getQuestionBank(cursor));
                        }
                        cursor.close();
                    }
                }
                cursor_collect.close();
                break;
            case "排除的题":
                Cursor cursor_ignore = mDataBase.queryIgnore(KM);
                while (cursor_ignore.moveToNext()) {
                    if(QuestionType != 0) {
                        Cursor cursor = mDataBase.queryExamBySTFL(
                                cursor_ignore.getString(cursor_ignore.getColumnIndex("Ignore_stxh")),
                                String.valueOf(QuestionType));
                        while (cursor.moveToNext()) {
                            QuestionList.add(getQuestionBank(cursor));
                        }
                        cursor.close();
                    } else {
                        Cursor cursor = mDataBase.queryExam (
                                cursor_ignore.getString(cursor_ignore.getColumnIndex("Ignore_stxh")));
                        while (cursor.moveToNext()) {
                            QuestionList.add(getQuestionBank(cursor));
                        }
                        cursor.close();
                    }
                }
                cursor_ignore.close();
                break;
        }
        return QuestionList;
    }

    public List<QuestionBank.TKListDataEntity> getErrorQuestionDataArrayList() {
        QuestionList = new ArrayList<>();
        getError();
        Collections.shuffle(QuestionList);
        return QuestionList;
    }

    private QuestionBank.TKListDataEntity getQuestionBank(Cursor cursor) {
        QuestionBank.TKListDataEntity data = new QuestionBank.TKListDataEntity();
        String[] strings = new String[cursor.getColumnCount()];
        for (int i = 0; i < cursor.getColumnCount(); ++i) {
            strings[i] = cursor.getString(i);
        }
        data.setDatas(strings);
        return data;
    }

    private void getError() {
        int i = 0;
        Cursor cursor = mDataBase.queryError(KM);
        while (cursor.moveToNext()) {
            int mGetErrorNum = 10;
            if(i< mGetErrorNum) {
                String[] cxs = CX.split(",");
                for(String CX : cxs) {
                    Cursor cursor1 = mDataBase.queryExam(KM, CX, cursor.getString(0));
                    while (cursor1.moveToNext()) {
                        if(!isAlreadyAdd(cursor1.getString(cursor1.getColumnIndex("Exam_stxh"))))
                            QuestionList.add(getQuestionBank(cursor1));
                    }
                    cursor1.close();
                }
            }
            i++;
        }
        cursor.close();
    }

    private void getIgnore() {
        ignoreList.clear();
        Cursor cursor = mDataBase.queryIgnore(KM);
        ignoreList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ignoreList.add(cursor.getString(0));
        }
        cursor.close();
    }

    private boolean isIgnore(String stxh) {
        for(String str : ignoreList) {
            if(str.equals(stxh))
                return true;
        }
        return false;
    }

    public void setCX(String CX) {
        this.CX = CX;
    }

    public void setKM(String KM) {
        this.KM = KM;
    }

    public String getKM() {
        return KM;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setQuestionType(int questionType) {
        QuestionType = questionType;
    }
}
