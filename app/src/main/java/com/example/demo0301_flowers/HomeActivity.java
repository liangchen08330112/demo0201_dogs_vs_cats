package com.example.demo0301_flowers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.Flower_Forest.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private FloatingActionButton button_camera;
    private ImageButton imageButton_watering;
    private ImageButton imageButton_mycenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        button_camera = (FloatingActionButton) findViewById(R.id.button_camera);
        imageButton_watering = (ImageButton) findViewById(R.id.imageButton_watering);
        imageButton_mycenter = (ImageButton) findViewById(R.id.imageButton_mycenter);
        button_camera.setOnClickListener(this);
        imageButton_watering.setOnClickListener(this);
        imageButton_mycenter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_camera) {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            Toast.makeText(HomeActivity.this, "拍照识别", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.imageButton_mycenter) {
            startActivity(new Intent(HomeActivity.this, MyCenterActivity.class));
            Toast.makeText(HomeActivity.this,"我的",Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.imageButton_watering) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.addCategory("android.intent.category.DEFAULT");
                Uri content_url = Uri.parse("http://www.sict.edu.cn//");
                intent.setData(content_url);
                startActivity(intent);
            }catch (Exception e){

            }

            Toast.makeText(HomeActivity.this,"web",Toast.LENGTH_SHORT).show();
        }
    }
}
