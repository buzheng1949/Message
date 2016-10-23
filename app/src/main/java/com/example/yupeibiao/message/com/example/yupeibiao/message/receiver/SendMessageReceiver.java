package com.example.yupeibiao.message.com.example.yupeibiao.message.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;

/**
 * Created by yupeibiao on 16/4/12.
 */
public class SendMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        try {
    /* android.content.BroadcastReceiver.getResultCode()方法 */
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    MyConstants.SEND_MESSAGE_SUCCESS=1;

                    /* 发送短信成功 */
                    Toast.makeText(context,"短信发送成功",Toast.LENGTH_LONG).show();
                    //Tools.insertMsg2msmDb();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    MyConstants.SEND_MESSAGE_SUCCESS=0;
                    Toast.makeText(context,"短信发送失败,请查看草稿箱",Toast.LENGTH_LONG).show();
                    /* 发送短信失败 */
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    MyConstants.SEND_MESSAGE_SUCCESS=0;
                    Toast.makeText(context,"短信发送失败,请查看草稿箱",Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    MyConstants.SEND_MESSAGE_SUCCESS=0;
                    Toast.makeText(context,"短信发送失败,请查看草稿箱",Toast.LENGTH_LONG).show();
                    break;
                default:

                    break;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
