package com.dada.mylibrary.Gson;

import java.util.List;

public class KM3Video {

    /**
     * total : 11
     * success : true
     * rows : [{"size":13.5,"subject":3,"name":"变更车道","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":6,"time":"1分05秒","detail":"http://www.baidu.com","isHot":1,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/bgcd.mp4"},{"size":16.7,"subject":3,"name":"超车","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":7,"time":"1分16秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/cc.mp4"},{"size":9.7,"subject":3,"name":"会车","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":8,"time":"0分43秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/hc.mp4"},{"size":21.7,"subject":3,"name":"靠边停车","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":9,"time":"1分29秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/kbtc.mp4"},{"size":15.4,"subject":3,"name":"起步","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":10,"time":"1分07秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/qb.mp4"},{"size":10,"subject":3,"name":"上车准备","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":11,"time":"0分38秒","detail":"http://www.baidu.com","isHot":1,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/sczb.mp4"},{"size":14,"subject":3,"name":"调头","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":12,"time":"1分03秒","detail":"http://www.baidu.com","isHot":1,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/dt.mp4"},{"size":12.7,"subject":3,"name":"通过路口","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":13,"time":"0分56秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/tglk.mp4"},{"size":6.55,"subject":3,"name":"通过人行道","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":14,"time":"0分29秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/tgrxd.mp4"},{"size":19.9,"subject":3,"name":"夜间行驶","photo":"http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg","id":15,"time":"1分18秒","detail":"http://www.baidu.com","isHot":0,"url":"http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/yjxs.mp4"}]
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
         * size : 13.5
         * subject : 3
         * name : 变更车道
         * photo : http://www.dadaxueche.com/m/upload/subjectvideo/subjecttwo/cftc.jpg
         * id : 6
         * time : 1分05秒
         * detail : http://www.baidu.com
         * isHot : 1
         * url : http://www.dadaxueche.com/m/upload/subjectvideo/subjectthree/bgcd.mp4
         */

        private double size;
        private int subject;
        private String name;
        private String photo;
        private int id;
        private String time;
        private String detail;
        private int isHot;
        private String url;

        public void setSize(double size) {
            this.size = size;
        }

        public void setSubject(int subject) {
            this.subject = subject;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public double getSize() {
            return size;
        }

        public int getSubject() {
            return subject;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public int getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public String getDetail() {
            return detail;
        }

        public int getIsHot() {
            return isHot;
        }

        public String getUrl() {
            return url;
        }
    }
}