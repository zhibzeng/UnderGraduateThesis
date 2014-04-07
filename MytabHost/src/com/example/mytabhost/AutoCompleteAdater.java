package com.example.mytabhost;


import com.example.mytabhost.db.DateBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class AutoCompleteAdater extends SimpleCursorAdapter {
    private DateBaseHelper dbHelper = null;
    private Context context;
    // 查询字段
    private String queryField;
    private int BorC;
    private int OorI;
    public AutoCompleteAdater(Context context, int layout, Cursor c,String from, int to,int BorC,int OorI) {
       super(context, layout, c, new String[] { from },new int[] { to });
       this.context = context;
       this.queryField = from;
       this.BorC=BorC;
       this.OorI=OorI;
       
    }
    /**
     * 动态查询数据库
     */
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
       if (constraint != null) {
           return getDbHelper().query((String)constraint,BorC,OorI);
       } else {
           return null;
       }
    } 
    /**
     * 这里设置在弹出的提示列表中点击某一项后的返回值,返回值将被显示在文本框中
     */
    @Override
    public CharSequence convertToString(Cursor cursor) {
       return cursor.getString(cursor.getColumnIndex(queryField));
    }
    
    public DateBaseHelper getDbHelper() {
       if (dbHelper == null) {
           dbHelper = new DateBaseHelper(this.context);
       }
       return dbHelper;
    }
}
