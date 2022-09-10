package com.example.demo0301_flowers.introduction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Flower_Forest.R;
import com.example.demo0301_flowers.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FirstScreen extends AppCompatActivity {

    Handler handler;
    int seconds = 2;
    Runnable runnable;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        seconds--;
                        if (seconds<0){
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,1000,1000);
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FirstScreen.this, LoginActivity.class));
                finish();
            }
        },3000);
    }
}
