package com.example.mytabhost;


import com.example.mytabhost.db.DateBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class AutoCompleteAdater extends SimpleCursorAdapter {
    private DateBaseHelper dbHelper = null;
    private Context context;
    // ��ѯ�ֶ�
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
     * ��̬��ѯ���ݿ�
     */
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
       if (constraint != null) {
           return getDbHelper().query((String)constraint,BorC,OorI);
       } else {
           return null;
       }
    } 
    /**
     * ���������ڵ�������ʾ�б��е��ĳһ���ķ���ֵ,����ֵ������ʾ���ı�����
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
