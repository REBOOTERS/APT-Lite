package com.engineer.aptlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.engineer.apt_annotation.QRouter;

@QRouter("/activity/simple")
public class SimpleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
    }
}
