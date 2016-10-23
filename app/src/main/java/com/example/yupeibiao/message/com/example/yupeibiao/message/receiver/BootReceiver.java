package com.example.yupeibiao.message.com.example.yupeibiao.message.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.yupeibiao.message.com.example.yupeibiao.message.service.MySmsService;

/**
 * Created by yupeibiao on 16/4/16.
 * 开机广播
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent_boot=new Intent(context, MySmsService.class);
        context.startService(intent_boot);
    }
}
