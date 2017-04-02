package com.loubinfeng.www.progressbardemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loubinfeng.www.progressbardemo.view.GeneralCircleMaterialProgressBar;

public class GeneralCircleProgressBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_circle_progress_bar);
        GeneralCircleMaterialProgressBar bar2 = (GeneralCircleMaterialProgressBar)findViewById(R.id.bar2);
        GeneralCircleMaterialProgressBar bar3 = (GeneralCircleMaterialProgressBar)findViewById(R.id.bar3);
        bar2.setColor(Color.GREEN);
        bar3.setColor(Color.YELLOW);
    }
}
