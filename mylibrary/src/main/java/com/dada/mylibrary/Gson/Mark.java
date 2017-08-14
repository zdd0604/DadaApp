package com.dada.mylibrary.Gson;

/**
 * Created by wpf on 10-12-0012.
 */
public class Mark {

    /**
     * todayRank : 1
     * totalRank : 3
     * success : true
     * msg : 用户未注册
     * errType : 0
     */

    private int todayRank;
    private int totalRank;
    private boolean success;
    private String msg;
    private int errType;

    public void setTodayRank(int todayRank) {
        this.todayRank = todayRank;
    }

    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setErrType(int errType) {
        this.errType = errType;
    }

    public int getTodayRank() {
        return todayRank;
    }

    public int getTotalRank() {
        return totalRank;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getErrType() {
        return errType;
    }
}
