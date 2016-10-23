package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.LogUtils;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.MyQueryHandler;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.Tools;

/**
 * Created by yupeibiao on 16/4/11.
 */
public class MessageDetailActivity extends Activity implements View.OnClickListener{
    private TextView textview;
    public Context context;
    private TextView tv_send;
    private ListView listView;
    private MyAdapter adapter;
    private String address;
    public  EditText editText;
    private String messageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_messagedetail);
        context=this;
        initViews();
        initEvents();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_send_message:
                String message=editText.getText().toString().trim();
                if(message.equals("")){
                    Toast.makeText(MessageDetailActivity.this,"请输入您的短信内容",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Tools.sendMessage(MessageDetailActivity.this, message, address);
                    if(MyConstants.SEND_MESSAGE_SUCCESS==1){
                        LogUtils.showLog("yupeibiao",MyConstants.SEND_MESSAGE_SUCCESS+"");
                        editText.setText("");
                    }else{
                        LogUtils.showLog("yupeibiao",MyConstants.SEND_MESSAGE_SUCCESS+"");
                       //MyConstants.SEND_MESSAGE_SUCCESS=0;
                    }
                    InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0,2);
                }
            break;
        }
    }

    public void initViews(){
        editText=(EditText)findViewById(R.id.et_input_msg_conversation_detail);
        tv_send=(TextView)findViewById(R.id.tv_send_message);
        listView=(ListView)findViewById(R.id.lv_conversation_detail);
        textview=(TextView)findViewById(R.id.tv_title_conversation_detail);
    }
    private void prepareData() {

        MyQueryHandler myQueryHandler = new MyQueryHandler(getContentResolver());
        myQueryHandler.setOnCursorChangedListener(new MyQueryHandler.IOnCursorChangedListener() {

            @Override
            /**
             *  当adapter 获得 cursor 的时候，回调此方法
             */
            public void onCursorChanged(int token, Object cookie, Cursor cursor) {
                // 让listview 显示最后一行
                listView.setSelection(adapter.getCount()-1);

            }
        });
        myQueryHandler.startQuery(99, adapter, MyConstants.URI_SMS, projection, " address="+address, null, " date ");
    }

    /**
     * 显示会话详情，所需要的列
     */
    private String[] projection={
            "_id","body","date","type"
    };

    /**
     * 短信内容所在列的索引值  为 1
     */
    private final int INDEX_BODY = 1;
    private final int INDEX_DATE = 2;
    private final int INDEX_TYPE = 3;



    public void initEvents(){
        address=getIntent().getStringExtra("address");
         messageName=Tools.findNameByNumber(context, address);
        if(TextUtils.isEmpty(messageName)){
            textview.setText(address);
        }else{
            textview.setText(messageName);
        }
        tv_send.setOnClickListener(this);
        adapter=new MyAdapter(context,null);
        listView.setAdapter(adapter);

        listView.setDivider(null);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(adapter.getCount()-1);
            }
        });
        prepareData();

    }
    class MyAdapter extends CursorAdapter{
        public MyAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        /**
         * 当内容发生改变的时候，回调此方法
         */
        protected void onContentChanged() {

            super.onContentChanged();
            // 让listView 显示最后一行
            listView.setSelection(getCount() - 1);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view=View.inflate(context,R.layout.list_item_message_detail,null);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.tl_send=(TableLayout)view.findViewById(R.id.tl_send);
            viewHolder.tl_receive=(TableLayout)view.findViewById(R.id.tl_receive);
            viewHolder.tv_receive=(TextView)view.findViewById(R.id.tv_msg_receive);
            viewHolder.tv_send=(TextView)view.findViewById(R.id.tv_msg_send);
            viewHolder.tv_date_send=(TextView)view.findViewById(R.id.tv_date_time);
            viewHolder.tv_date_receive=(TextView)view.findViewById(R.id.tv_date);
            view.setTag(viewHolder);
            return view;
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder vh=(ViewHolder)view.getTag();
            int type = cursor.getInt(INDEX_TYPE);// 获得短信类型
            String text = cursor.getString(INDEX_BODY);//获得短信内容
            Log.i("yupeibiao",text);
            long when = cursor.getLong(INDEX_DATE);// 获得日期
            String dateStr = DateFormat.getDateFormat(context).format(when);
            if(type == MyConstants.TYPE_RECEIVE){ // 接收到的短信
                vh.tl_receive.setVisibility(View.VISIBLE);
                vh.tl_send.setVisibility(View.GONE);
                //设置短信内容
                vh.tv_receive.setText(text);
                //vh.tv_date_receive.setText(dateStr);
            }else{
                vh.tl_receive.setVisibility(View.GONE);
                vh.tl_send.setVisibility(View.VISIBLE);
                //设置短信内容
                vh.tv_send.setText(text);
                vh.tv_date_send.setText(dateStr);
            }
        }
    }
    class ViewHolder{
         TextView tv_date_receive;
         TextView tv_date_send;
         TextView tv_send;
         TextView tv_receive;
         TableLayout tl_send;
         TableLayout tl_receive;
    }

}
