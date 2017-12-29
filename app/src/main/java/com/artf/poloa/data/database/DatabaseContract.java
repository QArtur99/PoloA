package com.artf.poloa.data.database;

import android.provider.BaseColumns;

/**
 * Created by ART_F on 2017-05-04.
 */

public class DatabaseContract {

    private DatabaseContract(){}

    public static class CoinMarketAnalysis implements BaseColumns {
        public static final String TABLE_NAME = "ccMap";
        public static final String DATA = "data";

        static final String SQL_CREATE_MACD =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + DATA + " TEXT"
                        + ");";

        static final String SQL_DELETE_MACD =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String[] projection = {
                _ID,
                DATA
        };
    }
}
