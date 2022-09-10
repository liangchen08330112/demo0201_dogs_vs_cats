package com.example.demo0301_flowers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Flower_Forest.R;
import com.example.demo0301_flowers.data.SaveUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_username,editText_password;
    private TextView textView_register;
    private Button button_login;
    private CheckBox checkBox_rememberPassword;

    int requestCode = -1;
    public static final int REGISTER_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (requestCode!=REGISTER_CODE){
            readSP();
        }
    }

    private void initView() {
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);
        textView_register = (TextView) findViewById(R.id.textView_register);
        button_login = (Button) findViewById(R.id.button_login);
        checkBox_rememberPassword = (CheckBox) findViewById(R.id.checkBox_rememberPassword);

        button_login.setOnClickListener(this);
        textView_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = editText_username.getText().toString().trim();
        String pswd = editText_password.getText().toString().trim();
        switch (v.getId()) {
            case R.id.textView_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.button_login:
                SaveUser su = new SaveUser(this);
                if (name.equals("") || pswd.equals("")) {
                    Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String condition = "用户不存在";

                    Cursor cursor = su.getDatabase().rawQuery("select name,password from users where name=?", new String[]{name});
                    while (cursor.moveToNext()) {
                        condition = cursor.getString(1);
                    }

                    cursor.close();//关门游标
                    if (condition.equals("用户不存在")) {
                        Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (condition.equals(pswd)) {
                            writeSP(name,pswd);
                            startActivity(new Intent(this, HomeActivity.class));
                            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }
    private void writeSP(String name,String pswd) {
        SharedPreferences sp = getSharedPreferences("UserRecord",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //如果选择了记住密码，用户名和密码都会保存，并且在以后打开时自动填充
        if(checkBox_rememberPassword.isChecked()){
            editor.putBoolean("remember",true);
            editor.putString("username",name);
            editor.putString("password",pswd);
        }else {
            editor.putBoolean("remember",false);
        }
        editor.commit();
    }
    private void readSP() {
        SharedPreferences sp = getSharedPreferences("UserRecord",MODE_PRIVATE);
        //如果选择了记住密码，用户名和密码都会保存，并且在以后打开时自动填充
        if(sp.getBoolean("remember",false)){
            String username = sp.getString("username","");
            String password = sp.getString("password","");
            editText_username.setText(username);
            editText_password.setText(password);
            checkBox_rememberPassword.setChecked(true);
            //取消记住密码，用户名和密码不会自动填充。
        }else {
            editText_username.setText("");
            editText_password.setText("");
            checkBox_rememberPassword.setChecked(false);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.requestCode=-1;
        if (resultCode==RESULT_OK){
            if (data!=null){
                editText_username.setText(data.getStringExtra("name"));
                editText_password.setText(data.getStringExtra("password"));
                this.requestCode = requestCode;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}