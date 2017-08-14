package com.dada.mylibrary.Gson;

/**
 * Created by zhangdongdong on 2015/9/24.
 */
public class ErrorInfo {

    /**
     * msg : 请登录
     * errType : 1
     * success : false
     */

    private String msg;
    private int errType;
    private boolean success;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setErrType(int errType) {
        this.errType = errType;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public int getErrType() {
        return errType;
    }

    public boolean getSuccess() {
        return success;
    }
}
