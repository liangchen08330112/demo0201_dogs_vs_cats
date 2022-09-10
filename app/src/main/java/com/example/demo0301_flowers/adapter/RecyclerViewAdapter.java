package com.example.demo0301_flowers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Flower_Forest.R;
import com.example.demo0301_flowers.data.RecyclerViewBean;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<RecyclerViewBean> beans;

    public RecyclerViewAdapter(Context context, ArrayList<RecyclerViewBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item,null);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                RecyclerViewBean bean = beans.get(position);
                Toast.makeText(context, bean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewBean bean = beans.get(position);
        holder.imageView.setImageResource(bean.getImg());
        holder.textView.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.imageView = rootView.findViewById(R.id.imageView);
            this.textView = rootView.findViewById(R.id.textView);
        }
    }
}
