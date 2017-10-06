/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author kapil.vij
 *         edited by Umesh
 */
public class GenericDB extends SQLiteOpenHelper {
    public static final ArrayList<String> DB_SQL_CREATE_TABLE_QUERIES = new ArrayList<String>();
    public static final ArrayList<String> DB_SQL_DROP_TABLE_QUERIES = new ArrayList<String>();
    public static final ArrayList<String> DB_SQL_UPGARDE_QUERIES = new ArrayList<String>();
    private String LOG_TAG = GenericDB.class.getSimpleName();

    public GenericDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "Tables creation start.");
        for (String table : DB_SQL_CREATE_TABLE_QUERIES) {
            db.execSQL(table);
        }
        Log.i(LOG_TAG, "Tables creation end.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        Log.i(LOG_TAG, "DB upgrade.");
        for (String table : DB_SQL_DROP_TABLE_QUERIES) {
            db.execSQL(table);
        }

        for (String table : DB_SQL_UPGARDE_QUERIES) {
            db.execSQL(table);
        }

        onCreate(db);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------

    public long insert(String table, ContentValues cv) {
        return getWritableDatabase().insert(table, null, cv);
    }

    public Cursor selectAll(String table) {
        return getReadableDatabase().rawQuery("SELECT * FROM " + table, null);
    }

    public Cursor select(String table, String[] columns, String whereClause, String[] selectionArgs, String orderBy) {
        return getReadableDatabase().query(table, columns, whereClause, selectionArgs, null, null, orderBy);
    }

    public Cursor selectMin(String table, String column, String whereClause) {
        return getReadableDatabase().rawQuery("SELECT MIN(" + column + ") FROM " + table + " WHERE " + whereClause, null);
    }

    public Cursor selectMax(String table, String column, String whereClause) {
        return getReadableDatabase().rawQuery("SELECT MAX(" + column + ") FROM " + table + " WHERE " + whereClause, null);
    }

    public Cursor selectDistinct(String table, String[] columns, String groupBy, String orderBy) {
        return getReadableDatabase().query(true, table, columns, null, null, groupBy, null, orderBy, null);
    }

    public int delete(String table, String whereClause, String[] selectionArgs) {
        return getWritableDatabase().delete(table, whereClause, selectionArgs);
    }

    public int update(String table, ContentValues values, String whereClause, String[] selectionArgs) {
        return getWritableDatabase().update(table, values,whereClause, selectionArgs);
    }
}
