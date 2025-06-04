package com.example.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import java.util.ArrayList;
import java.util.List;

public class BillDBHelper extends SQLiteOpenHelper {
    public static String TAG = "BillDBHelper";
    public static String db_name = "bill.sqlite";
    public static BillDBHelper mHelper = null;
    public Context mContext;
    public int mVersion;
    public SQLiteDatabase mReadDB;
    public SQLiteDatabase mWriteDB;
    public String mTableName, mCreateSQL, mSelectSQL;
    public static String table_name = "bill_info";

    public BillDBHelper(Context context) {
        super(context, db_name, null, 1);
        mContext = context;
        mVersion = 1;
        mWriteDB = this.getWritableDatabase();
        mReadDB = this.getReadableDatabase();
        mTableName = table_name;
        mSelectSQL = String.format("select rowid,_id,date,month,type,amount,descb,create_time,update_time from %s where "
                , mTableName);
    }

    //创建数据库
    @OptIn(markerClass = UnstableApi.class)
    public void onCreate(SQLiteDatabase db) {
        mCreateSQL = "create table if not exists bill_info " +
                "(_id INTEGER primary KEY autoincrement not null," + //自动生成每条记录的id
                "date varchar not null," + "month integer not null," +
                "type integer not null,"
                + "amount double not null, descb varchar not null," //改用descb
                + "create_time varchar not null," + "update_time varchar not null" + ");";
        Log.d(TAG, "create table");
        db.execSQL(mCreateSQL);

    }

    public static BillDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new BillDBHelper(context);

        }
        return mHelper;
    }
    //数据库插入操作
    public long insert(BillInfo info) {
        List<BillInfo> infoList = new ArrayList<>();
        infoList.add(info);
        return insert(infoList);//函数重载
    }

    @OptIn(markerClass = UnstableApi.class)
    public long insert(List<BillInfo> infoList) {
        long result = -1;
        for (int i = 0; i < infoList.size(); i++) {
            BillInfo info = infoList.get(i);
            List<BillInfo> tempList = new ArrayList<>();
            //不能存在重复记录，线判断，后添加
            ContentValues cv = new ContentValues();
            cv.put("date", info.date);
            cv.put("month", info.month);
            cv.put("type", info.type);
            cv.put("amount", info.amount);
            cv.put("descb", info.descb);
            cv.put("create_time", info.create_time);
            cv.put("update_time", info.update_time);
            cv.put("month", info.month);
            Log.d(TAG, "month=" + info.month);
            result = mWriteDB.insert(mTableName, "", cv);
            if (result == -1) return result;

        }
        Log.d(TAG, "result=" + result);
        return result;
    }

    public void save(BillInfo bill) {
        List<BillInfo> bill_list = (List<BillInfo>) queryById(bill.xuhao);
        BillInfo info = null;
        if (bill_list.size() > 0) {
            info = bill_list.get(0);
        }
        if (info != null) {
            bill.rowid = info.rowid;
            bill.create_time = info.create_time;
            bill.update_time = DateUtil.getNowDateTime("");
            update(bill);
        } else {
            bill.create_time = DateUtil.getNowDateTime("");
            insert(bill);
        }
    }

    //数据库更新操作
    public int update(BillInfo info) {
        return update(info, "rowid=" + info.rowid+";");
    }

    public int update(BillInfo info, String condition) {
        ContentValues cv = new ContentValues();
        cv.put("date", info.date);
        cv.put("month", info.month);
        cv.put("type", info.type);
        cv.put("amount", info.amount);
        cv.put("descb", info.descb);
        cv.put("create_time", info.create_time);
        cv.put("update_time", info.update_time);
        // 执行更新记录动作，该语句返回更新的记录数量
        return mWriteDB.update(mTableName, cv, condition, null);
    }

    //数据库查询操作
    public Object queryById(int id) {
        String sql = "_id=" + id + ";";
        return query(sql);
    }

    @OptIn(markerClass = UnstableApi.class)
    public ArrayList<BillInfo> query(String condition) {
        String sql = mSelectSQL + condition;
        Log.d(TAG, "query sql:" + sql);
        ArrayList<BillInfo> infoList = new ArrayList<BillInfo>();
        Cursor cursor = mReadDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BillInfo info = new BillInfo();
            info.rowid = cursor.getLong(0);
            info.xuhao = cursor.getInt(1);
            info.date = cursor.getString(2);
            info.month = cursor.getInt(3); // 取出整型数
            info.type = cursor.getInt(4); // 取出整型数
            info.amount = cursor.getDouble(5); // 取出双精度数
            info.descb = cursor.getString(6); // 取出字符串
            info.create_time = cursor.getString(7); // 取出字符串
            info.update_time = cursor.getString(8); // 取出字符串
            infoList.add(info);
        }
        cursor.close();
        Log.d(TAG, "infoList.size = " + infoList.size());
        return infoList;

    }

    public ArrayList<BillInfo> queryByMonth(int month) {
        return query("  month="+ month+" order by date asc");
    }

    //删除操作
    @OptIn(markerClass = UnstableApi.class)
    public void delete(int id) {
        String delete_sql = String.format("delete from %s where _id=%d;", mTableName, id);
        Log.d(TAG, "delete sql");
        mWriteDB.execSQL(delete_sql);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
