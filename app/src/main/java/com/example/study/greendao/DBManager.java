package com.example.study.greendao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.example.study.Constant;
import com.example.study.database.greendao.DaoMaster;
import com.example.study.database.greendao.DaoSession;
import com.example.study.database.greendao.StudentDao;
import com.liang.log.MLog;

import org.greenrobot.greendao.database.Database;

import java.util.Arrays;

public class DBManager {
    private final String TAG = "DBManager, ";
    private static final DBManager INSTANCE = new DBManager();

    private DaoSession daoSession;

    private Database db;

    private DBManager() {

    }

    public static DBManager getInstance() {
        return INSTANCE;
    }

    public void initDBManager(Context context) {
        MLog.i(TAG + "initDBManager: ");
        DBHelper dbHelper = new DBHelper(context, Constant.DB_NAME, null);
        db = dbHelper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();// 获取实体对象
        MLog.i(TAG + "daoSession: " + daoSession.toString());
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private boolean isDaoSessionNull() {
        return daoSession == null;
    }

    public void insertStudent(Student student) {
        if (!isDaoSessionNull()) {
            daoSession.getStudentDao().insert(student);
        }
    }

    public void updateStudent(Student student) {
        if (!isDaoSessionNull()) {
            daoSession.getStudentDao().update(student);
        }
    }

    public boolean isTableExists(String tableName) {
        return MigrationHelper.isTableExists(db, tableName);
    }

    public void query() {
        MigrationHelper.generateTempTables(db, StudentDao.class);
    }
//    public void query() {
////        Cursor cursor = writableDatabase.query("category", null, null, null, null, null, null);
////        if (cursor.moveToFirst()) {
////            do{
////                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
////                Log.e("db", "id: " + id);
////            } while(cursor.moveToNext());
////        }
//
//        try {
//            // 查询所有用户表
//            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
////            MLog.i(TAG + "cursor: ", cursor.);
//
//            // 遍历结果集
//            if (cursor.moveToFirst()) {
//                do {
//                    @SuppressLint("Range") String tableName = cursor.getString(cursor.getColumnIndex("name"));
//                    MLog.i(TAG + "Tables: " + tableName);
//                } while (cursor.moveToNext());
//            } else {
//                MLog.i(TAG + "Tables: "  + "No tables found.");
//            }
//
//            cursor.close();
//        } finally {
////            db.close();
//        }
//
//
//        try {
//            // 获取 SQLiteDatabase 实例
////            SQLiteDatabase db = daoSession.getDatabase();
//
//            // 执行原始 SQL 查询
//            Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master", null);
//
//            // 打印表头
//            String[] columnNames = cursor.getColumnNames();
//            MLog.i(TAG + "SQLiteMaster" + "Columns: " + Arrays.toString(columnNames));
//
//            // 遍历结果集
//            if (cursor.moveToFirst()) {
//                do {
//                    // 提取各字段值
//                    String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
//                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                    String tblName = cursor.getString(cursor.getColumnIndexOrThrow("tbl_name"));
//                    String sql = cursor.getString(cursor.getColumnIndexOrThrow("sql"));
//
//                    // 打印结果（示例）
//                    MLog.i(TAG + "SQLiteMaster" + "\nType: " + type
//                            + "\nName: " + name
//                            + "\nTable: " + tblName
//                            + "\nSQL: " + sql);
//                } while (cursor.moveToNext());
//            } else {
//                MLog.i(TAG + "SQLiteMaster" + "No records found.");
//            }
//
//            cursor.close();
//        } finally {
//            // GreenDAO 会自动管理数据库连接，无需手动关闭
//        }
//    }
}
