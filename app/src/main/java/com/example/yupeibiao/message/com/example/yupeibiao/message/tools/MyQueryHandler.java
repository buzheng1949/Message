package com.example.yupeibiao.message.com.example.yupeibiao.message.tools;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

/**
 * 查询界面
 * 经修改网络上某小哥的某些方法，感谢开源
 */
public class MyQueryHandler extends AsyncQueryHandler{

	public MyQueryHandler(ContentResolver cr) {
		super(cr);
	}


	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		
		System.out.println("onQueryComplete : token:"+token);
		System.out.println("onQueryComplete : cookie:"+cookie);
		Tools.printCursor(cursor);
		
		if(cookie!=null && cookie instanceof CursorAdapter){
			CursorAdapter adapter = (CursorAdapter) cookie;

			adapter.changeCursor(cursor);
		}
		
		if(cursorChangedListener!=null){
			cursorChangedListener.onCursorChanged(token, cookie, cursor);
		}
		
	}
	
	
	public IOnCursorChangedListener getCursorChangedListener() {
		return cursorChangedListener;
	}

	public void setOnCursorChangedListener(IOnCursorChangedListener cursorChangedListener) {
		this.cursorChangedListener = cursorChangedListener;
	}


	private IOnCursorChangedListener cursorChangedListener;
	
	/**
	 *
	 * @author yupeibiao
	 *
	 */
	public interface IOnCursorChangedListener{
		
		void onCursorChanged(int token, Object cookie, Cursor cursor);
	}
	
}
