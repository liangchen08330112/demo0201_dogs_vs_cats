package com.example.demo0301_flowers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Flower_Forest.R;

public class Picture extends AppCompatActivity {
    private ListView listView;
    //需要适配的数据数组
    private String []names={"玫瑰","向日葵","郁金香","蒲公英","UC浏览器","小雏菊"};
    private int[]icons={R.drawable.rose,R.drawable.sunflower,R.drawable.tulips,R.drawable.dandelion,R.drawable.daisy};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        listView=findViewById(R.id.listview);
        MyAdapter myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=View.inflate(Picture.this,R.layout.item,null);
            TextView textView=view.findViewById(R.id.textView);
            textView.setText(names[position]);
            ImageView imageView=view.findViewById(R.id.imageView);
            imageView.setImageResource(icons[position]);

            return view;
        }
    }
}