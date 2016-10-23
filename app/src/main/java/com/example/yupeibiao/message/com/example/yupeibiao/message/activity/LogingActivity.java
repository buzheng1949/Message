package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;

/**
 * Created by yupeibiao on 16/4/13.
 * 登陆界面
 */
public class LogingActivity extends Activity {
    private String password;
    private EditText et_your_password;
    private Button button_yes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggingactivity);
        initViews();
        initEvents();
    }
    public void initEvents(){
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_waitcheck=et_your_password.getText().toString().trim();
                if(TextUtils.isEmpty(password_waitcheck)){
                    Toast.makeText(LogingActivity.this, "输入的密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(!password_waitcheck.equals(password)){
                    Toast.makeText(LogingActivity.this, "密码有误，请重新输入", Toast.LENGTH_SHORT).show();
                }else{
                    MyConstants.PASSWORD_RIGHT=1;
                    Intent intent=new Intent(LogingActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
    public void initViews(){
        password= getSharedPreferences("info.txt",MODE_PRIVATE).getString("password",null);
        button_yes=(Button)findViewById(R.id.button_right);
        et_your_password=(EditText)findViewById(R.id.et_your_password);
    }
}
