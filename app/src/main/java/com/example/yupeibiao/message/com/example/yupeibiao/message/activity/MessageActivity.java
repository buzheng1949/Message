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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.MyQueryHandler;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.Tools;

import java.util.HashSet;

/**
 * Created by yupeibiao on 16/4/8.
 */
public class MessageActivity extends Activity implements AdapterView.OnItemClickListener{

    public static Cursor corous;
    private ListView listView;
    public  static boolean isEdit = false;

    public static MyListAdapter adapter ;

    public Context context;

    public static  HashSet<Integer> clickSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.context = this;
        init();
        prepareData();

    }

    /**
     * 要查询的列
     */
    private String[] projection={
            "sms.body AS snippet",
            "sms.thread_id AS _id",
            "groups.msg_count AS msg_count",
            "address as address",
            "date as date"
    };

    /**
     * 短信内容所在列的索引值 为 0
     */
    private final int INDEX_BODY = 0;
    /**
     * 会话ID所在列的索引值 为 1
     */
     public static  final int INDEX_THREAD_ID = 1;
    /**
     * 短信数量所在列的索引值 为 2
     */
    private final int INDEX_MSG_COUNT = 2;
    /**
     * 短信联系人电话所在列的索引值 为 3
     */
    private final int INDEX_ADDRESS = 3;
    /**
     * 短信日期所在列的索引值 为 4
     */
    private final int INDEX_DATE = 4;



    /**
     * 给listView 准备数据
     */
    private void prepareData() {

        MyQueryHandler queryHandler = new  MyQueryHandler(getContentResolver());

        queryHandler.startQuery(234, adapter, MyConstants.URI_CONVERSATION, projection, null, null, " date desc");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        corous=adapter.getCursor();
        int threadID=corous.getInt(INDEX_THREAD_ID);
        if(isEdit){
            if(!clickSet.contains(threadID)){
                clickSet.add(threadID);
            }else{
                clickSet.remove(threadID);
            }
            adapter.notifyDataSetChanged();
        }else{
            String address=corous.getString(INDEX_ADDRESS);
            Intent intent=new Intent(this,MessageDetailActivity.class);
            intent.putExtra("address",address);
            startActivity(intent);
        }

    }

    private void init() {
        listView = (ListView) findViewById(R.id.lv_message);
        adapter = new MyListAdapter(this, null);
        listView.setAdapter(adapter);
        clickSet=new HashSet<Integer>();
        listView.setOnItemClickListener(this);

    }




    class MyListAdapter extends CursorAdapter {

        public MyListAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        /**
         * 当 conterView是null时，需要用户创建view 的时候调
         */
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            View view = View.inflate(context, R.layout.list_item_conversation,null);
            ViewHolder vh = new ViewHolder();
            vh.face = (ImageView) view.findViewById(R.id.iv_face_list_item);
            vh.checkbox=(ImageView)view.findViewById(R.id.image_checkbox);
            vh.address = (TextView) view.findViewById(R.id.tv_address_list_item);
            vh.body = (TextView) view.findViewById(R.id.tv_body_list_item);
            vh.date = (TextView) view.findViewById(R.id.tv_date_list_item);
            view.setTag(vh);
            return view;
        }

        @Override

        public void bindView(View view, Context context, Cursor cursor) {

            ViewHolder vh = (ViewHolder) view.getTag();


            vh.body.setText(cursor.getString(INDEX_BODY));


            long when = cursor.getLong(INDEX_DATE);
            String dateStr;

            if(DateUtils.isToday(when)){
                dateStr = DateFormat.getTimeFormat(context).format(when);
            }else{
                dateStr = DateFormat.getDateFormat(context).format(when);
            }
            vh.date.setText(dateStr);

            String number = cursor.getString(INDEX_ADDRESS);
            int msgcount = cursor.getInt(INDEX_MSG_COUNT);// 获得短信的数量

            String name = Tools.findNameByNumber(context, number);
            if(name == null){//表明无此联系人
                vh.address.setText(number+"("+msgcount+")");
            }else{
                vh.address.setText(name+"("+msgcount+")");
            }

            if(isEdit){

             vh.checkbox.setVisibility(View.VISIBLE);
            int threadId=cursor.getInt(INDEX_THREAD_ID);
            if(clickSet.contains(threadId)){
             vh.checkbox.setEnabled(true);
            }else{
               vh.checkbox.setEnabled(false);
            }
             }else{
              vh.checkbox.setVisibility(View.GONE);
            }
        }
    }

    class ViewHolder{
        public ImageView checkbox;
        public ImageView face;
        public TextView address;
        public TextView body;
        public TextView date;
    }


}
