package com.engineer.aptlite;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.SparseArrayCompat;

import com.engineer.apt_annotation.BindString;
import com.engineer.apt_annotation.BindView;
import com.engineer.apt_library.BindViewTools;
import com.engineer.apt_library.QRouterApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content)
    @BindString("Hello APT")
    TextView mContent;

    @BindView(R.id.name)
    @BindString("APT Success")
    TextView mName;

    @BindView(R.id.genCode)
    Button mGenCode;
    @BindView(R.id.jump)
    Button mJump;
    @BindView(R.id.jump_1)
    Button mJump1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViewTools.bind(this);


        mGenCode.setOnClickListener(v -> {
                    System.out.println("the value is " + testCode());
                    Toast.makeText(this, testCode(), Toast.LENGTH_SHORT).show();
                }
        );

        mJump.setOnClickListener(v ->
                QRouterApi.go(this, "/activity/second")
        );

        mJump1.setOnClickListener(v ->
                QRouterApi.go(this, "/activity/simple")
        );
    }

    private String testCode() {
        JSONObject json = new JSONObject();
        try {
            json.put("111", "222");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();

    }
}
