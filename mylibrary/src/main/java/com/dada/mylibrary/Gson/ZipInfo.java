package com.dada.mylibrary.Gson;

/**
 * Created by wpf on 9-28-0028.
 */
public class ZipInfo {

    /**
     * tksize : 130493
     * km1size : 10923165
     * success : true
     * version : 1.0
     * km4size : 37122618
     */

    private int tksize;
    private int km1size;
    private boolean success;
    private String version;
    private int km4size;
    private String url;
    private String msg;

    public void setTksize(int tksize) {
        this.tksize = tksize;
    }

    public void setKm1size(int km1size) {
        this.km1size = km1size;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setKm4size(int km4size) {
        this.km4size = km4size;
    }

    public int getTksize() {
        return tksize;
    }

    public int getKm1size() {
        return km1size;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getVersion() {
        return version;
    }

    public int getKm4size() {
        return km4size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
