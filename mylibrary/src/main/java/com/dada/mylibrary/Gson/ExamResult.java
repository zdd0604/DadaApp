package com.dada.mylibrary.Gson;

import com.dada.mylibrary.Util.CEncrypeClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wpf on 8-26-0026.
 */
public class ExamResult {

    /**
     * RegID : 9
     * kscx : C1
     * TKLX : 1
     * BeginDateTime : 2015-08-25T11:18:37.1500115+08:00
     * EndDateTime : 2015-08-25T11:48:37.1500115+08:00
     * KSTS : 100
     * WCTS : 99
     * KSCJ : 95
     * xyw : xyw
     * STResults : [{"stid":1,"bzda":"A","xzda":"C","sj":"2015-08-25T10:58:37.1500115+08:00","evl":"T","xyw":"xyw0"},{"stid":2,"bzda":"A","xzda":"C","sj":"2015-08-25T10:59:07.1500115+08:00","evl":"T","xyw":"xyw1"},{"stid":3,"bzda":"A","xzda":"C","sj":"2015-08-25T10:59:37.1500115+08:00","evl":"T","xyw":"xyw2"},{"stid":4,"bzda":"A","xzda":"C","sj":"2015-08-25T11:00:07.1500115+08:00","evl":"T","xyw":"xyw3"},{"stid":5,"bzda":"A","xzda":"C","sj":"2015-08-25T11:00:37.1500115+08:00","evl":"T","xyw":"xyw4"},{"stid":6,"bzda":"A","xzda":"C","sj":"2015-08-25T11:01:07.1500115+08:00","evl":"T","xyw":"xyw5"},{"stid":7,"bzda":"A","xzda":"C","sj":"2015-08-25T11:01:37.1500115+08:00","evl":"T","xyw":"xyw6"},{"stid":8,"bzda":"A","xzda":"C","sj":"2015-08-25T11:02:07.1500115+08:00","evl":"T","xyw":"xyw7"},{"stid":9,"bzda":"A","xzda":"C","sj":"2015-08-25T11:02:37.1500115+08:00","evl":"T","xyw":"xyw8"},{"stid":10,"bzda":"A","xzda":"C","sj":"2015-08-25T11:03:07.1500115+08:00","evl":"T","xyw":"xyw9"},{"stid":11,"bzda":"A","xzda":"C","sj":"2015-08-25T11:03:37.1500115+08:00","evl":"T","xyw":"xyw10"},{"stid":12,"bzda":"A","xzda":"C","sj":"2015-08-25T11:04:07.1500115+08:00","evl":"T","xyw":"xyw11"},{"stid":13,"bzda":"A","xzda":"C","sj":"2015-08-25T11:04:37.1500115+08:00","evl":"T","xyw":"xyw12"},{"stid":14,"bzda":"A","xzda":"C","sj":"2015-08-25T11:05:07.1500115+08:00","evl":"T","xyw":"xyw13"},{"stid":15,"bzda":"A","xzda":"C","sj":"2015-08-25T11:05:37.1500115+08:00","evl":"T","xyw":"xyw14"},{"stid":16,"bzda":"A","xzda":"C","sj":"2015-08-25T11:06:07.1500115+08:00","evl":"T","xyw":"xyw15"},{"stid":17,"bzda":"A","xzda":"C","sj":"2015-08-25T11:06:37.1500115+08:00","evl":"T","xyw":"xyw16"},{"stid":18,"bzda":"A","xzda":"C","sj":"2015-08-25T11:07:07.1500115+08:00","evl":"T","xyw":"xyw17"},{"stid":19,"bzda":"A","xzda":"C","sj":"2015-08-25T11:07:37.1500115+08:00","evl":"T","xyw":"xyw18"},{"stid":20,"bzda":"A","xzda":"C","sj":"2015-08-25T11:08:07.1500115+08:00","evl":"T","xyw":"xyw19"},{"stid":21,"bzda":"A","xzda":"C","sj":"2015-08-25T11:08:37.1500115+08:00","evl":"T","xyw":"xyw20"},{"stid":22,"bzda":"A","xzda":"C","sj":"2015-08-25T11:09:07.1500115+08:00","evl":"T","xyw":"xyw21"},{"stid":23,"bzda":"A","xzda":"C","sj":"2015-08-25T11:09:37.1500115+08:00","evl":"T","xyw":"xyw22"},{"stid":24,"bzda":"A","xzda":"C","sj":"2015-08-25T11:10:07.1500115+08:00","evl":"T","xyw":"xyw23"},{"stid":25,"bzda":"A","xzda":"C","sj":"2015-08-25T11:10:37.1500115+08:00","evl":"T","xyw":"xyw24"},{"stid":26,"bzda":"A","xzda":"C","sj":"2015-08-25T11:11:07.1500115+08:00","evl":"T","xyw":"xyw25"},{"stid":27,"bzda":"A","xzda":"C","sj":"2015-08-25T11:11:37.1500115+08:00","evl":"T","xyw":"xyw26"},{"stid":28,"bzda":"A","xzda":"C","sj":"2015-08-25T11:12:07.1500115+08:00","evl":"T","xyw":"xyw27"},{"stid":29,"bzda":"A","xzda":"C","sj":"2015-08-25T11:12:37.1500115+08:00","evl":"T","xyw":"xyw28"},{"stid":30,"bzda":"A","xzda":"C","sj":"2015-08-25T11:13:07.1500115+08:00","evl":"T","xyw":"xyw29"},{"stid":31,"bzda":"A","xzda":"C","sj":"2015-08-25T11:13:37.1500115+08:00","evl":"T","xyw":"xyw30"},{"stid":32,"bzda":"A","xzda":"C","sj":"2015-08-25T11:14:07.1500115+08:00","evl":"T","xyw":"xyw31"},{"stid":33,"bzda":"A","xzda":"C","sj":"2015-08-25T11:14:37.1500115+08:00","evl":"T","xyw":"xyw32"},{"stid":34,"bzda":"A","xzda":"C","sj":"2015-08-25T11:15:07.1500115+08:00","evl":"T","xyw":"xyw33"},{"stid":35,"bzda":"A","xzda":"C","sj":"2015-08-25T11:15:37.1500115+08:00","evl":"T","xyw":"xyw34"},{"stid":36,"bzda":"A","xzda":"C","sj":"2015-08-25T11:16:07.1500115+08:00","evl":"T","xyw":"xyw35"},{"stid":37,"bzda":"A","xzda":"C","sj":"2015-08-25T11:16:37.1500115+08:00","evl":"T","xyw":"xyw36"},{"stid":38,"bzda":"A","xzda":"C","sj":"2015-08-25T11:17:07.1500115+08:00","evl":"T","xyw":"xyw37"},{"stid":39,"bzda":"A","xzda":"C","sj":"2015-08-25T11:17:37.1500115+08:00","evl":"T","xyw":"xyw38"},{"stid":40,"bzda":"A","xzda":"C","sj":"2015-08-25T11:18:07.1500115+08:00","evl":"T","xyw":"xyw39"},{"stid":41,"bzda":"A","xzda":"C","sj":"2015-08-25T11:18:37.1500115+08:00","evl":"T","xyw":"xyw40"},{"stid":42,"bzda":"A","xzda":"C","sj":"2015-08-25T11:19:07.1500115+08:00","evl":"T","xyw":"xyw41"},{"stid":43,"bzda":"A","xzda":"C","sj":"2015-08-25T11:19:37.1500115+08:00","evl":"T","xyw":"xyw42"},{"stid":44,"bzda":"A","xzda":"C","sj":"2015-08-25T11:20:07.1500115+08:00","evl":"T","xyw":"xyw43"},{"stid":45,"bzda":"A","xzda":"C","sj":"2015-08-25T11:20:37.1500115+08:00","evl":"T","xyw":"xyw44"},{"stid":46,"bzda":"A","xzda":"C","sj":"2015-08-25T11:21:07.1500115+08:00","evl":"T","xyw":"xyw45"},{"stid":47,"bzda":"A","xzda":"C","sj":"2015-08-25T11:21:37.1500115+08:00","evl":"T","xyw":"xyw46"},{"stid":48,"bzda":"A","xzda":"C","sj":"2015-08-25T11:22:07.1500115+08:00","evl":"T","xyw":"xyw47"},{"stid":49,"bzda":"A","xzda":"C","sj":"2015-08-25T11:22:37.1500115+08:00","evl":"T","xyw":"xyw48"},{"stid":50,"bzda":"A","xzda":"C","sj":"2015-08-25T11:23:07.1500115+08:00","evl":"T","xyw":"xyw49"},{"stid":51,"bzda":"A","xzda":"C","sj":"2015-08-25T11:23:37.1500115+08:00","evl":"T","xyw":"xyw50"},{"stid":52,"bzda":"A","xzda":"C","sj":"2015-08-25T11:24:07.1500115+08:00","evl":"T","xyw":"xyw51"},{"stid":53,"bzda":"A","xzda":"C","sj":"2015-08-25T11:24:37.1500115+08:00","evl":"T","xyw":"xyw52"},{"stid":54,"bzda":"A","xzda":"C","sj":"2015-08-25T11:25:07.1500115+08:00","evl":"T","xyw":"xyw53"},{"stid":55,"bzda":"A","xzda":"C","sj":"2015-08-25T11:25:37.1500115+08:00","evl":"T","xyw":"xyw54"},{"stid":56,"bzda":"A","xzda":"C","sj":"2015-08-25T11:26:07.1500115+08:00","evl":"T","xyw":"xyw55"},{"stid":57,"bzda":"A","xzda":"C","sj":"2015-08-25T11:26:37.1500115+08:00","evl":"T","xyw":"xyw56"},{"stid":58,"bzda":"A","xzda":"C","sj":"2015-08-25T11:27:07.1500115+08:00","evl":"T","xyw":"xyw57"},{"stid":59,"bzda":"A","xzda":"C","sj":"2015-08-25T11:27:37.1500115+08:00","evl":"T","xyw":"xyw58"},{"stid":60,"bzda":"A","xzda":"C","sj":"2015-08-25T11:28:07.1500115+08:00","evl":"T","xyw":"xyw59"},{"stid":61,"bzda":"A","xzda":"C","sj":"2015-08-25T11:28:37.1500115+08:00","evl":"T","xyw":"xyw60"},{"stid":62,"bzda":"A","xzda":"C","sj":"2015-08-25T11:29:07.1500115+08:00","evl":"T","xyw":"xyw61"},{"stid":63,"bzda":"A","xzda":"C","sj":"2015-08-25T11:29:37.1500115+08:00","evl":"T","xyw":"xyw62"},{"stid":64,"bzda":"A","xzda":"C","sj":"2015-08-25T11:30:07.1500115+08:00","evl":"T","xyw":"xyw63"},{"stid":65,"bzda":"A","xzda":"C","sj":"2015-08-25T11:30:37.1500115+08:00","evl":"T","xyw":"xyw64"},{"stid":66,"bzda":"A","xzda":"C","sj":"2015-08-25T11:31:07.1500115+08:00","evl":"T","xyw":"xyw65"},{"stid":67,"bzda":"A","xzda":"C","sj":"2015-08-25T11:31:37.1500115+08:00","evl":"T","xyw":"xyw66"},{"stid":68,"bzda":"A","xzda":"C","sj":"2015-08-25T11:32:07.1500115+08:00","evl":"T","xyw":"xyw67"},{"stid":69,"bzda":"A","xzda":"C","sj":"2015-08-25T11:32:37.1500115+08:00","evl":"T","xyw":"xyw68"},{"stid":70,"bzda":"A","xzda":"C","sj":"2015-08-25T11:33:07.1500115+08:00","evl":"T","xyw":"xyw69"},{"stid":71,"bzda":"A","xzda":"C","sj":"2015-08-25T11:33:37.1500115+08:00","evl":"T","xyw":"xyw70"},{"stid":72,"bzda":"A","xzda":"C","sj":"2015-08-25T11:34:07.1500115+08:00","evl":"T","xyw":"xyw71"},{"stid":73,"bzda":"A","xzda":"C","sj":"2015-08-25T11:34:37.1500115+08:00","evl":"T","xyw":"xyw72"},{"stid":74,"bzda":"A","xzda":"C","sj":"2015-08-25T11:35:07.1500115+08:00","evl":"T","xyw":"xyw73"},{"stid":75,"bzda":"A","xzda":"C","sj":"2015-08-25T11:35:37.1500115+08:00","evl":"T","xyw":"xyw74"},{"stid":76,"bzda":"A","xzda":"C","sj":"2015-08-25T11:36:07.1500115+08:00","evl":"T","xyw":"xyw75"},{"stid":77,"bzda":"A","xzda":"C","sj":"2015-08-25T11:36:37.1500115+08:00","evl":"T","xyw":"xyw76"},{"stid":78,"bzda":"A","xzda":"C","sj":"2015-08-25T11:37:07.1500115+08:00","evl":"T","xyw":"xyw77"},{"stid":79,"bzda":"A","xzda":"C","sj":"2015-08-25T11:37:37.1500115+08:00","evl":"T","xyw":"xyw78"},{"stid":80,"bzda":"A","xzda":"C","sj":"2015-08-25T11:38:07.1500115+08:00","evl":"T","xyw":"xyw79"},{"stid":81,"bzda":"A","xzda":"C","sj":"2015-08-25T11:38:37.1500115+08:00","evl":"T","xyw":"xyw80"},{"stid":82,"bzda":"A","xzda":"C","sj":"2015-08-25T11:39:07.1500115+08:00","evl":"T","xyw":"xyw81"},{"stid":83,"bzda":"A","xzda":"C","sj":"2015-08-25T11:39:37.1500115+08:00","evl":"T","xyw":"xyw82"},{"stid":84,"bzda":"A","xzda":"C","sj":"2015-08-25T11:40:07.1500115+08:00","evl":"T","xyw":"xyw83"},{"stid":85,"bzda":"A","xzda":"C","sj":"2015-08-25T11:40:37.1500115+08:00","evl":"T","xyw":"xyw84"},{"stid":86,"bzda":"A","xzda":"C","sj":"2015-08-25T11:41:07.1500115+08:00","evl":"T","xyw":"xyw85"},{"stid":87,"bzda":"A","xzda":"C","sj":"2015-08-25T11:41:37.1500115+08:00","evl":"T","xyw":"xyw86"},{"stid":88,"bzda":"A","xzda":"C","sj":"2015-08-25T11:42:07.1500115+08:00","evl":"T","xyw":"xyw87"},{"stid":89,"bzda":"A","xzda":"C","sj":"2015-08-25T11:42:37.1500115+08:00","evl":"T","xyw":"xyw88"},{"stid":90,"bzda":"A","xzda":"C","sj":"2015-08-25T11:43:07.1500115+08:00","evl":"T","xyw":"xyw89"},{"stid":91,"bzda":"A","xzda":"C","sj":"2015-08-25T11:43:37.1500115+08:00","evl":"T","xyw":"xyw90"},{"stid":92,"bzda":"A","xzda":"C","sj":"2015-08-25T11:44:07.1500115+08:00","evl":"T","xyw":"xyw91"},{"stid":93,"bzda":"A","xzda":"C","sj":"2015-08-25T11:44:37.1500115+08:00","evl":"T","xyw":"xyw92"},{"stid":94,"bzda":"A","xzda":"C","sj":"2015-08-25T11:45:07.1500115+08:00","evl":"T","xyw":"xyw93"},{"stid":95,"bzda":"A","xzda":"C","sj":"2015-08-25T11:45:37.1500115+08:00","evl":"T","xyw":"xyw94"},{"stid":96,"bzda":"A","xzda":"C","sj":"2015-08-25T11:46:07.1500115+08:00","evl":"T","xyw":"xyw95"},{"stid":97,"bzda":"A","xzda":"C","sj":"2015-08-25T11:46:37.1500115+08:00","evl":"T","xyw":"xyw96"},{"stid":98,"bzda":"A","xzda":"C","sj":"2015-08-25T11:47:07.1500115+08:00","evl":"T","xyw":"xyw97"},{"stid":99,"bzda":"A","xzda":"C","sj":"2015-08-25T11:47:37.1500115+08:00","evl":"T","xyw":"xyw98"},{"stid":100,"bzda":"A","xzda":"C","sj":"2015-08-25T11:48:07.1500115+08:00","evl":"T","xyw":"xyw99"}]
     */

