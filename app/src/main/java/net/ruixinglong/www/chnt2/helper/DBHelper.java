package net.ruixinglong.www.chnt2.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String name = "chnt.db";
    private static final int version = 1;

    public DBHelper(Context context) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS user(" +
                "_id integer not null primary key autoincrement," +
                "name varchar(255)," +
                "telephone varchar(20)," +
                "email varchar(255)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20)," +
                "card varchar(255))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // do nothing
    }
}
