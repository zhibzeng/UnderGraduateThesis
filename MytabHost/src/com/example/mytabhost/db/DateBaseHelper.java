package com.example.mytabhost.db;
import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateBaseHelper extends SQLiteOpenHelper {
   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_NAME = "autoComplete4.db";
    // 根据name自动查询
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
       // 初始数据库表
       String[] nameStrArrayStr = new String[] {
    	//外环 28
        "龙腾东路出口", "丽都路口入口", "创业路出口","创业路入口","人民南路出口","人民南路入口","科华中路出口","科华中路入口","琉璃路出口","锦华路出口",
        "东大路出口","双桂路入口","双桂路出口","双庆路出口","万年路出口","杉板桥路出口","杉板桥路入口","建设北路出口","建设北路入口","府青路出口",
        "驷马桥路入口","驷马桥路出口","解放路入口","北新干道出口","北新干道入口","九里堤出口","银沙路入口","茶店子出口","清溪西路入口",
        //内环 30
       "大石西路出口","家园南路入口","抚琴西路出口","营兴街入口","茶店子出口","沙湾路出口","府河市场入口","北站东二路出口","北星大道出口","北新干道入口",
       "红花北路入口","府青路出口","府青路入口","建设路出口","杉板桥路出口","杉板桥路入口","双林北支路入口","双庆路双桥路出口","牛市口入口","龙舟路出口",
       "莲桂东路入口","净居寺路出口","科华中路出口","人民南路出口","创业路出口","创业路入口","高升桥路出口","红牌楼路入口","武侯大道出口",
       "成温立交入口",

       //内环
       "双桥子北站","双桥子南站","牛市口站","莲桂东路口站","龙舟路口站","成仁公交站","东湖公园站","科华路口站","人南立交东站","紫荆北路口站",
       "红牌楼东站","红牌楼西站","少陵路口站","清水河站","光华村街口站","府南新区站","蜀汉路口北站","营门口北站","西南交大站","商贸大道口站",
       "火车北站西站","高笋塘站","三友路口站","桃蹊路口站","建设路口站","建设南口路站","杉板桥站","双林北支路口站","万年场站",
       
       //外环
       "建设南路口站","建设路口站","桃蹊路口站","三友路口站","高笋塘站","火车北站西站","商贸大道口站","西南交大站","营门口北站",
       "蜀汉路口北站","府南新区站","光华村街口站","清水河站","少陵路口站","红牌楼西站","红牌楼东站","紫荆北路口站","人南立交东站","科华路口站",
       "东湖公园站","成仁公交站","龙舟路口站","莲桂东路口站","牛市口站","双桥子南站","双桥子北站","万年场站","双林北支路口站"
       };
       //1表示C,即car;0表示B,即bus
       int[] BorCStrArrayStr = new int[] {
    		   //car28个
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,
    		   //car30个
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,1,1,
    		   1,1,1,1,1,1,1,1,1,1,
    		   //bus28个
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0,
    		   //bus28个
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0,0,0,
    		   0,0,0,0,0,0,0,0
    		                              };  
       //1表示I,即内环；0表示O，即外环
       int[] OorIStrArrayStr = new int[] {
               //外环31个
               0,0,0,0,0,0,0,0,0,0,
               0,0,0,0,0,0,0,0,0,0,
               0,0,0,0,0,0,0,0,
               //内环30个
    		   1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,1,1,
               //内环28个
               1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,1,1,
               1,1,1,1,1,1,1,1,
               //外环28个
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
     * 根据输入内容模糊查询
     * @param name
     * @return
     */
    public Cursor query(String name,int BorC,int OorI) {
       SQLiteDatabase db = this.getReadableDatabase();
      // return db.rawQuery("select * from test where name like '%" + name + "%' limit 10",null);
       return db.rawQuery("select * from test where BorC="+BorC+" and OorI="+OorI+" and name like '%" + name + "%' limit 10",null);

    }
}
