package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * Created by wpf on 8-11-0011.
 */
public class QuestionBank {

    private String ret;

    private String message;

    private List<TKListDataEntity> TKListData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public void setTKListData(List<TKListDataEntity> TKListData) {
        this.TKListData = TKListData;
    }

    public List<TKListDataEntity> getTKListData() {
        return TKListData;
    }

    public static class TKListDataEntity implements Comparable{
        /**
         * stid : 1845
         * stxh : 10001
         * bh : 1
         * stlx : 101
         * stsx : 1
         * sttx : 2
         * stnr : 驾驶机动车在道路上违反道路交通安全法的行为，属于什么行为？
         * xzdaa : 违章行为
         * xzdab : 违法行为
         * xzdac : 过失行为
         * xzdad : 违规行为
         * txlj :
         * stda : B
         * stbj : 1
         * sycx : A1A2A3B1B2C1C2C3C4C5MNP
         * stfl : 01
         * tklx : 1
         * ksyy : ABFUD
         * zplx : 0
         */

        private String stid;
        private String stxh;
        private String bh;
        private String stlx;
        private String stsx;
        private String sttx;
        private String stnr;
        private String xzdaa;
        private String xzdab;
        private String xzdac;
        private String xzdad;
        private String txlj;
        private String stda;
        private String stbj;
        private String sycx;
        private String stfl;
        private String tklx;
        private String ksyy;
        private String zplx;
        private String btjs;

        public void setStid(String stid) {
            this.stid = stid;
        }

        public void setStxh(String stxh) {
            this.stxh = stxh;
        }

        public void setBh(String bh) {
            this.bh = bh;
        }

        public void setStlx(String stlx) {
            this.stlx = stlx;
        }

        public void setStsx(String stsx) {
            this.stsx = stsx;
        }

        public void setSttx(String sttx) {
            this.sttx = sttx;
        }

        public void setStnr(String stnr) {
            this.stnr = stnr;
        }

        public void setXzdaa(String xzdaa) {
            this.xzdaa = xzdaa;
        }

        public void setXzdab(String xzdab) {
            this.xzdab = xzdab;
        }

        public void setXzdac(String xzdac) {
            this.xzdac = xzdac;
        }

        public void setXzdad(String xzdad) {
            this.xzdad = xzdad;
        }

        public void setTxlj(String txlj) {
            this.txlj = txlj;
        }

        public void setStda(String stda) {
            this.stda = stda;
        }

        public void setStbj(String stbj) {
            this.stbj = stbj;
        }

        public void setSycx(String sycx) {
            this.sycx = sycx;
        }

        public void setStfl(String stfl) {
            this.stfl = stfl;
        }

        public void setTklx(String tklx) {
            this.tklx = tklx;
        }

        public void setKsyy(String ksyy) {
            this.ksyy = ksyy;
        }

        public void setZplx(String zplx) {
            this.zplx = zplx;
        }

        public String getStid() {
            return stid;
        }

        public String getStxh() {
            return stxh;
        }

        public String getBh() {
            return bh;
        }

        public String getStlx() {
            return stlx;
        }

        public String getStsx() {
            return stsx;
        }

        public String getSttx() {
            return sttx;
        }

        public String getStnr() {
            return stnr;
        }

        public String getXzdaa() {
            return xzdaa;
        }

        public String getXzdab() {
            return xzdab;
        }

        public String getXzdac() {
            return xzdac;
        }

        public String getXzdad() {
            return xzdad;
        }

        public String getTxlj() {
            return txlj;
        }

        public String getStda() {
            return stda;
        }

        public String getStbj() {
            return stbj;
        }

        public String getSycx() {
            return sycx;
        }

        public String getStfl() {
            return stfl;
        }

        public String getTklx() {
            return tklx;
        }

        public String getKsyy() {
            return ksyy;
        }

        public String getZplx() {
            return zplx;
        }

        public String[] getDatas() {
            return new String[] {
                    getBh(), getStxh(), getStda(), getStid(), getSycx(), getStsx(),
                    getXzdab(), getXzdaa(), getStfl(), getSttx(), getStnr(), getTxlj(),
                    getTklx(), getXzdad(), getXzdac(), getKsyy(), getStlx(),
                    getStbj(), getZplx()};
        }

        public void setDatas(String[] strings) {
            int i = 0;
            setBh(strings[i++]);
            setStxh(strings[i++]);
            setStda(strings[i++]);
            setStid(strings[i++]);
            setSycx(strings[i++]);
            setStsx(strings[i++]);
            setXzdab(strings[i++]);
            setXzdaa(strings[i++]);
            setStfl(strings[i++]);
            setSttx(strings[i++]);
            setStnr(strings[i++]);
            setTxlj(strings[i++]);
            setTklx(strings[i++]);
            setXzdad(strings[i++]);
            setXzdac(strings[i++]);
            setKsyy(strings[i++]);
            setStlx(strings[i++]);
            setStbj(strings[i++]);
            setZplx(strings[i]);
        }

        @Override
        public int compareTo(Object another) {
            return this.sttx.compareTo(((TKListDataEntity) another).getSttx());
        }
    }
}
