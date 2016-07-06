package com.yinfork.infoqapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbOpenHelper";
    private static final String DATABASE_NAME = "news.db";
    private static final int VERSION = 1;

    private static Context mContext;
    private static DbOpenHelper sInstance;

    public static void initContext(Context context){
        mContext = context;
    }

    private DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public synchronized static DbOpenHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DbOpenHelper(mContext);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(NewsDbBean.CREATE_TABLE);
        }catch (Exception e){
            Log.e(TAG,"",e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
