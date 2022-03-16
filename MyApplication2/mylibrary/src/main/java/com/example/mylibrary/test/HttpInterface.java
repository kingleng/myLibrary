package com.example.mylibrary.test;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HttpInterface {
    @FormUrlEncoded
    @POST("/ModuleDefaultCompany/RentManage/SearchRentNo/")
    Observable<RoomResult> search(@Field("CertNo") String idNo);
}