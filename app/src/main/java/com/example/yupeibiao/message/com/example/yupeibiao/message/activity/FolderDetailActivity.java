package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.LogUtils;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.MyQueryHandler;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.Tools;

/**
 * Created by yupeibiao on 16/4/12.
 */
public class FolderDetailActivity extends Activity {
    private ListView listview;
    private MyAdapter adapter;
    private int position;
    private TextView tv_folder_topbar;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folderdetailactivity);
        initViews();
        initEvents();
    }
    public void initViews(){
        position = getIntent().getIntExtra("position", 0);
        tv_folder_topbar=(TextView)findViewById(R.id.tv_folder_topbar);
        listview=(ListView)findViewById(R.id.list_folder_detail);
    }
    public void initEvents(){
        if(position==0){
            tv_folder_topbar.setText("收件箱");
        }else if(position==1){
            tv_folder_topbar.setText("发件箱");
        }else if(position==2){
            tv_folder_topbar.setText("已发送");
        }else{
            tv_folder_topbar.setText("草稿箱");
        }

        adapter=new MyAdapter(FolderDetailActivity.this,null);
        prepareData();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String address=MessageActivity.adapter.getCursor().getString(INDEX_ADDRESS);
                //Holder vh = (Holder) view.getTag();
                //TextView who=(TextView)view.findViewById(R.id.tv_address);
               // vh.tv_name.getText().toString();
               // String address=who.getText().toString();
                //String tv=((TextView)view.findViewById(R.id.tv_address)).getText().toString();
                Intent intent=new Intent(FolderDetailActivity.this,MessageDetailActivity.class);
                intent.putExtra("address",
                       adapter.getCursor().getString(INDEX_ADDRESS));
                startActivity(intent);
            }
        });
    }

    private String[] projection={
            "body","_id","address","date"
    };

    /**
     * 短信内容所在列的索引值 为 0
     */
    private final int INDEX_BODY = 0;
    /**
     * 短信联系人电话所在列的索引值 为 3
     */
    private final int INDEX_ADDRESS = 2;
    /**
     * 短信日期所在列的索引值 为 4
     */
    private final int INDEX_DATE = 3;


    private void prepareData() {
        MyQueryHandler myQueryHandler =new MyQueryHandler(getContentResolver());
        myQueryHandler.startQuery(99, adapter, Tools.getUriFromIndex(position),
                projection, null, null, " date desc");

        myQueryHandler.setOnCursorChangedListener(new MyQueryHandler.IOnCursorChangedListener() {

            @Override
            public void onCursorChanged(int token, Object cookie, Cursor cursor) {

                cursor.moveToPosition(-1);
            }
        });
    }
    class MyAdapter extends CursorAdapter{

        public MyAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup parent) {
            View view=View.inflate(FolderDetailActivity.this,R.layout.list_item_folderdetail,null);
            Holder holder=new Holder();
            holder.tv_name=(TextView)view.findViewById(R.id.tv_address);
            holder.tv_content=(TextView)view.findViewById(R.id.tv_content);
            holder.tv_number=(TextView)view.findViewById(R.id.tv_number_folderdetail);
            view.setTag(holder);

            return view;
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Holder vh = (Holder) view.getTag();

            vh.tv_content.setText(cursor.getString(INDEX_BODY));

            long when = cursor.getLong(INDEX_DATE);
            String dateStr;
            if(DateUtils.isToday(when)){
                dateStr = DateFormat.getTimeFormat(context).format(when);
            }else{
                dateStr = DateFormat.getDateFormat(context).format(when);
            }
            vh.tv_number.setText(dateStr);
            number = cursor.getString(INDEX_ADDRESS);
            final String name = Tools.findNameByNumber(context, number);
            if(name == null){
                vh.tv_name.setText(number);
            }else{
                vh.tv_name.setText(name);
            }
            LogUtils.showLog("yupeibiao", vh.tv_name.getText().toString() + "abcd");


        }
    }
    class Holder{
        private TextView tv_name;
        private TextView tv_content;
        private TextView tv_number;
    }

}