    private int RegID = 0;
    private String kscx = "";
    private String TKLX = "";
    private Date BeginDateTime = new Date(System.currentTimeMillis());
    private Date EndDateTime = new Date(System.currentTimeMillis());
    private int KSTS = 0;
    private int WCTS = 0;
    private int KSCJ = 0;
    private String xyw = "";
    private List<STResultsEntity> STResults = new ArrayList<>();

    public ExamResult() {
        getXyw();
    }

    public void setRegID(int RegID) {
        this.RegID = RegID;
    }

    public void setKscx(String kscx) {
        this.kscx = kscx;
    }

    public void setTKLX(String TKLX) {
        this.TKLX = TKLX;
    }

    public void setBeginDateTime(Date BeginDateTime) {
        this.BeginDateTime = BeginDateTime;
    }

    public void setEndDateTime(Date EndDateTime) {
        this.EndDateTime = EndDateTime;
    }

    public void setKSTS(int KSTS) {
        this.KSTS = KSTS;
    }

    public void setWCTS(int WCTS) {
        this.WCTS = WCTS;
    }

    public void setKSCJ(int KSCJ) {
        this.KSCJ = KSCJ;
    }

    public void setXyw(String xyw) {
        this.xyw = xyw;
    }

    public void setSTResults(List<STResultsEntity> STResults) {
        this.STResults = STResults;
    }

