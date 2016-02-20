package com.akhooo.coofde.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by vadivelansr on 2/11/2016.
 */
@Database(version = CoofdeDatabase.VERSION)
public class CoofdeDatabase {
    private static final String LOG_TAG = CoofdeDatabase.class.getSimpleName();
    private CoofdeDatabase(){}
    public static final int VERSION = 3;

    @Table(CoofdeColumns.class)public static final String COOFDE = "coofde";

    public static final String[] _MIGRATIONS = {
            "ALTER TABLE " + COOFDE + " ADD COLUMN url TEXT",
            "ALTER TABLE " + COOFDE + " ADD COLUMN store TEXT"
    };

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            String migration = _MIGRATIONS[i - 1];
            db.beginTransaction();
            try {
                db.execSQL(migration);
                db.setTransactionSuccessful();
            } catch (Exception e) {
               Log.d(LOG_TAG, "Error executing database migration: " + migration);
                break;
            } finally {
                db.endTransaction();
            }
        }
    }
}
