package com.dada.mylibrary.Gson;

/**
 * Created by zhangdongdong on 2015/10/22.
 */
public class Festival {

    /**
     * success : true
     * photo : http://www.dadaxueche.com/m/upload/bannerphoto/choujiang.jpg
     * title : 抽奖
     * url : http://www.dadaxueche.com/m/choujiang.html
     * isShow : true
     */

    private boolean success;
    private String photo;
    private String title;
    private String url;
    private boolean isShow;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean getIsShow() {
        return isShow;
    }
}
