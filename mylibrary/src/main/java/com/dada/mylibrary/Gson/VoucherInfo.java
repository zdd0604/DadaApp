package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * Created by zhangdongdong on 2015/10/9.
 */
public class VoucherInfo {


    /**
     * total : 4
     * success : true
     * rows : [{"money":400,"getTime":"2015-08-04","id":18,"state":2,"endTime":"2015-12-04"},{"money":500,"getTime":"2015-07-05","id":19,"state":3,"endTime":"2015-11-04"},{"money":200,"getTime":"2015-08-01","id":16,"state":3,"endTime":"2015-10-02"},{"money":300,"getTime":"2015-09-01","id":17,"state":1,"endTime":"2015-10-02"}]
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
         * money : 400.0
         * getTime : 2015-08-04
         * id : 18
         * state : 2
         * endTime : 2015-12-04
         */

        private double money;
        private String getTime;
        private int id;
        private int state;
        private String endTime;

        public void setMoney(double money) {
            this.money = money;
        }

        public void setGetTime(String getTime) {
            this.getTime = getTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getMoney() {
            return money;
        }

        public String getGetTime() {
            return getTime;
        }

        public int getId() {
            return id;
        }

        public int getState() {
            return state;
        }

        public String getEndTime() {
            return endTime;
        }
    }
}
