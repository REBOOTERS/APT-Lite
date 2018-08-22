package com.engineer.aptlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.engineer.apt_annotation.BindView;
import com.engineer.apt_library.BindViewTools;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content)
    TextView mContext;
    @BindView(R.id.name)
    TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViewTools.bind(this);
        mContext.setText("Hello APT");
        mName.setText("APT Success");
    }
}
