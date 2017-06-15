package com.example.ruslan.towncare.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by omrih on 15-Jun-17.
 */

public class ModelSql extends SQLiteOpenHelper {
    ModelSql(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CaseSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CaseSql.onUpgrade(db, oldVersion, newVersion);
    }
}
