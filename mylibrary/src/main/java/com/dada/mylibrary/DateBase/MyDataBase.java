package com.dada.mylibrary.DateBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.dada.mylibrary.Gson.ExamResult;
import com.dada.mylibrary.Gson.QuestionBank;


/**
 * Created by wpf on 8-11-0011.
 */

public class MyDataBase {
    private static final String DATABASE_NAME = "嗒嗒数据库";

    private String Table_Exam = "Exam_Table";

    private String Key_Exam_bh = "Exam_bh";                                                      //0
    private String Key_Exam_stxh = "Exam_stxh";                                                    //1
    private String Key_Exam_stda = "Exam_stda";                                                    //2
    private String Key_Exam_stid = "Exam_stid";                                                    //3
    private String Key_Exam_sycx = "Exam_sycx";                                                    //4
    private String Key_Exam_stsx = "Exam_stsx";                                                    //5
    private String Key_Exam_xzdab = "Exam_xzdab";                                                   //6
    private String Key_Exam_xzdaa = "Exam_xzdaa";                                                   //7
    private String Key_Exam_stfl = "Exam_stfl";                                                    //8
    private String Key_Exam_sttx = "Exam_sttx";                                                    //9
    private String Key_Exam_stnr = "Exam_stnr";                                                    //10
    private String Key_Exam_txlj = "Exam_txlj";                                                    //11
    private String Key_Exam_tklx = "Exam_tklx";                                                    //12
    private String Key_Exam_xzdad = "Exam_xzdad";                                                   //13
    private String Key_Exam_xzdac = "Exam_xzdac";                                                   //14
    private String Key_Exam_gxsj = "Exam_gxsj";                                                    //15
    private String Key_Exam_ksyy = "Exam_ksyy";                                                    //16
    private String Key_Exam_stlx = "Exam_stlx";                                                    //17
    private String Key_Exam_stbj = "Exam_stbj";                                                    //18
    private String Key_Exam_zplx = "Exam_zplx";                                                    //19
    private String Key_Exam_btjs = "Exam_btjs";                                                    //20

    private String Table_Exam_Result = "Exam_Result_Table";
    private String Key_Result_kscx = "Result_kscx";
    private String Key_Result_tklx = "Result_tklx";
    private String Key_Result_BeginTime = "Result_BeginTime";
    private String Key_Result_EndTime = "Result_EndTime";
    private String Key_Result_ksts = "Result_ksts";
    private String Key_Result_wcts = "Result_wcts";
    private String Key_Result_kscj = "Result_kscj";

    private String Table_Ignore = "Ignore_Table";
    private String Key_Ignore_stxh = "Ignore_stxh";
    private String Key_Ignore_km = "Ignore_km";

    private String Table_Collect = "Collect_Table";
    private String Key_Collect_stxh = "Collect_stxh";
    private String Key_Collect_km = "Collect_km";

    private String Table_Error = "Error_Table";
    private String Key_Error_stxh = "Error_stxh";
    private String Key_Error_km = "Error_km";

    private String Table_Right = "Right_Table";
    private String Key_Right_stxh = "Right_stxh";
    private String Key_Right_km = "Right_km";

    private String Table_State = "State_Table";
    private String Key_State_stid = "State_stid";
    private String Key_State_bzda = "State_bzda";
    private String Key_State_xzda = "State_xzda";
    private String Key_State_sj = "State_sj";
    private String Key_State_evl = "State_evl";
    private String Key_State_Type = "State_type";
    private String Key_State_KM = "State_km";
    private String Key_State_CX = "State_cx";

    private String Table_MNKS_Result = "MNKS_Result";
    private String Key_MNKS_km = "MNKS_km";
    private String Key_MNKS_cx = "MNKS_cx";
    private String Key_MNKS_questionList = "MNKS_questionList";
    private String Key_MNKS_resultList = "MNKS_resultList";

    private String Table_UserInfo = "Table_UserInfo";
    private String UserInfo_name = "UserInfo_name";
    private String UserInfo_identityId = "UserInfo_identityId";
    private String UserInfo_mobile = "UserInfo_mobile";
    private String UserInfo_photo = "UserInfo_photo";
    private String UserInfo_schoolName = "UserInfo_schoolName";
    private String UserInfo_longitude = "UserInfo_longitude";
    private String UserInfo_latitude = "UserInfo_latitude";
    private String UserInfo_url = "UserInfo_url";

