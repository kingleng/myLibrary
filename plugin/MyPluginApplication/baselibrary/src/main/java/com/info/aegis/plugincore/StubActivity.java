package com.info.aegis.plugincore;

import android.app.Activity;
import android.os.Bundle;

import com.info.aegis.baselibrary.R;

public class StubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stub_layout);
    }
}
