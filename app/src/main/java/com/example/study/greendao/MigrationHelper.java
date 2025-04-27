package com.example.study.greendao;

import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.liang.log.MLog;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MigrationHelper {

    private final static String TAG = "MigrationHelper, ";

    public static WeakReference<ReCreateAllTableListener> reCreateAllTableListenerWeakReference;

    public interface ReCreateAllTableListener {
        void onCreateAllTables(Database database, boolean ifNotExists);
        void onDropAllTables(Database database, boolean ifExists);
    }

    @SafeVarargs
    public static void migrate(Database database, ReCreateAllTableListener reCreateAllTableListener, Class<? extends AbstractDao<?, ?>>... daoClass) {
        reCreateAllTableListenerWeakReference = new WeakReference<>(reCreateAllTableListener);
        migrate(database, daoClass);
    }

    private static void migrate(Database database, Class<? extends AbstractDao<?,?>>[] daoClass) {
        // 生成临时 table
        MLog.i(TAG + "generateTempTables start");
        generateTempTables(database, daoClass);
        MLog.i(TAG + "generateTempTables end");

        ReCreateAllTableListener reCreateAllTableListener = reCreateAllTableListenerWeakReference.get();
        if (reCreateAllTableListener != null) {
            MLog.i(TAG + "onDropAllTables");
            reCreateAllTableListener.onDropAllTables(database, true);
            MLog.i(TAG + "onCreateAllTables");
            reCreateAllTableListener.onCreateAllTables(database, false);
        } else {
//            dropAllTables();
//            createAllTables();
        }

        MLog.i(TAG + "restoreData start");
        restoreData(database, daoClass);
        MLog.i(TAG + "restoreData end");
    }

    private static void restoreData(Database database, Class<? extends AbstractDao<?,?>>[] daoClass) {
        // 遍历所有需要处理的 DAO 类（对应数据库表）
        for (int i = 0; i < daoClass.length; i++) {
            // 创建 DAO 配置对象，关联数据库和 DAO 类
            DaoConfig daoConfig = new DaoConfig(database, daoClass[i]);
            // 获取原始表名（如 "user"）
            String tableName = daoConfig.tablename;
            // 生成临时表名（如 "user_TEMP"）
            String tempTableName = daoConfig.tablename.concat("_TEMP");

            // 检查临时表是否存在（第二个参数 true 表示检查临时表）
            if (!isTableExists(database, tempTableName)) {
                continue; // 临时表不存在，跳过当前表的数据恢复
            }

            try {
                // 获取主表和临时表的列信息（TableInfo 包含列名、类型、是否非空等元数据）
                List<TableInfo> newTableInfos = TableInfo.getTableInfo(database, tableName);       // 主表列信息
                List<TableInfo> tempTableInfos = TableInfo.getTableInfo(database, tempTableName); // 临时表列信息

                // 准备 SELECT 和 INSERT 的列列表（用于构建 SQL 语句）
                ArrayList<String> selectColumns = new ArrayList<>(newTableInfos.size()); // SELECT 的列
                ArrayList<String> intoColumns = new ArrayList<>(newTableInfos.size());   // INSERT 的列

                // 第一阶段：匹配主表和临时表共有的列
                for (TableInfo tableInfo : tempTableInfos) {
                    if (newTableInfos.contains(tableInfo)) { // 列存在于主表中
                        String column = '`' + tableInfo.name + '`'; // 添加反引号防止 SQL 注入
                        intoColumns.add(column);  // 添加到 INSERT 列列表
                        selectColumns.add(column); // 添加到 SELECT 列列表
                    }
                }

                // 第二阶段：处理主表新增的非空列（临时表中没有的列）
                for (TableInfo tableInfo : newTableInfos) {
                    if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) { // 新增的非空列
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column); // 添加到 INSERT 列列表

                        // 构建默认值（如果列有默认值则用默认值，否则用空字符串）
                        String value;
                        if (tableInfo.dfltValue != null) {
                            value = "'" + tableInfo.dfltValue + "' AS "; // 例如：'default_value' AS column_name
                        } else {
                            value = "'' AS "; // 例如：'' AS column_name（空字符串）
                        }
                        selectColumns.add(value + column); // 添加到 SELECT 列列表
                    }
                }

                // 如果需要恢复的列不为空，构建并执行 REPLACE INTO 语句
                if (intoColumns.size() != 0) {
                    StringBuilder insertTableStringBuilder = new StringBuilder();
                    insertTableStringBuilder.append("REPLACE INTO `").append(tableName).append("` ("); // REPLACE INTO 主表
                    insertTableStringBuilder.append(TextUtils.join(",", intoColumns)); // 列列表（如 `id`,`name`）
                    insertTableStringBuilder.append(") SELECT ");
                    insertTableStringBuilder.append(TextUtils.join(",", selectColumns)); // SELECT 列列表（如 `id`,`name` 或 'default' AS `new_column`）
                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";"); // 从临时表选择数据

                    // 执行 SQL 语句（将临时表数据恢复到主表）
                    database.execSQL(insertTableStringBuilder.toString());
                    MLog.i(TAG + "【Restore data】 to " + tableName); // 打印日志
                }

                // 删除临时表（数据已恢复，无需保留）
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
                database.execSQL(dropTableStringBuilder.toString());
                MLog.i(TAG + "【Drop temp table】" + tempTableName); // 打印日志
            } catch (SQLException e) {
                // 异常处理：打印错误日志
                MLog.i(TAG + "【Failed to restore data from temp table 】" + tempTableName + e);
            }
        }
    }

    public static void generateTempTables(Database database, Class<? extends AbstractDao<?,?>>... daoClass) {
        for (int i = 0; i < daoClass.length; i++) {
            String tempTableName = null;
            DaoConfig daoConfig = new DaoConfig(database, daoClass[i]);
            String tableName = daoConfig.tablename;
            MLog.i(TAG + "tableName: " + tableName);
            if (!isTableExists(database, tableName)) {
                MLog.i(TAG + "new table, no need temp table");
                continue;
            }
            tempTableName = tableName.concat("_TEMP");
            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
            database.execSQL(dropTableStringBuilder.toString());

            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
            insertTableStringBuilder.append(" AS SELECT * FROM `").append(tableName).append("`;");
            database.execSQL(insertTableStringBuilder.toString());
            MLog.i(TAG + "【Table】" + tableName +"\n ---Columns-->"+getColumnsStr(daoConfig));
            MLog.i(TAG + "【Generate temp table】" + tempTableName);

            
        }
    }

    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daoConfig.allColumns.length; i++) {
            builder.append(daoConfig.allColumns[i]);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static boolean isTableExists(Database database, String tableName) {
        String sql = "SELECT COUNT(*) FROM `" + "sqlite_master" + "` WHERE type = ? AND name = ?";
        int count;
        Cursor cursor;
        cursor = database.rawQuery(sql, new String[]{"table", tableName});
        if (cursor == null || !cursor.moveToFirst()) {
            return false;
        }
        count = cursor.getInt(0);
        MLog.i("count: " + count);
        return count > 0;
    }

    private static class TableInfo {
        int cid;
        String name;
        String type;
        boolean notnull;
        String dfltValue;
        boolean pk;

        @Override
        public boolean equals(Object o) {
            return this == o
                    || o != null
                    && getClass() == o.getClass()
                    && name.equals(((TableInfo) o).name);
        }

        @NonNull
        @Override
        public String toString() {
            return "TableInfo{" +
                    "cid=" + cid +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", notnull=" + notnull +
                    ", dfltValue='" + dfltValue + '\'' +
                    ", pk=" + pk +
                    '}';
        }

        private static List<TableInfo> getTableInfo(Database db, String tableName) {
            String sql = "PRAGMA table_info(`" + tableName + "`)";
            MLog.i(sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor == null)
                return new ArrayList<>();

            TableInfo tableInfo;
            List<TableInfo> tableInfos = new ArrayList<>();
            while (cursor.moveToNext()) {
                tableInfo = new TableInfo();
                tableInfo.cid = cursor.getInt(0);
                tableInfo.name = cursor.getString(1);
                tableInfo.type = cursor.getString(2);
                tableInfo.notnull = cursor.getInt(3) == 1;
                tableInfo.dfltValue = cursor.getString(4);
                tableInfo.pk = cursor.getInt(5) == 1;
                tableInfos.add(tableInfo);
                // printLog(tableName + "：" + tableInfo);
            }
            cursor.close();
            return tableInfos;
        }
    }


}