    private String Table_VideoList = "Video_List";
    private String VideoKM = "VideoKM";
    private String VideoName = "VideoName";
    private String VideoSize = "VideoSize";
    private String VideoUrl = "VideoUrl";
    private String VideoIshot = "VideoIshot";
    private String VideoPhotourl = "VideoPhotourl";
    private String VideoDetail = "VideoDetail";

    private String Create_VideoList = "create table "
            + Table_VideoList + "( "
            + VideoKM + " text ,"
            + VideoName + " text ,"
            + VideoSize + " text , "
            + VideoUrl + " text  ,"
            + VideoIshot + " text , "
            + VideoPhotourl + " text , "
            + VideoDetail + " text "
            + ")";
    /**
     * school : {"mobile":"15101088501","telephone":"0311-86133584","id":1,"schoolName":"圣安驾校","type":0}
     * appraise : [{"level":4,"subject":0},{"level":3,"subject":1}]
     * success : true
     * dada_tel : 01082331865
     * order : {"licenseType":"c1","money":2500,"school":1,"car":"桑塔纳","classes":1,"className":"普通班","schoolName":"圣安驾校"}
     */
    private String Table_MySchool = "Table_MySchool";
    private String User_Mobile = "User_mobile";
    private String Order_id = "Order_id";
    private String Order_coachName = "Order_coachName";
    private String Order_licenseType = "Order_licenseType";
    private String Order_money = "Order_money";
    private String Order_school = "Order_school";
    private String Order_car = "Order_car";
    private String Order_classes = "Order_classes";
    private String Order_className = "Order_className";
    private String Order_schoolName = "Order_schoolName";
    private String Dada_telephone = "Dada_telephone";
    private String School_mobile = "School_mobile";
    private String School_telephone = "School_telephone";
    private String School_appraise = "School_appraise";

    private String Create_SchoolInfo = "create table "
            + Table_MySchool + "( "
            + User_Mobile + " text primary key,"
            + Order_id + " text, "
            + Order_coachName + " text,"
            + Order_licenseType + " text, "
            + Order_money + " text , "
            + Order_school + " text , "
            + Order_car + " text, "
            + Order_classes + " text , "
            + Order_className + " text, "
            + Order_schoolName + " text, "
            + Dada_telephone + " text , "
            + School_mobile + " text, "
            + School_telephone + " text ,"
            + School_appraise + " text "
            + ")";

    public String[] Keys = new String[]{
            Key_Exam_bh,
            Key_Exam_stxh,
            Key_Exam_stda,
            Key_Exam_stid,
            Key_Exam_sycx,
            Key_Exam_stsx,
            Key_Exam_xzdab,
            Key_Exam_xzdaa,
            Key_Exam_stfl,
            Key_Exam_sttx,
            Key_Exam_stnr,
            Key_Exam_txlj,
            Key_Exam_tklx,
            Key_Exam_xzdad,
            Key_Exam_xzdac,
            Key_Exam_gxsj,
            Key_Exam_ksyy,
            Key_Exam_stlx,
            Key_Exam_stbj,
            Key_Exam_zplx,
            Key_Exam_btjs
    };

    private String Create_Ignore = "create table "
            + Table_Ignore + "( "
            + Key_Ignore_stxh + " text primary key , "
            + Key_Ignore_km + " text not null "
            + ")";

    private String Create_Collect = "create table "
            + Table_Collect + "( "
            + Key_Collect_stxh + " text primary key , "
            + Key_Collect_km + " text not null "
            + ")";

    private String Create_Error = "create table "
            + Table_Error + "( "
            + Key_Error_stxh + " text primary key , "
            + Key_Error_km + " text not null "
            + ")";

    private String Create_Right = "create table "
            + Table_Right + "( "
            + Key_Right_stxh + " text primary key , "
            + Key_Right_km + " text not null "
            + ")";

    private String Create_State = "create table "
            + Table_State + "( "
            + Key_State_stid + " int , "
            + Key_State_bzda + " text not null , "
            + Key_State_xzda + " text , "
            + Key_State_sj + " datetime , "
            + Key_State_evl + " text , "
            + Key_State_Type + " text not null , "
            + Key_State_KM + " text not null , "
            + Key_State_CX + " text not null ,"
            + "PRIMARY KEY ("
            + Key_State_stid + " , "
            + Key_State_Type + " , "
            + Key_State_KM + " , "
            + Key_State_CX + ")"
            + ")";

