package com.dada.mylibrary.Gson;

/**
 * Created by wpf on 8-18-0018.
 */
public class Message {

    /**
     * ret : 0
     * message : 保存成功!
     */
    private String ret;
    private String message;

    public void setRet(String ret) {
        this.ret = ret;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRet() {
        return ret;
    }

    public String getMessage() {
        return message;
    }
}
