package com.dada.mylibrary.Gson;

/**
 * Created by wpf on 10-16-0016.
 */
public class Money {

    /**
     * msg : 非法请求
     * money : 1.0
     * success : true
     * name : 测试驾校2-C1-测试班
     */

    private String msg;
    private double money;
    private boolean success;
    private String name;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public double getMoney() {
        return money;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getName() {
        return name;
    }
}
