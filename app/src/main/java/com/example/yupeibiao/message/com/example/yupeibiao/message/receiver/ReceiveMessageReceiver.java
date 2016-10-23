package com.example.yupeibiao.message.com.example.yupeibiao.message.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.WindowManager;

import com.example.yupeibiao.message.R;
import com.example.yupeibiao.message.com.example.yupeibiao.message.activity.MainActivity;

/**
 * Created by yupeibiao on 16/4/16.
 */
public class ReceiveMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        Bundle bundle=intent.getExtras();
        Object [] objects=(Object [])bundle.get("pdus");
        for(Object object:objects){
            SmsMessage sms= SmsMessage.createFromPdu((byte[]) object);
             final AlertDialog.Builder builder=new AlertDialog.Builder(context);
           // builder.setTitle(sms.getOriginatingAddress());
           // builder.setMessage(sms.getMessageBody());
            builder.setTitle("新消息");
            builder.setMessage("您有一条新消息，请查收！");
            builder.setIcon(R.mipmap.message_chat_pressed);
            builder.setNegativeButton("忽略", null);
            builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(context, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
        }
    }
}
