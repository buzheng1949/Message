package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;
import com.example.yupeibiao.message.com.example.yupeibiao.message.service.MySmsService;

/**
 * 应用程序主入口
 */
public class MainActivity extends TabActivity implements View.OnClickListener{

    private static final String MessageTag="message";
    private static final String FolderTag="folder";
    private static final String GroupTag="group";
    private static final int NOTIFY_MESSAGE_LIST=1;
    private TextView tv_edit;
    private TextView topbar;
    private TabHost tabHost;
    private LinearLayout tabMessage;
    private LinearLayout tabFolder;
    private LinearLayout tabGroup;
    private LinearLayout ll_bottombar;
    private Button button_selectall;
    private Button button_delete;
    private RelativeLayout rl_bottombar;
    private ProgressDialog progressDialog;
    private boolean deleteFlag=false;
    private ImageView chat_all_image;
    private ImageView message_image;
    private ImageView group_image;
    private TextView tv_new_message;
    private static int MESSAGE_STATE=1;
    private static int GROUP_STATE=1;
    private static int FOLDER_STATE=1;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NOTIFY_MESSAGE_LIST:
                    MessageActivity.adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSharedPreferences("info.txt",MODE_PRIVATE).getString("password","")!=""&&MyConstants.PASSWORD_RIGHT==0){
                //进行验证
                Intent intent=new Intent(MainActivity.this,LogingActivity.class);
                startActivity(intent);
                MyConstants.PASSWORD_RIGHT=0;
        }
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this, MySmsService.class);
        startService(intent);
        initViews();
        initEvents();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_new_message:
                Intent intent_newMessage=new Intent(this,NewMessageActivity.class);
                startActivity(intent_newMessage);
                break;
            case(R.id.button_delete):
                if(MessageActivity.clickSet.size()==0){
                    Toast.makeText(MainActivity.this,"请选择您要删除的短信",Toast.LENGTH_LONG).show();
                }else{
                    showAnswerMessageDialog();
                }

                break;
            case(R.id.button_selectall):
                if(button_selectall.getText()=="取消全选"){
                    MessageActivity.clickSet.clear();
                    MessageActivity.adapter.notifyDataSetChanged();
                    button_selectall.setText("全选");
                    break;
                }
                Cursor cursor= MessageActivity.adapter.getCursor();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()){
                    int threadId=cursor.getInt(MessageActivity.INDEX_THREAD_ID);
                    MessageActivity.clickSet.add(threadId);
                }
                MessageActivity.adapter.notifyDataSetChanged();
                button_selectall.setText("取消全选");
                break;
            case(R.id.tv_edit):
                if("编辑".equals(tv_edit.getText().toString())){

                    MessageActivity.isEdit=true;
                    MessageActivity.adapter.notifyDataSetChanged();
                    rl_bottombar.setVisibility(View.GONE);
                    ll_bottombar.setVisibility(View.VISIBLE);
                    tv_edit.setText("取消");
                }else{
                    MessageActivity.isEdit=false;
                    MessageActivity.adapter.notifyDataSetChanged();
                    rl_bottombar.setVisibility(View.VISIBLE);
                    ll_bottombar.setVisibility(View.GONE);
                    tv_edit.setText("编辑");
                    MessageActivity.clickSet.clear();
                    MessageActivity.adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tab_message:
                tv_new_message.setVisibility(View.VISIBLE);
                tv_edit.setVisibility(View.VISIBLE);
                if(MESSAGE_STATE==1){
                    group_image.setBackgroundResource(R.mipmap.group);
                    chat_all_image.setBackgroundResource(R.mipmap.chat_all);
                    message_image.setBackgroundResource(R.mipmap.message_chat_pressed);
                    MESSAGE_STATE=0;
                    GROUP_STATE=1;
                    FOLDER_STATE=1;
                }else{
                    //message_image.setBackgroundResource(R.mipmap.message_chat);
                    MESSAGE_STATE=1;
                }

                Log.i("yupeibiao",tabHost.getCurrentTabTag());
                if(!tabHost.getCurrentTabTag().equals(MessageTag)){
                    tabHost.setCurrentTabByTag(MessageTag);
                    topbar.setText("会话");
                }
                break;
            case R.id.tab_folder:
                tv_edit.setVisibility(View.GONE);
                tv_new_message.setVisibility(View.GONE);
                if(FOLDER_STATE==1){
                    group_image.setBackgroundResource(R.mipmap.group);
                    message_image.setBackgroundResource(R.mipmap.message_chat);
                    chat_all_image.setBackgroundResource(R.mipmap.chat_all_pressed);
                    FOLDER_STATE=0;
                    MESSAGE_STATE=1;
                    GROUP_STATE=1;
                }else{
                    //chat_all_image.setBackgroundResource(R.mipmap.chat_all);
                    FOLDER_STATE=1;
                }


                if(!tabHost.getCurrentTabTag().equals(FolderTag)){
                    tabHost.setCurrentTabByTag(FolderTag);
                    topbar.setText("文件夹");
                }
                break;
            case R.id.tab_group:
                tv_new_message.setVisibility(View.GONE);
                tv_edit.setVisibility(View.GONE);
                if(GROUP_STATE==1){
                    message_image.setBackgroundResource(R.mipmap.message_chat);
                    chat_all_image.setBackgroundResource(R.mipmap.chat_all);
                    group_image.setBackgroundResource(R.mipmap.group_pressed);
                    GROUP_STATE=0;
                    FOLDER_STATE=1;
                    MESSAGE_STATE=1;
                }else{
                   // group_image.setBackgroundResource(R.mipmap.group);
                    GROUP_STATE=1;
                }


                if(!tabHost.getCurrentTabTag().equals(GroupTag)){
                    tabHost.setCurrentTabByTag(GroupTag);
                    topbar.setText("设置");
                }
                break;

        }
    }

    private void showAnswerMessageDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("警报！！！！");
        builder.setMessage("您是否真的忍心删除宝宝么？");
        builder.setIcon(R.mipmap.deleteornot);
        builder.setPositiveButton("残忍删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new MyDeleteMessageThread()).start();
                        deleteFlag=true;
                        showDelete();
            }
        });
        builder.setNegativeButton("温柔呵护", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    class MyDeleteMessageThread implements Runnable{
        @Override
        public void run() {
            for(Integer threadId:MessageActivity.clickSet){
                if(deleteFlag){
                    SystemClock.sleep(1000);
                    deleteMessage(getApplicationContext(), threadId);
                    progressDialog.incrementProgressBy(1);
                }else{
                    break;
                }

            }
            MessageActivity.clickSet.clear();
            handler.sendEmptyMessage(NOTIFY_MESSAGE_LIST);
            //MessageActivity.adapter.notifyDataSetChanged();
            deleteFlag=true;
            progressDialog.dismiss();
        }
    }
    public void deleteMessage(Context context,int threadId){
        context.getContentResolver().delete(MyConstants.URI_SMS, " thread_id = ?", new String[]{"" + threadId});

    }
    private void showDelete(){
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(MessageActivity.clickSet.size());
        progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFlag=false;
            }
        });
        progressDialog.show();
    }
    public void initEvents(){
        tv_new_message.setOnClickListener(this);
        tabFolder.setOnClickListener(this);
        tabMessage.setOnClickListener(this);
        tabGroup.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        button_selectall.setOnClickListener(this);
        group_image.setBackgroundResource(R.mipmap.group);
        chat_all_image.setBackgroundResource(R.mipmap.chat_all);
        message_image.setBackgroundResource(R.mipmap.message_chat_pressed);
        MESSAGE_STATE=0;
        GROUP_STATE=1;
        FOLDER_STATE=1;
    }
    public void initViews(){
        initAllViews();
    }
    public void initAllViews(){
        tabHost=getTabHost();
        addTabsByTabHost(MessageTag,"",R.mipmap.ic_nav_1_normal,MessageActivity.class);
        addTabsByTabHost(FolderTag,"",R.mipmap.ic_nav_3_normal,FolderActivity.class);
        addTabsByTabHost(GroupTag,"",R.mipmap.ic_nav_5_normal,GroupActivity.class);
        tabMessage=(LinearLayout)findViewById(R.id.tab_message);
        tabFolder=(LinearLayout)findViewById(R.id.tab_folder);
        tabGroup=(LinearLayout)findViewById(R.id.tab_group);
        topbar=(TextView)findViewById(R.id.topbar);
        tv_edit=(TextView)findViewById(R.id.tv_edit);
        ll_bottombar=(LinearLayout)findViewById(R.id.ll_bottombar);
        button_delete=(Button)findViewById(R.id. button_delete);
        button_selectall=(Button)findViewById(R.id.button_selectall);
        rl_bottombar=(RelativeLayout)findViewById(R.id.rl_bottombar);
        chat_all_image=(ImageView)findViewById(R.id.chat_all_image);
        message_image=(ImageView)findViewById(R.id.message_image);
        group_image=(ImageView)findViewById(R.id.group_image);
        tv_new_message=(TextView)findViewById(R.id.tv_new_message);
    }
    public void addTabsByTabHost(String tag,String title,int resourceId,Class<?>className){
        TabHost.TabSpec tabSpec=tabHost.newTabSpec(tag);
        tabSpec.setIndicator(title, getResources().getDrawable(resourceId));
        Intent intent=new Intent(this,className);
        tabSpec.setContent(intent);
        tabHost.addTab(tabSpec);
    }
}
