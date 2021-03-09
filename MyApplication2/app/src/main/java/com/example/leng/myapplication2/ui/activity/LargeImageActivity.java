package com.example.leng.myapplication2.ui.activity;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.LargeImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LargeImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);

        LargeImageView largeimage = findViewById(R.id.largeimage);

        try {
            InputStream is = getAssets().open("accvv.png");
            largeimage.setInputStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL("https://aegis-newspaper.obs.cn-north-1.myhuaweicloud.com/60b7a8b391bdfbf8ff2547f04bf39a21_60b7a8b391bdfbf8ff2547f04bf39a21/test/png/1614650650_%E4%BA%BA%E6%B0%91%E6%B3%95%E9%99%A2%E4%B8%80%E7%AB%99%E5%BC%8F.png");
//
//                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//
////                    BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
////                    // Grab the bounds for the scene dimensions
////                    tmpOptions.inJustDecodeBounds = true;
////                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, tmpOptions);
////                    int mImageWidth = tmpOptions.outWidth;
////                    int mImageHeight = tmpOptions.outHeight;
////
////                    BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(in, false);
//
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[1024];
//                    int len;
//                    while ((len = in.read(buffer)) > -1 ) {
//                        baos.write(buffer, 0, len);
//                    }
//                    baos.flush();
//
//                    InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
//
//                    largeimage.setInputStream(stream1);
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}