    private String Create_Exam = "create table "
            + Table_Exam + "( "
            + Key_Exam_bh + " text , "
            + Key_Exam_stxh + " text , "
            + Key_Exam_stda + " text not null , "
            + Key_Exam_stid + " int primary key, "
            + Key_Exam_sycx + " text , "
            + Key_Exam_stsx + " text , "
            + Key_Exam_xzdab + " text , "
            + Key_Exam_xzdaa + " text , "
            + Key_Exam_stfl + " int , "
            + Key_Exam_sttx + " text , "
            + Key_Exam_stnr + " text , "
            + Key_Exam_txlj + " text , "
            + Key_Exam_tklx + " text , "
            + Key_Exam_xzdad + " text , "
            + Key_Exam_xzdac + " text , "
            + Key_Exam_gxsj + " datetime , "
            + Key_Exam_ksyy + " text , "
            + Key_Exam_stlx + " int , "
            + Key_Exam_stbj + " int , "
            + Key_Exam_zplx + " int , "
            + Key_Exam_btjs + " text "
            + ")";

    private String Create_Table_Exam_Result = "create table "
            + Table_Exam_Result + "( "
            + Key_Result_kscx + " text , "
            + Key_Result_tklx + " int  , "
            + Key_Result_BeginTime + " datetime not null , "
            + Key_Result_EndTime + " datetime not null , "
            + Key_Result_ksts + " int , "
            + Key_Result_wcts + " int , "
            + Key_Result_kscj + " int "
            + ")";

    private String Create_Table_MNKS_Result = "create table "
            + Table_MNKS_Result + "("
            + Key_MNKS_km + " text not null , "
            + Key_MNKS_cx + " text not null , "
            + Key_MNKS_questionList + " text not null , "
            + Key_MNKS_resultList + " text not null , "
            + "PRIMARY KEY ("
            + Key_MNKS_km + " , "
            + Key_MNKS_cx + ")"
            + ")";

    private String Create_Table_UserInfo = "create table "
            + Table_UserInfo + "( "
            + UserInfo_name + " text , "
            + UserInfo_identityId + " text  , "
            + UserInfo_mobile + " text primary key, "
            + UserInfo_photo + " text , "
            + UserInfo_schoolName + " text , "
            + UserInfo_longitude + " text , "
            + UserInfo_latitude + " text ,"
            + UserInfo_url + " text "
            + ")";

    private String[] tableList = new String[]{
            Table_Exam,
            Table_Error,
            Table_Collect,
            Table_Ignore,
            Table_Right,
            Table_State,
            Table_Exam_Result,
            Table_UserInfo,
            Table_MNKS_Result,
            Table_MySchool
    };

    private String[] tableCreateList = new String[]{
            Create_Exam,
            Create_Error,
            Create_Collect,
            Create_Ignore,
            Create_Right,
            Create_State,
            Create_Table_Exam_Result,
            Create_Table_UserInfo,
            Create_Table_MNKS_Result,
            Create_SchoolInfo,
            Create_VideoList
    };

    private static SQLiteDatabase db;
    private Context context;

    public MyDataBase() {
    }

    public MyDataBase(Context context) {
        this.context = context;
    }

