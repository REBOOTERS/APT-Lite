package com.engineer.aptlite;

import android.os.Bundle;

import com.engineer.apt_annotation.BindString;
import com.engineer.apt_annotation.BindView;
import com.engineer.apt_annotation.QRouter;
import com.engineer.apt_library.BindViewTools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

@QRouter("/activity/second")
public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.text)
    @BindString("Second Activity")
    TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        BindViewTools.bind(this);

        initOther();

    }

    // <editor-fold defaultstate="collapsed" desc="init other">
    private void initOther() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    // </editor-fold>

}
