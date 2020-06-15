package com.example.authenticate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.protobuf.Internal;

public class SplashActivity extends Activity {

    private static String TAG = SplashActivity.class.getName();
    private static long MAX_SPLASH_TIME = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent it = new Intent(getBaseContext(),MainActivity.class);
                startActivity(it);
                finish();
            }
        }.start();
    }
}
