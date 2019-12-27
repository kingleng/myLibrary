package com.example.leng.myapplication2.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.ImageMapView;

import java.util.ArrayList;
import java.util.List;

public class ImageMapActivity extends AppCompatActivity {

    ImageMapView imageMapView;

    private List<ImageMapView.ItemType> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_map);

        imageMapView = findViewById(R.id.imagemapview);

        initData();
    }

    private void initData(){
        items.clear();
        ImageMapView.ItemType itemType = new ImageMapView.ItemType("poly", new int[]{344,56,344,53,344,8,181,8,181,53,181,56,181,90,239,90,239,132,344,132}, new ImageMapView.ItemListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "11", Toast.LENGTH_SHORT).show();
            }
        });
        items.add(itemType);

        ImageMapView.ItemType itemType2 = new ImageMapView.ItemType("poly", new int[]{6,103,234,103,234,203,6,203}, new ImageMapView.ItemListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "22", Toast.LENGTH_SHORT).show();
            }
        });
        items.add(itemType2);

        ImageMapView.ItemType itemType3 = new ImageMapView.ItemType("poly", new int[]{239,141,344,141,344,254,239,254}, new ImageMapView.ItemListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "33", Toast.LENGTH_SHORT).show();
            }
        });
        items.add(itemType3);

        ImageMapView.ItemType itemType4 = new ImageMapView.ItemType("poly", new int[]{65,212,234,212,234,318,65,318}, new ImageMapView.ItemListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "44", Toast.LENGTH_SHORT).show();
            }
        });
        items.add(itemType4);

        ImageMapView.ItemType itemType5 = new ImageMapView.ItemType("poly", new int[]{9,212,234,212,234,318,9,318}, new ImageMapView.ItemListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "55", Toast.LENGTH_SHORT).show();
            }
        });
        items.add(itemType5);

        ImageMapView.ItemType itemType6 = new ImageMapView.ItemType("poly", new int[]{239,263,344,263,344,371,239,371}, new ImageMapView.ItemListener() {
            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(), "66", Toast.LENGTH_SHORT).show();
            }
        });
        items.add(itemType6);

        imageMapView.setImageResource(R.drawable.pager_bg);
        imageMapView.setItems(items);
    }
}
