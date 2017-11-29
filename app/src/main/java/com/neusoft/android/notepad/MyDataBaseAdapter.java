package com.neusoft.android.notepad;

/**
 * Created by user on 2017/11/29.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    private static final String DATABASE_NAME = "note.db";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;

    private Context mContext = null;
    private static final String DB_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_BODY + " TEXT)";

    private SQLiteDatabase mdb = null;
    private DbHelper mdbHelper = null;

    private static class DbHelper extends SQLiteOpenHelper
    {
        public DbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(DB_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public MyDataBaseAdapter(Context context)
    {
        mContext = context;
    }

    public SQLiteDatabase open() throws SQLException
    {
        mdbHelper = new DbHelper(mContext);
        mdb = mdbHelper.getWritableDatabase();
        return mdb;
    }

    public void close()
    {
        mdbHelper.close();
    }


    public long insertData(SQLiteDatabase db, String title, String body)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY,body);

        return db.insert(DATABASE_TABLE, KEY_ROWID, initialValues);
    }

    public boolean delete(SQLiteDatabase db, long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + " = " + rowId, null)>0;
    }

    public Cursor fetchAllData(SQLiteDatabase db)
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_TITLE,KEY_BODY}, null, null, null, null, null);
    }

    public Cursor fetchData(SQLiteDatabase db, long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,KEY_BODY}, KEY_ROWID + " = " + rowId, null, null, null, null, null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateData(SQLiteDatabase db, long rowId, String title, String body)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);

        return db.update(DATABASE_TABLE, args, KEY_ROWID + " = " + rowId, null)>0;
    }

}

