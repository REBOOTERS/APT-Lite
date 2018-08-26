package com.engineer.aptlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.engineer.apt_annotation.BindString;
import com.engineer.apt_annotation.BindView;
import com.engineer.apt_library.BindViewTools;



public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content) @BindString("Hello APT")
    TextView mContent;
    @BindView(R.id.name) @BindString("APT Success")
    TextView mName;
    @BindView(R.id.genCode)
    Button mGenCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViewTools.bind(this);
        mGenCode.setOnClickListener(v -> {

        });
    }
}
