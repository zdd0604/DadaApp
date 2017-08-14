package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * Created by wpf on 8-18-0018.
 */
public class Zone {

    /**
     * ret : 0
     * SFBM : 01
     * DQData : [{"DQMC":"长安区","DQBM":"0101"},{"DQMC":"海淀区","DQBM":"0102"}]
     * message : 成功!
     */
    private String ret;
    private String SFBM;
    private List<DQDataEntity> DQData;
    private String message;

    public void setRet(String ret) {
        this.ret = ret;
    }

    public void setSFBM(String SFBM) {
        this.SFBM = SFBM;
    }

    public void setDQData(List<DQDataEntity> DQData) {
        this.DQData = DQData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRet() {
        return ret;
    }

    public String getSFBM() {
        return SFBM;
    }

    public List<DQDataEntity> getDQData() {
        return DQData;
    }

    public String getMessage() {
        return message;
    }

    public static class DQDataEntity {
        /**
         * DQMC : 长安区
         * DQBM : 0101
         */
        private String DQMC;
        private String DQBM;

        public void setDQMC(String DQMC) {
            this.DQMC = DQMC;
        }

        public void setDQBM(String DQBM) {
            this.DQBM = DQBM;
        }

        public String getDQMC() {
            return DQMC;
        }

        public String getDQBM() {
            return DQBM;
        }
    }
}
