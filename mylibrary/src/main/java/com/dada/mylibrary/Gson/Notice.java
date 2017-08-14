package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * Created by wpf on 9-30-0030.
 */
public class Notice {

    /**
     * total : 2
     * success : true
     * rows : [{"content":"公告：已更新为公安部统一颁布的2015最新题库！"},{"content":"同时在线参加模拟考的人数 100 人"}]
     */

    private int total;
    private String success;
    private List<RowsEntity> rows;

    public void setTotal(int total) {
        this.total = total;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public String getSuccess() {
        return success;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * content : 公告：已更新为公安部统一颁布的2015最新题库！
         */

        private String content;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
