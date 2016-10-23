package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yupeibiao.message.R;

/**
 * Created by yupeibiao on 16/4/8.
 * 设置界面
 */
public class GroupActivity extends Activity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private String [] listItemName;
    private int [] ImageViewId;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        initViews();
        initEvents();
    }
    public void initViews(){
        listView=(ListView)findViewById(R.id.list_setting);
        listItemName=new String[]{"应用加密","应用解锁","关于软件"};
        ImageViewId=new int[]{R.mipmap.c2,R.mipmap.c1,R.mipmap.c3};
    }
    public void initEvents(){
        adapter=new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if(position==0){
            if(!TextUtils.isEmpty(getSharedPreferences("info.txt",MODE_PRIVATE).getString("password",""))){
                intent =new Intent(GroupActivity.this,CheckPasswordActivity.class);
                startActivity(intent);
            }else{
                intent =new Intent(GroupActivity.this,SettingActivity.class);
                startActivity(intent);
            }

        }else if(position==2){
            intent =new Intent(GroupActivity.this,AboutSoftActivity.class);
            startActivity(intent);
        }else{
            intent =new Intent(GroupActivity.this,ClearPasswordActivity.class);
            startActivity(intent);
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listItemName.length;
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(GroupActivity.this,R.layout.list_item_group,null);
            ImageView imageView=(ImageView)view.findViewById(R.id.group_item_icon);
            TextView tv_name=(TextView)view.findViewById(R.id.group_text_name);
            imageView.setBackgroundResource(ImageViewId[position]);
            tv_name.setText(listItemName[position]);
            return view;
        }
    }



}
