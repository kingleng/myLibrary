//package com.example.mylibrary.util;
//
//import android.content.Context;
//import android.widget.TextView;
//
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.geocoder.GeocodeResult;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.amap.api.services.geocoder.RegeocodeQuery;
//import com.amap.api.services.geocoder.RegeocodeResult;
//
///**
// * Created by leng on 2017/3/29.
// */
//
//public class AddressUtils {
//
//    /**
//     * 通过经纬度反编码得到地址
//     * @param mContext
//     * @param latLonPoint
//     * @param addressText
//     * @return
//     */
//    public static String getAddressByLatLonPoint(Context mContext , LatLonPoint latLonPoint, final TextView addressText){
//        GeocodeSearch geocoderSearch = new GeocodeSearch(mContext);
//        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                addressText.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
////                Log.e("getAddressByLatLonPoint","通过经纬度反编码得到地址onRegeocodeSearched=="+regeocodeResult.getRegeocodeAddress().getFormatAddress());
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//            }
//        });
//
//        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
//
//        geocoderSearch.getFromLocationAsyn(query);
//
//        return null;
//    }
//
//
//    /**
//     * 通过经纬度反编码得到 简洁的地址（只包含 省 市 街道）
//     * @param mContext
//     * @param latLonPoint
//     * @param addressText
//     * @return
//     */
//    public static String getSimpleAddressByLatLonPoint(Context mContext , LatLonPoint latLonPoint, final TextView addressText){
//        GeocodeSearch geocoderSearch = new GeocodeSearch(mContext);
//        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                addressText.setText(regeocodeResult.getRegeocodeAddress().getProvince() + regeocodeResult.getRegeocodeAddress().getCity()
//                        + regeocodeResult.getRegeocodeAddress().getDistrict() + regeocodeResult.getRegeocodeAddress().getTownship());
////                Log.e("getSimpleAddress","通过经纬度反编码得到地址onRegeocodeSearched=="+regeocodeResult.getRegeocodeAddress().getProvince() + regeocodeResult.getRegeocodeAddress().getCity()
////                        + regeocodeResult.getRegeocodeAddress().getDistrict() + regeocodeResult.getRegeocodeAddress().getTownship());
//
////                Log.e("addressText","getNeighborhood:"+regeocodeResult.getRegeocodeAddress().getNeighborhood());
////                Log.e("addressText","getFormatAddress:"+regeocodeResult.getRegeocodeAddress().getFormatAddress());
////                Log.e("addressText","getDistrict:"+regeocodeResult.getRegeocodeAddress().getDistrict());
////                Log.e("addressText","getAdCode:"+regeocodeResult.getRegeocodeAddress().getAdCode());
////                Log.e("addressText","getBuilding:"+regeocodeResult.getRegeocodeAddress().getBuilding());
////                Log.e("addressText","getCity:"+regeocodeResult.getRegeocodeAddress().getCity());
////                Log.e("addressText","getTownship:"+regeocodeResult.getRegeocodeAddress().getTownship());
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//            }
//        });
//
//        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
//
//        geocoderSearch.getFromLocationAsyn(query);
//
//        return null;
//    }
//
//    public static String getSimpleAddressByLatLonPoint2(Context mContext , LatLonPoint latLonPoint, final TextView addressText){
//        GeocodeSearch geocoderSearch = new GeocodeSearch(mContext);
//        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                addressText.setText(regeocodeResult.getRegeocodeAddress().getTownship());
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//            }
//        });
//
//        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
//
//        geocoderSearch.getFromLocationAsyn(query);
//
//        return null;
//    }
//
//}
