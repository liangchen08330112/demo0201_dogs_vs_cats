package com.example.demo0301_flowers.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SaveUser {
    SQLiteDatabase database;

    public SaveUser(Context context){
        database = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().toString()+"/users.db",null);
        database.execSQL("create table if not exists users(name varchar(12),password varchar(16),primary key(name))");
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
