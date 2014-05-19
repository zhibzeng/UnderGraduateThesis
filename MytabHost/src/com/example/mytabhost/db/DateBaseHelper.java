package com.example.mytabhost.db;
import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateBaseHelper extends SQLiteOpenHelper {
   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_NAME = "autoComplete4.db";
    // ����name�Զ���ѯ
    public static final String NAME = "name";
    public static final String BorC = "BorC";
    public static final String OorI = "OorI";
    public DateBaseHelper(Context context) {
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       String sqlString = "create table test (_id integer primary key autoincrement,BorC integer,OorI integer,name varchat(20) not null on conflict fail)";
       db.execSQL(sqlString);
       // ��ʼ���ݿ��
       String[] nameStrArrayStr = new String[] {
    	//�⻷ 28
        "���ڶ�·����", "����·�����", "��ҵ·����","��ҵ·���","������·����","������·���","�ƻ���·����","�ƻ���·���","����·����","����·����",
        "����·����","˫��·���","˫��·����","˫��·����","����·����","ɼ����·����","ɼ����·���","���豱·����","���豱·���","����·����",
        "������·���","������·����","���·���","���¸ɵ�����","���¸ɵ����","����̳���","��ɳ·���","����ӳ���","��Ϫ��·���",
        //�ڻ� 30
       "��ʯ��·����","��԰��·���","������·����","Ӫ�˽����","����ӳ���","ɳ��·����","�����г����","��վ����·����","���Ǵ������","���¸ɵ����",
       "�컨��·���","����·����","����·���","����·����","ɼ����·����","ɼ����·���","˫�ֱ�֧·���","˫��·˫��·����","ţ�п����","����·����",
       "����·���","������·����","�ƻ���·����","������·����","��ҵ·����","��ҵ·���","������·����","����¥·���","���������",
       "�����������",

       //�ڻ�
       "˫���ӱ�վ","˫������վ","ţ�п�վ","����·��վ","����·��վ","���ʹ���վ","������԰վ","�ƻ�·��վ","����������վ","�Ͼ���·��վ",
       "����¥��վ","����¥��վ","����·��վ","��ˮ��վ","�⻪��ֿ�վ","��������վ","��·�ڱ�վ","Ӫ�ſڱ�վ","���Ͻ���վ","��ó�����վ",
       "�𳵱�վ��վ","������վ","����·��վ","����·��վ","����·��վ","�����Ͽ�·վ","ɼ����վ","˫�ֱ�֧·��վ","���곡վ",
       
       //�⻷
       "������·��վ","����·��վ","����·��վ","����·��վ","������վ","�𳵱�վ��վ","��ó�����վ","���Ͻ���վ","Ӫ�ſڱ�վ",
       "��·�ڱ�վ","��������վ","�⻪��ֿ�վ","��ˮ��վ","����·��վ","����¥��վ","����¥��վ","�Ͼ���·��վ","����������վ","�ƻ�·��վ",
       "������԰վ","���ʹ���վ","����·��վ","����·��վ","ţ�п�վ","˫������վ","˫���ӱ�վ","���곡վ","˫�ֱ�֧·��վ"
       };
       //1��ʾC,��car;0��ʾB,��bus
       int[] BorCStrArrayStr = new int[] {
    		   //car28��
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,
    		   //car30��
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,1,1,
    		   //bus28��
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0,
    		   //bus28��
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0
    		                              };  
       //1��ʾI,���ڻ���0��ʾO�����⻷
       int[] OorIStrArrayStr = new int[] {
               //�⻷31��
               0,0,0,0,0,0,0,0,0,0,
               0,0,0,0,0,0,0,0,0,0,
               0,0,0,0,0,0,0,0,
               //�ڻ�30��
    		   1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,1,1,
               //�ڻ�28��
               1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,
               //�⻷28��
               0,0,0,0,0,0,0,0,0,0,
               0,0,0,0,0,0,0,0,0,0,
               0,0,0,0,0,0,0,0
               };
       for (int i = 0; i < nameStrArrayStr.length; i++) {
           db.execSQL("INSERT INTO test("+BorC+","+OorI+","+NAME+")values(?,?,?)",new Object[] { BorCStrArrayStr[i],OorIStrArrayStr[i],nameStrArrayStr[i]});
       }
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
       // do nothing here
    }
    /**
     * ������������ģ����ѯ
     * @param name
     * @return
     */
    public Cursor query(String name,int BorC,int OorI) {
       SQLiteDatabase db = this.getReadableDatabase();
      // return db.rawQuery("select * from test where name like '%" + name + "%' limit 10",null);
       return db.rawQuery("select * from test where BorC="+BorC+" and OorI="+OorI+" and name like '%" + name + "%' limit 10",null);

    }
}
