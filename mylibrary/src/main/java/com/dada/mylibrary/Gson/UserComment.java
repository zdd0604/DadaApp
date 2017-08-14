package com.dada.mylibrary.Gson;

/**
 * Created by zhangdongdong on 2015/9/21.
 */
public class UserComment {

    /**
     * msg : 非法请求
     * success : false
     */

    private String msg;
    private boolean success;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getSuccess() {
        return success;
    }
}
