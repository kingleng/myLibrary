package com.example.leng.myapplication.view.activity;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.leng.myapplication.R;
import com.example.leng.myapplication.view.myView.AnimXuanli;

public class Main6Activity extends Activity {

    private AnimXuanli animXuanli;
    private SeekBar seekbar1;
    private SeekBar seekbar2;
    private SeekBar seekbar3;
    private SeekBar seekbar4;

    int value1;
    int value2;
    int value3;
    int value4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        initView();

        Path path1 = new Path();
        path1.moveTo(310, 0);
        path1.lineTo(310, 400);
        path1.lineTo(210, 500);
        path1.lineTo(210, 600);
        path1.lineTo(310, 700);
        path1.lineTo(310, 1280);

        animXuanli.setPath(path1);

        animXuanli.setLineWidth(5);


    }

    private void initView() {
        animXuanli = (AnimXuanli) findViewById(R.id.anim_xuanli);
        seekbar1 = (SeekBar) findViewById(R.id.seekbar1);
        seekbar2 = (SeekBar) findViewById(R.id.seekbar2);
        seekbar3 = (SeekBar) findViewById(R.id.seekbar3);
        seekbar4 = (SeekBar) findViewById(R.id.seekbar4);

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value1 = progress;
                setAnim(value1,value2,value3,value4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value2 = progress;
                setAnim(value1,value2,value3,value4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value3 = progress;
                setAnim(value1,value2,value3,value4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value4 = progress;
                setAnim(value1,value2,value3,value4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setAnim(int value1, int value2, int value3, int value4){
        animXuanli.setDarkLineProgress(value1/100f, value2/100f);
        animXuanli.setLightLineProgress(value3/100f, value4/100f);
    }
}
