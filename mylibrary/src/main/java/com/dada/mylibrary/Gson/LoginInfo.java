package com.dada.mylibrary.Gson;

/**
 * Created by zhangdongdong on 2015/9/24.
 */
public class LoginInfo {
    /**
     * success : true
     * entity : {"gender":0,"identityId":"210904199103282026","latitude":"39.862451","name":"王丹","mobile":"13001239050","photo":"/photo/user/288.png","schoolName":"圣安驾校","longitude":"116.528163"}
     * url : http://dadaxueche2015.oss-cn-beijing.aliyuncs.com/
     */

    private boolean success;
    private EntityEntity entity;
    private String url;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setEntity(EntityEntity entity) {
        this.entity = entity;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getSuccess() {
        return success;
    }

    public EntityEntity getEntity() {
        return entity;
    }

    public String getUrl() {
        return url;
    }

    public static class EntityEntity {
        /**
         * gender : 0
         * identityId : 210904199103282026
         * latitude : 39.862451
         * name : 王丹
         * mobile : 13001239050
         * photo : /photo/user/288.png
         * schoolName : 圣安驾校
         * longitude : 116.528163
         */

        private int gender;
        private String identityId;
        private String latitude;
        private String name;
        private String mobile;
        private String photo;
        private String schoolName;
        private String longitude;

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setIdentityId(String identityId) {
            this.identityId = identityId;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public int getGender() {
            return gender;
        }

        public String getIdentityId() {
            return identityId;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getName() {
            return name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getPhoto() {
            return photo;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public String getLongitude() {
            return longitude;
        }
    }
}
