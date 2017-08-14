package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * Created by zhangdongdong on 2015/10/9.
 */
public class HomeBanner {

    /**
     * total : 6
     * success : true
     * rows : [{"photo":"http://www.dadaxueche.com/m/upload/bannerphoto/a2.jpg","title":"报名（a2）","url":"http://www.dadaxueche.com/m/index.html"},{"photo":"http://www.dadaxueche.com/m/upload/bannerphoto/a3.jpg","title":"下载学员端（a3）","url":"http://www.dadaxueche.com/m/index.html"},{"photo":"http://www.dadaxueche.com/m/upload/bannerphoto/a4.jpg","title":"下载教练端（a4）","url":"http://www.dadaxueche.com/m/index.html"},{"photo":"http://www.dadaxueche.com/m/upload/bannerphoto/a5.jpg","title":"下载驾校端（a5）","url":"http://www.dadaxueche.com/m/index.html"},{"photo":"http://www.dadaxueche.com/m/upload/bannerphoto/a6.jpg","title":"下载教练端（a6）","url":"http://www.dadaxueche.com/m/index.html"},{"photo":"http://www.dadaxueche.com/m/upload/bannerphoto/a7.jpg","title":"下载驾校端（a7）","url":"http://www.dadaxueche.com/m/index.html"}]
     */

    private int total;
    private boolean success;
    private List<RowsEntity> rows;

    public void setTotal(int total) {
        this.total = total;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public boolean getSuccess() {
        return success;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * photo : http://www.dadaxueche.com/m/upload/bannerphoto/a2.jpg
         * title : 报名（a2）
         * url : http://www.dadaxueche.com/m/index.html
         */

        private String photo;
        private String title;
        private String url;

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUrl(String url) {
            this.url = url;
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
    }
}
