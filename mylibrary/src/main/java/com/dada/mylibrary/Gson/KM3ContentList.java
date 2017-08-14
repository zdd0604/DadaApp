package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-10-06
 * Time: 11:01
 */
public class KM3ContentList {

    /**
     * total : 3
     * success : true
     * rows : [{"subject":3,"time":"2015-09-10 00:00","title":"驾照科目三考试基本注意事项","url":"http://www.dadaxueche.com/m/kesan/kslbwz/ksjbzysx.html"},{"subject":3,"time":"2015-09-10 00:00","title":"科三考试技巧大汇总","url":"http://www.dadaxueche.com/m/kesan/kslbwz/ksksdzh.html"},{"subject":3,"time":"2015-09-10 00:00","title":"夜考注意事项","url":"http://www.dadaxueche.com/m/kesan/kslbwz/ykzysxzj.html"}]
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
         * subject : 3
         * time : 2015-09-10 00:00
         * title : 驾照科目三考试基本注意事项
         * url : http://www.dadaxueche.com/m/kesan/kslbwz/ksjbzysx.html
         */

        private int subject;
        private String time;
        private String title;
        private String url;

        public void setSubject(int subject) {
            this.subject = subject;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSubject() {
            return subject;
        }

        public String getTime() {
            return time;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }
}