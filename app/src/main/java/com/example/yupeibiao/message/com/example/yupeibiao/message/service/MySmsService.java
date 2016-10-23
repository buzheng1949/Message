package com.example.yupeibiao.message.com.example.yupeibiao.message.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;
import com.example.yupeibiao.message.com.example.yupeibiao.message.receiver.SendMessageReceiver;

/**
 * Created by yupeibiao on 16/4/15.
 */
public class MySmsService extends Service {
    private SendMessageReceiver mReceiverMessageSend;
    private SendMessageReceiver mReceiverMessageSendsuccess;
    @Override
    public void onCreate() {
        super.onCreate();
        /* 自定义IntentFilter为SENT_SMS_ACTIOIN Receiver */
        IntentFilter mFilter01;
        mFilter01 = new IntentFilter(MyConstants.SMS_SEND_ACTIOIN);
        mReceiverMessageSend = new SendMessageReceiver();
        registerReceiver(mReceiverMessageSend, mFilter01);

      /* 自定义IntentFilter为DELIVERED_SMS_ACTION Receiver */
        mFilter01 = new IntentFilter(MyConstants.SMS_DELIVERED_ACTION);
        mReceiverMessageSendsuccess = new SendMessageReceiver();
        registerReceiver(mReceiverMessageSendsuccess, mFilter01);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverMessageSend != null && mReceiverMessageSendsuccess != null) {
            unregisterReceiver(mReceiverMessageSend);
            unregisterReceiver(mReceiverMessageSendsuccess);
        }
    }
}
