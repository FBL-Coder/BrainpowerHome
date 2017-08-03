package cn.semtec.community2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lidroid.xutils.util.LogUtils;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ecommunity.db"; //数据库名称
    private static final int version = 4; //数据库版本
    //留影记录
    public static final String VIDEO_RECORD = "record";
    public static final String RECORD_ID = "_id";
    public static final String RECORD_DATE = "date";
    public static final String RECORD_PICTURE = "picture";
    public static final String RECORD_ACCOUNT = "account";
    public static final String RECORD_DEVICE = "device";


    //消息记录
    public static final String MESSAGE = "message";
    public static final String MESSAGE_ID = "_id";
    public static final String MESSAGE_ACCOUNT = "account";
    public static final String MESSAGE_TYPE = "type";
    public static final String MESSAGE_CONTENT = "content";
    public static final String MESSAGE_DATE = "date";
    public static final String MESSAGE_FROM = "comefrom";
    public static final String MESSAGE_ISREAD = "isread";
    public static final String MESSAGE_URL = "url";

    //动态密码开门记录
    public static final String DYNAMIC = "dynamic_password";
    public static final String DYNAMIC_ID = "_id";
    public static final String DYNAMIC_NAME = "name";
    public static final String DYNAMIC_DATE = "date";
    public static final String DYNAMIC_PASSWORD = "password";


    public DBhelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    ;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + VIDEO_RECORD + "(" +
                RECORD_ID + " integer primary key autoincrement," +
                RECORD_DATE + " varchar(20) not null," +
                RECORD_ACCOUNT + " varchar(20) not null," +
                RECORD_DEVICE + " varchar(40) not null," +
                RECORD_PICTURE + " varchar(150) not null" + ")";
        db.execSQL(sql);

        String sql1 = "create table if not exists " + MESSAGE + "(" +
                MESSAGE_ID + " integer primary key autoincrement," +
                MESSAGE_ACCOUNT + " varchar(20) not null," +
                MESSAGE_TYPE + " varchar(10) not null," +
                MESSAGE_CONTENT + " varchar(1000) not null," +
                MESSAGE_DATE + " varchar(20) not null," +
                MESSAGE_FROM + " varchar(20) not null," +
                MESSAGE_URL + " varchar(100) not null," +
                MESSAGE_ISREAD + " integer not null" + ")";

        db.execSQL(sql1);
        String sql2 = "create table if not exists " + DYNAMIC + "(" +
                DYNAMIC_ID + " integer primary key autoincrement," +
                DYNAMIC_NAME + " varchar(20) not null," +
                DYNAMIC_DATE + " varchar(20) not null," +
                DYNAMIC_PASSWORD + " varchar(4) not null" + ")";
        db.execSQL(sql2);
        LogUtils.e("creat数据库");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql = "drop table if exists " + VIDEO_RECORD;
//        db.execSQL(sql);
//        String sql1 = "drop table if exists " + MESSAGE;
//        db.execSQL(sql1);
//        String sql2 = "drop table if exists " + DYNAMIC;
//        db.execSQL(sql2);
        onCreate(db);
    }


}
