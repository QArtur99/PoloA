package com.artf.poloa.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ART_F on 2017-05-04.
 */

public final class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "PoloA.db"; // the name of our database
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.CoinMarketAnalysis.SQL_CREATE_MACD);
        insertData(db, "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.CoinMarketAnalysis.SQL_DELETE_MACD);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void insertData(SQLiteDatabase db, String data) {
        ContentValues mainValues = new ContentValues();
        mainValues.put(DatabaseContract.CoinMarketAnalysis.DATA, data);
        db.insert(DatabaseContract.CoinMarketAnalysis.TABLE_NAME, null, mainValues);
    }

    public void updateDatabase(SQLiteDatabase db, String data, String id) {
        ContentValues mainValues = new ContentValues();
        mainValues.put(DatabaseContract.CoinMarketAnalysis.DATA, data);
        db.update(DatabaseContract.CoinMarketAnalysis.TABLE_NAME, mainValues, DatabaseContract.CoinMarketAnalysis._ID + "=" + id, null);
        db.close();
    }


}
