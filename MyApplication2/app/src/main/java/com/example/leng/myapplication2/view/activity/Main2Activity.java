package com.example.leng.myapplication2.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.view.myView.MyView5;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private MyView5 myView5;

    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myView5 = (MyView5)findViewById(R.id.myview5);

        mData = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            mData.add("item" + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,mData);
        myView5.setAdapter(adapter);
    }
}
