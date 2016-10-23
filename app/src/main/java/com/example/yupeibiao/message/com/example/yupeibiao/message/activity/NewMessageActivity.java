package com.example.yupeibiao.message.com.example.yupeibiao.message.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;
import com.example.yupeibiao.message.com.example.yupeibiao.message.tools.Tools;

/**
 * Created by yupeibiao on 16/4/14.
 * 新建短信界面
 */
public class NewMessageActivity extends Activity implements View.OnClickListener {

    private AutoCompleteTextView autoCompleteTextView;
    private ImageView selectContact;

    private EditText inputMsg;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_message);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autotext);
        selectContact = (ImageView) findViewById(R.id.iv_select_contact);
        inputMsg = (EditText) findViewById(R.id.et_input_msg);
        selectContact.setOnClickListener(this);
        findViewById(R.id.btn_send_msg).setOnClickListener(this);

        adapter = new ACTVAdapter(this, null);
        autoCompleteTextView.setAdapter(adapter);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {


            public Cursor runQuery(CharSequence constraint) {

                System.out.println(constraint);

                Cursor cursor = getContentResolver().query(MyConstants.URI_CONTACTS, projection, "data1 like '%"+constraint+"%'", null, null);
                return cursor;
            }
        });

    }

    /**
     * 要查询的列
     */
    private String[] projection = {
            "_id","data1","display_name"
    };

    private final int INDEX_NAME=2;
    private final int INDEX_NUMBER=1;

    private ACTVAdapter adapter;

    class ACTVAdapter extends CursorAdapter{

        public ACTVAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override

        public CharSequence convertToString(Cursor cursor) {
            return cursor.getString(INDEX_NUMBER);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = View.inflate(context, R.layout.list_item_newmessage, null);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView name = (TextView) view.findViewById(R.id.tv_name_actv);
            TextView number = (TextView) view.findViewById(R.id.tv_number_actv);

            name.setText(cursor.getString(INDEX_NAME));
            number.setText(cursor.getString(INDEX_NUMBER));
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_msg:// 发送短信的按钮
                String address = autoCompleteTextView.getText().toString();
                if(TextUtils.isEmpty(address.trim())){
                    Toast.makeText(this, "请输入收件人号码", Toast.LENGTH_SHORT).show();
                    return ;
                }
                String msg = inputMsg.getText().toString();
                if(TextUtils.isEmpty(msg.trim())){
                    Toast.makeText(this, "请输入短信内容",Toast.LENGTH_SHORT).show();
                    return ;
                }
                Tools.sendMessage(this, msg, address);
                //inputMsg.setText("");
                finish();
               /* Intent intent1=new Intent(NewMessageActivity.this,MessageDetailActivity.class);
                intent1.putExtra("address",
                        adapter.getCursor().getString(1));
                startActivity(intent1);*/

                break;
            case R.id.iv_select_contact:

                Intent intent = new Intent();
                intent.setAction("android.intent.action.PICK");
                intent.setData(Uri.parse("content://com.android.contacts/contacts"));
                startActivityForResult(intent, 99);

                break;
            default:
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String stringExtra = data.getStringExtra("phone");
        System.out.println("stringExtra::"+stringExtra);

        Uri uri = data.getData();

        Cursor cursor = getContentResolver().query(uri, new String[]{"_id"}, null, null, null);
        cursor.moveToNext();
        int contactId = cursor.getInt(0);

        Cursor cursor2 = getContentResolver().query(MyConstants.URI_CONTACTS, new String[]{"data1"}, " contact_id = "+contactId, null, null);
        cursor2.moveToNext();

        String number = cursor2.getString(0);

        autoCompleteTextView.setText(number);

    }


}
