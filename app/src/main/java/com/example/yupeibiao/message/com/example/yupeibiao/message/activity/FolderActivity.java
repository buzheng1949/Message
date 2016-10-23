package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
 * 文件界面
 */
public class FolderActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private String [] nameList;
    private int []  imageSource;
    private ListView listview;
    private ImageView imageview;
    private TextView tv_name;
    private MyFolderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initEvents();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
            intent=new Intent(FolderActivity.this,FolderDetailActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);

    }

    class MyFolderAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nameList.length;
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
            View view=View.inflate(FolderActivity.this, R.layout.list_item_folder,null);
            imageview=(ImageView)view.findViewById(R.id.iv_icon_folder);
            tv_name=(TextView)view.findViewById(R.id.tv_name_folder);
            imageview.setBackgroundResource(imageSource[position]);
            tv_name.setText(nameList[position]);
            return view;
        }
    }
    public void initEvents(){
        adapter=new MyFolderAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }
    public void initViews(){
         listview=getListView();
         nameList=new String[]{" 收件箱","发件箱"," 已发送","  草稿箱"};
         imageSource=new int[]{R.mipmap.b1,R.mipmap.b2,R.mipmap.b3,R.mipmap.b4};

    }
}
