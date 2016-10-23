package com.example.yupeibiao.message.com.example.yupeibiao.message.tools;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;

import com.example.yupeibiao.message.com.example.yupeibiao.message.constant.MyConstants;

import java.io.InputStream;
import java.util.ArrayList;

public class Tools {

	public static void printCursor(Cursor cursor) {

		if (cursor == null) {
			System.out.println("cursor == null");
			return;
		}
		if (cursor.getCount() == 0) {
			System.out.println("cursor.getCount() == 0");
			return;
		}

		while (cursor.moveToNext()) {
			int columnCount = cursor.getColumnCount();
			System.out.println("当前是第几行：" + cursor.getPosition());
			for (int i = 0; i < columnCount; i++) {
				String columnName = cursor.getColumnName(i);
				String value = cursor.getString(i);
				System.out.println(columnName + " : " + value);
			}

		}
	}
	public static String findAddressByName(Context ctx, String name) {
		String address = null;

		Uri uri2 = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, name);

		Cursor cursor = ctx.getContentResolver().query(uri2,
				new String[] { "address" }, null, null, null);
		if (cursor.getCount() == 0) {
			return null;
		} else {
			// cursor 返回时，默认指向的是 第一行的上一行，即 -1 行 ,而所有的数据是从 第 0行开始的。
			cursor.moveToNext();
			address = cursor.getString(0);// cursor 仅查询一列内容，所以取的时候，列的索引值为 0
		}
		return address;
	}

	public static String findNameByNumber(Context ctx, String number) {
		String name = null;

		Uri uri2 = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);

		Cursor cursor = ctx.getContentResolver().query(uri2,
				new String[] { "display_name" }, null, null, null);
		if (cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToNext();
			name = cursor.getString(0);
		}
		return name;
	}

	public static int findIDByNumber(Context ctx, String number) {
		int contactId;

		Uri uri2 = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);

		Cursor cursor = ctx.getContentResolver().query(uri2,
				new String[]{"_id"}, null, null, null);
		if (cursor.getCount() == 0) {
			return -1;
		} else {
			// cursor 返回时，默认指向的是 第一行的上一行，即 -1 行 ,而所有的数据是从 第 0行开始的。
			cursor.moveToNext();
			contactId = cursor.getInt(0);// cursor 仅查询一列内容，所以取的时候，列的索引值为 0
		}
		return contactId;
	}


	public static Bitmap getFaceById(Context ctx, String contactId) {
		Bitmap bitmap = null;

		Uri uri = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId);

		InputStream input = Contacts.openContactPhotoInputStream(ctx.getContentResolver(), uri);

		if(input == null){
			return null;
		}
		
		bitmap = BitmapFactory.decodeStream(input);
		
		return bitmap;
	}

	public static void deleteMsgByThreadId(Context ctx, Integer threadId) {
		
		ctx.getContentResolver().delete(MyConstants.URI_SMS, " thread_id = ?", new String[]{"" + threadId});
		
	}


	public static void sendMessage(Context ctx, String msg, String address) {
		
		SmsManager smsManager = SmsManager.getDefault();
		//对短信内容进行切割，防止，内容过长。
		ArrayList<String> msgList = smsManager.divideMessage(msg);
		
		//用于启动广播的意图
		Intent intent1 = new Intent(MyConstants.SMS_SEND_ACTIOIN);
		Intent intent2=new Intent(MyConstants.SMS_DELIVERED_ACTION);
		PendingIntent sentIntent = PendingIntent.getBroadcast(ctx, 88, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent delievrIntent = PendingIntent.getBroadcast(ctx, 88, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		for (int i = 0; i < msgList.size(); i++) {
			String oneMsg = msgList.get(i);
			smsManager.sendTextMessage(
					address,
					null,
					oneMsg,
					sentIntent,
					delievrIntent);
		}

			insertMsg2msmDb(ctx,msg,address);


		
	}


	public static void insertMsg2msmDb(Context ctx, String msg, String address) {
		
		ContentValues values = new ContentValues();
		
		values.put("address", address); 
		
		values.put("body", msg); 
		
		values.put("type", MyConstants.TYPE_SEND); 
		
		ctx.getContentResolver().insert(MyConstants.URI_SMS, values);
		
	}


	public static  Uri getUriFromIndex(int index){
		switch (index) {
		case 0:
			return MyConstants.URI_INBOX;
		case 1:
			return MyConstants.URI_OUTBOX;
		case 2:
			return MyConstants.URI_DRAFT;
		case 3:
			return MyConstants.URI_SENT;

		}
		return null;
	}
	
}
