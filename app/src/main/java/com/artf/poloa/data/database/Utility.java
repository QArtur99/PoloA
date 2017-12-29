package com.artf.poloa.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class Utility {

    public static HashMap<String, TradeObject> loadHashMap(Context context) {
        HashMap<String, TradeObject> ccMap = Settings.Trade.CC_LIST;
        SQLiteOpenHelper mainDatabase = new DatabaseHelper(context);
        SQLiteDatabase db = mainDatabase.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(DatabaseContract.CoinMarketAnalysis.TABLE_NAME, DatabaseContract.CoinMarketAnalysis.projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
          ///  id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CoinMarketAnalysis._ID));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CoinMarketAnalysis.DATA));
            if (data.length() > 0) {
                Type type = new TypeToken<HashMap<String, TradeObject>>() {}.getType();
                ccMap = new Gson().fromJson(data, type);

            }
        }
        return ccMap;
    }

    public static void updateDatabase(Context context, String data) {
        DatabaseHelper mainDatabase = new DatabaseHelper(context);
        SQLiteDatabase db = mainDatabase.getWritableDatabase();
        mainDatabase.updateDatabase(db, data, "0");
        db.close();
    }

}
