//package com.example.leng.myapplication.view.activity;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.example.leng.myapplication.R;
//import com.jph.takephoto.app.TakePhotoActivity;
//import com.jph.takephoto.model.TResult;
//
//public class PicSelectActivity extends TakePhotoActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pic_select);
//    }
//
//    public void onClick(View view) {
//        getTakePhoto();
//    }
//
//    @Override
//    public void takeCancel() {
//        super.takeCancel();
//    }
//
//    @Override
//    public void takeFail(TResult result, String msg) {
//        super.takeFail(result, msg);
//    }
//
//    @Override
//    public void takeSuccess(TResult result) {
//        super.takeSuccess(result);
//        result.getImages();
//    }
//
//
//
//}
