package com.dada.mylibrary.Gson;

import java.util.List;

/**
 * Created by zhangdongdong on 2015/9/24.
 */
public class DrivingSchool {


    /**
     * school : {"coachName":"zhangsan","mobile":"15101088501","telephone":"0311-86133584","id":1,"schoolName":"圣安驾校","type":1}
     * appraise : [{"level":4,"subject":0},{"level":3,"subject":1}]
     * success : true
     * dada_tel : 01082331865
     * order : {"coachName":"zhangsan","licenseType":"c1","money":2500,"school":1,"car":"桑塔纳","classes":1,"className":"普通班","id":163,"schoolName":"圣安驾校"}
     */

    private SchoolEntity school;
    private boolean success;
    private String dada_tel;
    private OrderEntity order;
    private List<AppraiseEntity> appraise;

    public void setSchool(SchoolEntity school) {
        this.school = school;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setDada_tel(String dada_tel) {
        this.dada_tel = dada_tel;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public void setAppraise(List<AppraiseEntity> appraise) {
        this.appraise = appraise;
    }

    public SchoolEntity getSchool() {
        return school;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getDada_tel() {
        return dada_tel;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public List<AppraiseEntity> getAppraise() {
        return appraise;
    }

    public static class SchoolEntity {
        /**
         * coachName : zhangsan
         * mobile : 15101088501
         * telephone : 0311-86133584
         * id : 1
         * schoolName : 圣安驾校
         * type : 1
         */

        private String coachName;
        private String mobile;
        private String telephone;
        private int id;
        private String schoolName;
        private int type;

        public void setCoachName(String coachName) {
            this.coachName = coachName;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCoachName() {
            return coachName;
        }

        public String getMobile() {
            return mobile;
        }

        public String getTelephone() {
            return telephone;
        }

        public int getId() {
            return id;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public int getType() {
            return type;
        }
    }

    public static class OrderEntity {
        /**
         * coachName : zhangsan
         * licenseType : c1
         * money : 2500.0
         * school : 1
         * car : 桑塔纳
         * classes : 1
         * className : 普通班
         * id : 163
         * schoolName : 圣安驾校
         */

        private String coachName;
        private String licenseType;
        private double money;
        private int school;
        private String car;
        private int classes;
        private String className;
        private int id;
        private String schoolName;

        public void setCoachName(String coachName) {
            this.coachName = coachName;
        }

        public void setLicenseType(String licenseType) {
            this.licenseType = licenseType;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public void setSchool(int school) {
            this.school = school;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public void setClasses(int classes) {
            this.classes = classes;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getCoachName() {
            return coachName;
        }

        public String getLicenseType() {
            return licenseType;
        }

        public double getMoney() {
            return money;
        }

        public int getSchool() {
            return school;
        }

        public String getCar() {
            return car;
        }

        public int getClasses() {
            return classes;
        }

        public String getClassName() {
            return className;
        }

        public int getId() {
            return id;
        }

        public String getSchoolName() {
            return schoolName;
        }
    }

    public static class AppraiseEntity {
        /**
         * level : 4
         * subject : 0
         */

        private int level;
        private int subject;

        public void setLevel(int level) {
            this.level = level;
        }

        public void setSubject(int subject) {
            this.subject = subject;
        }

        public int getLevel() {
            return level;
        }

        public int getSubject() {
            return subject;
        }

        @Override
        public String toString() {
            return "{" +"level=" + level +", subject=" + subject +'}';
        }
    }
}
