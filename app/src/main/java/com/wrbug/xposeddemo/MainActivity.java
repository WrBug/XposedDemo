package com.wrbug.xposeddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Xposed", "before setcontent");
        setContentView(R.layout.activity_main);
        Log.i("Xposed", "after setcontent");
        textView = (TextView) findViewById(R.id.textview);
        textView.setText("WrBug");

        Log.i("Xposed", "before inflate");
        getLayoutInflater().inflate(R.layout.view_demo, null);
        Log.i("Xposed", "after inflate");
    }
}
