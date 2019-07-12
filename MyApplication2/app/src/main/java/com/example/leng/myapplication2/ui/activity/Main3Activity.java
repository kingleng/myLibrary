package com.example.leng.myapplication2.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.ui.myView.MyView6;

public class Main3Activity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private MyView6 myView6;
    private SeekBar bar1;
    private SeekBar bar2;
    private SeekBar bar3;
    private SeekBar bar4;
    private SeekBar bar5;
    private SeekBar bar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        myView6 = (MyView6)findViewById(R.id.myview6);

        bar1 = (SeekBar)findViewById(R.id.bar1);
        bar2 = (SeekBar)findViewById(R.id.bar2);
        bar3 = (SeekBar)findViewById(R.id.bar3);
        bar4 = (SeekBar)findViewById(R.id.bar4);
        bar5 = (SeekBar)findViewById(R.id.bar5);
        bar6 = (SeekBar)findViewById(R.id.bar6);

        bar1.setProgress(myView6.getValue1());
        bar2.setProgress(myView6.getValue2());
        bar3.setProgress(myView6.getValue3());
        bar4.setProgress(myView6.getValue4());
        bar5.setProgress(myView6.getValue5());
        bar6.setProgress(myView6.getValue6());

        bar1.setOnSeekBarChangeListener(this);
        bar2.setOnSeekBarChangeListener(this);
        bar3.setOnSeekBarChangeListener(this);
        bar4.setOnSeekBarChangeListener(this);
        bar5.setOnSeekBarChangeListener(this);
        bar6.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()){
            case R.id.bar1:
                myView6.setValue1(progress);
                break;
            case R.id.bar2:
                myView6.setValue2(progress);
                break;
            case R.id.bar3:
                myView6.setValue3(progress);
                break;
            case R.id.bar4:
                myView6.setValue4(progress);
                break;
            case R.id.bar5:
                myView6.setValue5(progress);
                break;
            case R.id.bar6:
                myView6.setValue6(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
