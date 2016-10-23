package com.example.yupeibiao.message.com.example.yupeibiao.message.constant;

import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * 常量存储
 */
public class MyConstants {

	public static int PASSWORD_RIGHT = 0;//默认验证密码错误
	public static boolean NEED_TO_LOGGING=false;//默认不保存

	public static Uri URI_CONVERSATION = Uri.parse("content://sms/conversations");

	public static Uri URI_SMS = Uri.parse("content://sms");

	public static Uri URI_CONTACTS = Phone.CONTENT_URI;

	public static int TYPE_RECEIVE = 1;

	public static int TYPE_SEND = 2;

	public static int SEND_MESSAGE_SUCCESS=1;

	public static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	public static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

	public static Uri URI_INBOX = Uri.parse("content://sms/inbox");

	public static Uri URI_OUTBOX = Uri.parse("content://sms/outbox");

	public static Uri URI_DRAFT = Uri.parse("content://sms/draft");

	public static Uri URI_SENT = Uri.parse("content://sms/sent");
	
	
}
