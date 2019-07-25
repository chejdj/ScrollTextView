package com.chejdj.scrolltextview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zhuyangyang on 2019-07-25
 */
public class MainActivity extends AppCompatActivity {
    ScrollTextView scrollTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollTextView = findViewById(R.id.scrollView);
        //handler.sendEmptyMessageDelayed(1, 5000);
    }

    @SuppressWarnings("HandlerLeak") Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String hello = "hello" + Math.random() * 10;
            scrollTextView.setText(hello);
            handler.sendEmptyMessageDelayed(1, 5000);
        }
    };
}