    public int getRegID() {
        return RegID;
    }

    public String getKscx() {
        return kscx;
    }

    public String getTKLX() {
        return TKLX;
    }

    public Date getBeginDateTime() {
        return BeginDateTime;
    }

    public Date getEndDateTime() {
        return EndDateTime;
    }

    public int getKSTS() {
        return KSTS;
    }

    public int getWCTS() {
        return WCTS;
    }

    public int getKSCJ() {
        return KSCJ;
    }

    public String getXyw() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        xyw = CEncrypeClass.Encrypt16(
                RegID + kscx + TKLX +
                        simpleDateFormat.format(BeginDateTime) +
                        simpleDateFormat.format(EndDateTime) +
                        KSTS + WCTS + KSCJ + STResults.size());
        return xyw;
    }

    public List<STResultsEntity> getSTResults() {
        return STResults;
    }

    public static class STResultsEntity {
        /**
         * stid : 1
         * bzda : A
         * xzda : C
         * sj : 2015-08-25T10:58:37.1500115+08:00
         * evl : T
         * xyw : xyw0
         */

        private int stid = 0;
        private String bzda = "";
        private String xzda = "";
        private Date sj = new Date(System.currentTimeMillis());
        private String evl = "";
        private String xyw = "";

        public STResultsEntity() {
            getXyw();
        }

        public void setStid(int stid) {
            this.stid = stid;
        }

        public void setBzda(String bzda) {
            this.bzda = bzda;
        }

        public void setXzda(String xzda) {
            this.xzda = xzda;
        }

        public void setSj(Date sj) {
            this.sj = sj;
        }

        public void setEvl(String evl) {
            this.evl = evl;
        }

        public void setXyw(String xyw) {
            this.xyw = xyw;
        }

        public int getStid() {
            return stid;
        }

        public String getBzda() {
            return bzda;
        }

        public String getXzda() {
            return xzda;
        }

        public Date getSj() {
            return sj;
        }

        public String getEvl() {
            return evl;
        }

        public String getXyw() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            xyw = CEncrypeClass.Encrypt16(stid + bzda + xzda + simpleDateFormat.format(sj) + evl);
            return xyw;
        }
    }
}
