package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.Activity;
import android.content.SharedPreferences;
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
 * 设置密码界面
 */
public class SettingActivity extends Activity implements View.OnClickListener{
    public static boolean NEED_INPUT_PASSWORD=false;
    private EditText  et_password;
    private EditText  et_comfirm_passwprd;
    private Button button_okay;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordactivity);
        initViews();
        initEvents();
    }

    @Override
    public void onClick(View v) {
        String password=et_password.getText().toString().trim();
        String comfirm_password=et_comfirm_passwprd.getText().toString().trim();
        if(TextUtils.isEmpty(password)||TextUtils.isEmpty(comfirm_password)){
            Toast.makeText(SettingActivity.this,"请输入您的密码",Toast.LENGTH_SHORT).show();
        }else if(!password.equals(comfirm_password)){
            Toast.makeText(SettingActivity.this,"您两次的密码不一致",Toast.LENGTH_SHORT).show();
        }else{
            //将密码保存在sharedprefernce
            sp=getSharedPreferences("info.txt",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("password",password);
            editor.commit();
            MyConstants.PASSWORD_RIGHT=1;
            MyConstants.NEED_TO_LOGGING=true;
            finish();
        }
    }

    public void initEvents(){
        button_okay.setOnClickListener(this);
    }
    public void initViews(){
        et_password=(EditText)findViewById(R.id.et_password);
        et_comfirm_passwprd=(EditText)findViewById(R.id.et_confirm_password);
        button_okay=(Button)findViewById(R.id.button_okay);
    }
}
