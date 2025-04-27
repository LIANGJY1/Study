package com.example.study.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.study.database.greendao.DaoMaster;
import com.example.study.database.greendao.StudentDao;

import org.greenrobot.greendao.database.Database;

public class DBHelper extends DaoMaster.OpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database database, boolean ifNotExists) {
                DaoMaster.createAllTables(database, ifNotExists);

            }

            @Override
            public void onDropAllTables(Database database, boolean ifExists) {
                DaoMaster.dropAllTables(database, ifExists);
                dropMiniAppTable(database, ifExists);
            }
        }, StudentDao.class);
    }

    private void dropMiniAppTable(@NonNull Database db, boolean ifExists){
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MINI_APP_HISTORY_T\"";
        db.execSQL(sql);
    }
}
