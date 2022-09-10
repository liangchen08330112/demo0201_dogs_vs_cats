package com.example.demo0301_flowers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.Flower_Forest.R;
import com.example.demo0301_flowers.adapter.RecyclerViewAdapter;
import com.example.demo0301_flowers.data.RecyclerViewBean;

import java.util.ArrayList;

public class MyCenterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButton_back;
    private Button button_about,button_logout;
    private RecyclerView recyclerView;

    int[] imgs = {R.drawable.rose,R.drawable.sunflower,R.drawable.tulips,R.drawable.yueji};
    String[] names = {"玫瑰","向日葵","郁金香","月季"};

    ArrayList<RecyclerViewBean> beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);

        initView();
        
        initData();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,beans);
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);
    }

    private void initData() {
        beans = new ArrayList<>();
        RecyclerViewBean bean;
        for (int i = 0;i<imgs.length;i++){
            bean = new RecyclerViewBean(imgs[i],names[i]);
            beans.add(bean);
        }
    }

    private void initView() {
        imageButton_back = findViewById(R.id.imageButton_back);
        button_about = findViewById(R.id.button_about);
        button_logout = findViewById(R.id.button_logout);
        recyclerView = findViewById(R.id.recyclerView);
        
        button_about.setOnClickListener(this);
        button_logout.setOnClickListener(this);
        imageButton_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("关于花林").setMessage("花林版本V2.0\n有任何使用问题欢迎向客服留言。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.button_logout:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("提示").setMessage("您确定要退出登录吗？").setCancelable(true);
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MyCenterActivity.this,LoginActivity.class));
                    }
                });
                builder1.setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
            case R.id.imageButton_back:
                startActivity(new Intent(this,HomeActivity.class));
                break;
            default:
                break;
        }
    }
}