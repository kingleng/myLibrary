package com.example.leng.myapplication.model.bean;


import java.util.List;

/**
 * Created by 17092234 on 2018/4/16.
 */

public class AutoBean {


    public List<LiveSquareIiListEntity> liveSquareIiList;
    public BabyInfoEntity babyInfo;
    public GuideInfoEntity guideInfo;

    public static class LiveSquareIiListEntity {
        public String itemCode;
        public String createTime;
        public String type;
        public String cityCode;
        public String cityName;
        public String storeCode;
        public String storeName;
        public String showLevel;
        public String belongCategory;
        public String belongFunction;
        public String belongPage;
        public String jumpUrl;
        public String itemImg;
        public String itemName;
        public String itemId;
    }

    public static class BabyInfoEntity {
        public String url;
        public String cityCode;
        public String storeCode;
        public String storeName;
        public String businessCode;
        public String guidePhoto;
        public String guideName;
        public String guideId;
    }

    public static class GuideInfoEntity {
        public String url;
        public String cityCode;
        public String storeCode;
        public String storeName;
        public String businessCode;
        public String guidePhoto;
        public String guideName;
        public String guideId;
    }
}