    public void open() throws SQLiteException {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context, DATABASE_NAME, null, 1);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    private class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String aTableCreateList : tableCreateList) {
                db.execSQL(aTableCreateList);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            for (String aTableList : tableList) {
                db.execSQL("DROP TABLE IF EXISTS " + aTableList);
            }
            onCreate(db);
        }
    }

    public boolean insertExam(QuestionBank.TKListDataEntity data) {
        ContentValues cv = new ContentValues();
        String[] strings = data.getDatas();
        for (int i = 0; i < strings.length; ++i) {
            cv.put(Keys[i], strings[i]);
        }
        return db.insert(Table_Exam, null, cv) != -1;
    }

    public boolean deleteExam() {
        return db.delete(Table_Exam, null, null) != -1;
    }

    public boolean updateExam(String stxh, String[] strings) {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < strings.length; ++i) {
            if (!Keys[i].equals(Key_Exam_stxh))
                cv.put(Keys[i], strings[i]);
        }
        return db.update(Table_Exam, cv, Key_Exam_stxh + " =? ", new String[]{stxh}) != -1;
    }

    public Cursor queryExam(String stxh) {
        return db.query(Table_Exam, null, Key_Exam_stxh + " =? ",
                new String[]{stxh}, null, null, null);
    }

    public Cursor queryExam() {
        return db.query(Table_Exam, null, null, null, null, null, Key_Exam_sttx);
    }

    public Cursor queryExamBySTFL(String stxh, String stfl) {
        return db.query(Table_Exam, null, Key_Exam_stxh + " =? " + " AND " +
                Key_Exam_stfl + " =? ", new String[]{stxh, stfl}, null, null, Key_Exam_sttx);
    }

    public Cursor queryExam(String KM, String CX) {
        return db.query(Table_Exam, null, Key_Exam_tklx + " =? "
                + " AND "
                + Key_Exam_sycx + " like? ", new String[]{KM, "%" + CX + "%"}, null, null, Key_Exam_sttx);
    }

    public Cursor queryExam(String KM, String CX, String stxh) {
        return db.query(Table_Exam, null, Key_Exam_tklx + " =? "
                + " AND "
                + Key_Exam_sycx + " like? "
                + " AND "
                + Key_Exam_stxh + " =? ", new String[]{KM, "%" + CX + "%", stxh}, null, null, Key_Exam_sttx);
    }

    public Cursor queryExamByKM(String KM) {
        return db.query(Table_Exam, null, Key_Exam_tklx + " =? ", new String[]{KM}, null, null, Key_Exam_sttx);
    }

    public boolean deleteExamResult(String time) {
        return db.delete(Table_Exam_Result, Key_Result_BeginTime + " =? ", new String[]{time}) != -1;
    }

    public boolean insertExamResult(String kscx, String tklx, String beginTime, String endTime, int ksts, int wcts, int kscj) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_Result_kscx, kscx);
        contentValues.put(Key_Result_tklx, tklx);
        contentValues.put(Key_Result_BeginTime, beginTime);
        contentValues.put(Key_Result_EndTime, endTime);
        contentValues.put(Key_Result_ksts, ksts);
        contentValues.put(Key_Result_wcts, wcts);
        contentValues.put(Key_Result_kscj, kscj);
        return db.insert(Table_Exam_Result, null, contentValues) != -1;
    }

    public Cursor queryExamResult(String km) {
        return db.query(Table_Exam_Result, null, Key_Result_tklx + " =? ", new String[]{km}, null, null, null);
    }

    public boolean insertMNKSResult(String km, String cx, String MNKS_questionList, String MNKS_resultList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_MNKS_km, km);
        contentValues.put(Key_MNKS_cx, cx);
        contentValues.put(Key_MNKS_questionList, MNKS_questionList);
        contentValues.put(Key_MNKS_resultList, MNKS_resultList);
        return db.insert(Table_MNKS_Result, null, contentValues) != -1;
    }

    public boolean updateMNKSResult(String km, String cx, String MNKS_questionList, String MNKS_resultList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_MNKS_questionList, MNKS_questionList);
        contentValues.put(Key_MNKS_resultList, MNKS_resultList);
        return db.update(Table_MNKS_Result, contentValues, Key_MNKS_km + " =? "
                + " AND "
                + Key_MNKS_cx + " =? ", new String[]{km, cx}) != -1;
    }

    public boolean isEmptyMNKSResult(String km, String cx) {
        return db.query(Table_MNKS_Result, null, Key_MNKS_km + " =? "
                + " AND "
                + Key_MNKS_cx + " =? ", new String[]{km, cx}, null, null, null).getCount() <= 0;
    }

    public Cursor queryMNKSResult(String km, String cx) {
        return db.query(Table_MNKS_Result, null, Key_MNKS_km + " =? "
                + " AND "
                + Key_MNKS_cx + " =? ", new String[]{km, cx}, null, null, null);
    }

    public boolean deleteMNKSResult(String km, String cx) {
        return db.delete(Table_MNKS_Result, Key_MNKS_km + " =? "
                + " AND "
                + Key_MNKS_cx + " =? ", new String[]{km, cx}) != -1;
    }

    public boolean insertError(String stxh, String km) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_Error_stxh, stxh);
        contentValues.put(Key_Error_km, km);
        return db.insert(Table_Error, null, contentValues) != -1;
    }

    public Cursor queryError(String km) {
        return db.query(Table_Error, null, Key_Error_km + " =? ", new String[]{km}, null, null, null);
    }

    public boolean isError(String stxh, String km) {
        return db.query(Table_Error, null, Key_Error_stxh + " =? "
                        + "AND"
                        + Key_Error_km + " =? ",
                new String[]{stxh, km}, null, null, null).getCount() != 0;
    }

    public boolean deleteAllError(String km) {
        return db.delete(Table_Error, Key_Error_km + " =? ", new String[]{km}) != -1;
    }

    public boolean deleteError(String stxh, String km) {
        return db.delete(Table_Error, Key_Error_stxh + " =? " + " AND "
                + Key_Error_km + " =? ", new String[]{stxh, km}) != -1;
    }

    public boolean insertRight(String stxh, String km) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_Right_stxh, stxh);
        contentValues.put(Key_Right_km, km);
        return db.insert(Table_Right, null, contentValues) != -1;
    }

    public boolean deleteRight(String stxh, String km) {
        return db.delete(Table_Right, Key_Right_stxh + " =? " + " AND "
                + Key_Right_km + " =? ", new String[]{stxh, km}) != -1;
    }

    public boolean insertUserInfo(String name,
                                  String identityId,
                                  String mobile,
                                  String photo,
                                  String schoolname,
                                  String longitude,
                                  String latitude,
                                  String url) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserInfo_name, name);
        contentValues.put(UserInfo_identityId, identityId);
        contentValues.put(UserInfo_mobile, mobile);
        contentValues.put(UserInfo_photo, photo);
        contentValues.put(UserInfo_schoolName, schoolname);
        contentValues.put(UserInfo_longitude, longitude);
        contentValues.put(UserInfo_latitude, latitude);
        contentValues.put(UserInfo_url, url);
        return db.insert(Table_UserInfo, null, contentValues) != -1;
    }

    public Cursor queryUserInfo(String mobile) {
        return db.query(Table_UserInfo, null, UserInfo_mobile + " =? ", new String[]{mobile}, null, null, null);
    }

    public boolean deleteUserInfo(String userInfo_mobile) {
        return db.delete(Table_UserInfo, UserInfo_mobile + " =? ", new String[]{userInfo_mobile}) != -1;
    }

    public boolean insertSchoolInfo(String user_Mobile, String order_id,
                                    String order_coachName, String order_licenseType,
                                    String order_money, String order_school, String order_car, String order_classes,
                                    String order_className, String order_schoolName,
                                    String dada_telephone, String school_mobile,
                                    String school_telephone,String appraise) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(User_Mobile, user_Mobile);
        contentValues.put(Order_id, order_id);
        contentValues.put(Order_coachName, order_coachName);
        contentValues.put(Order_licenseType, order_licenseType);
        contentValues.put(Order_money, order_money);
        contentValues.put(Order_school, order_school);
        contentValues.put(Order_car, order_car);
        contentValues.put(Order_classes, order_classes);
        contentValues.put(Order_className, order_className);
        contentValues.put(Order_schoolName, order_schoolName);
        contentValues.put(Dada_telephone, dada_telephone);
        contentValues.put(School_mobile, school_mobile);
        contentValues.put(School_telephone, school_telephone);
        contentValues.put(School_appraise, appraise);
        return db.insert(Table_MySchool, null, contentValues) != -1;
    }

    public Cursor querySchoolInfo(String user_Mobile) {
        return db.query(Table_MySchool, null, User_Mobile + " =? ", new String[]{user_Mobile}, null, null, null);
    }

    public boolean deleteSchoolInfo(String user_Mobile) {
        return db.delete(Table_MySchool, User_Mobile + " =? ", new String[]{user_Mobile}) != -1;
    }


    public boolean isRight(String stxh, String km) {
        return db.query(Table_Right, null, Key_Right_stxh + " =? "
                        + "AND"
                        + Key_Right_km + " =? ",
                new String[]{stxh, km}, null, null, null).getCount() != 0;
    }

    public Cursor queryRight(String km) {
        return db.query(Table_Right, null, Key_Right_km + " =? ", new String[]{km}, null, null, null);
    }

    public boolean deleteAllRight(String km) {
        return db.delete(Table_Right, Key_Right_km + " =? ", new String[]{km}) != -1;
    }

    public boolean insertCollect(String stxh, String km) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_Collect_stxh, stxh);
        contentValues.put(Key_Collect_km, km);
        return db.insert(Table_Collect, null, contentValues) != -1;
    }

    public Cursor queryCollect(String km) {
        return db.query(Table_Collect, null, Key_Collect_km + " =? ", new String[]{km}, null, null, null);
    }

    public boolean isCollect(String stxh) {
        return db.query(Table_Collect, null, Key_Collect_stxh + " =? ", new String[]{stxh}, null, null, null).getCount() != 0;
    }

    public boolean deleteCollect(String stxh, String km) {
        return db.delete(Table_Collect, Key_Collect_km + " =? " + " AND " + Key_Collect_stxh + " =? ", new String[]{km, stxh}) != -1;
    }

    public boolean deleteAllCollect(String km) {
        return db.delete(Table_Collect, Key_Collect_km + " =? ", new String[]{km}) != -1;
    }

    public Cursor queryIgnore(String km) {
        return db.query(Table_Ignore, null, Key_Ignore_km + " =? ", new String[]{km}, null, null, null);
    }

    public boolean isIgnore(String stxh, String km) {
        return db.query(Table_Ignore,
                null,
                Key_Ignore_stxh + " =? "
                        + " AND "
                        + Key_Ignore_km + " =? ",
                new String[]{stxh, km}
                , null, null, null)
                .getCount()
                != 0;
    }

    public boolean insertIgnore(String stxh, String km) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_Ignore_stxh, stxh);
        contentValues.put(Key_Ignore_km, km);
        return db.insert(Table_Ignore, null, contentValues) != -1;
    }

    public boolean deleteIgnore(String stxh, String km) {
        return db.delete(Table_Ignore, Key_Ignore_stxh + " =? " + " AND "
                + Key_Ignore_km + " =?", new String[]{stxh, km}) != -1;
    }

    public boolean deleteAllIgnore(String km) {
        return db.delete(Table_Ignore, Key_Ignore_km + " =? ", new String[]{km}) != -1;
    }

    public boolean insertState(String type, String KM, String CX, ExamResult.STResultsEntity entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_State_stid, entity.getStid());
        contentValues.put(Key_State_bzda, entity.getBzda());
        contentValues.put(Key_State_xzda, entity.getXzda());
        contentValues.put(Key_State_sj, entity.getSj().toString());
        contentValues.put(Key_State_evl, entity.getEvl());
        contentValues.put(Key_State_Type, type);
        contentValues.put(Key_State_KM, KM);
        contentValues.put(Key_State_CX, CX);
        return db.insert(Table_State, null, contentValues) != -1;
    }

    public boolean updateState(String type, String KM, String CX, ExamResult.STResultsEntity entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_State_bzda, entity.getBzda());
        contentValues.put(Key_State_xzda, entity.getXzda());
        contentValues.put(Key_State_sj, entity.getSj().toString());
        contentValues.put(Key_State_evl, entity.getEvl());
        return db.update(Table_State, contentValues, Key_State_stid + " =? " + " AND "
                        + Key_State_Type + " =? " + " AND "
                        + Key_State_KM + " =? " + " AND "
                        + Key_State_CX + " =? ",
                new String[]{String.valueOf(entity.getStid()), type, KM, CX}) != -1;
    }

    public Cursor queryState(String type, String KM, String CX) {
        return db.query(Table_State, null, Key_State_Type + " =? " + " AND "
                + Key_State_KM + " =? " + " AND "
                + Key_State_CX + " =? ", new String[]{type, KM, CX}, null, null, null);
    }

    public boolean isEmptyState(String type, String km, String cx) {
        return db.query(Table_State, null, Key_State_Type + " =? " + " AND "
                + Key_State_KM + " =? " + " AND "
                + Key_State_CX + " =? ", new String[]{type, km, cx}, null, null, null).getCount() <= 0;
    }

    public boolean deleteState(String type, String km, String cx) {
        return db.delete(Table_State, Key_State_Type + " =? " + " AND "
                + Key_State_KM + " =? " + " AND "
                + Key_State_CX + " =? ", new String[]{type, km, cx}) != -1;
    }

    public boolean insertVideoList(String km, String name, String size, String url, String ishot, String photourl, String detail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VideoKM, km);
        contentValues.put(VideoName, name);
        contentValues.put(VideoSize, size);
        contentValues.put(VideoUrl, url);
        contentValues.put(VideoIshot, ishot);
        contentValues.put(VideoPhotourl, photourl);
        contentValues.put(VideoDetail, detail);
        return db.insert(Table_VideoList, null, contentValues) != -1;
    }

    public Cursor queryVideoList(String KM) {
        return db.query(Table_VideoList, null, VideoKM + "=?", new String[]{KM}, null, null, null);
    }

	 public Cursor queryVideoList2(String km, String name) {
        return db.query(Table_VideoList, null, VideoKM + " =?" + " AND " + VideoName + " =?", new String[]{km, name}, null, null, null);
    }

    public boolean deleteVideoList(String km) {
        return db.delete(Table_VideoList, VideoKM + " =? ", new String[]{km}) != -1;
    }
}
