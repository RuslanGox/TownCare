package com.example.ruslan.towncare.Models.Case;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 15-Jun-17.
 */

public class CaseSql {
    // Tables Name
    public static final String CASE_TABLE = "Cases";

    // Columns Names
    private static final String CASE_ID = "caseid";
    private static final String CASE_TITLE = "casetitle";
    private static final String CASE_DATE = "casedate";
    private static final String CASE_LIKE_COUNT = "caselikecount";
    private static final String CASE_TYPE = "casetype";
    private static final String CASE_STATUS = "casestatus";
    private static final String CASE_OPENER_PHONE = "caseopenerphone";
    private static final String CASE_OPENER_ID = "caseopenerid";
    private static final String CASE_TOWN = "casetown";
    private static final String CASE_ADDRESS = "caseaddress";
    private static final String CASE_DESCRIPTION = "casedescription";
    private static final String CASE_IMAGE_URL = "caseimageurl";

    // Queries
    private static final String WHERE_CASE_ID = "caseid=?";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CASE_TABLE + "(" +
                CASE_ID + " TEXT PRIMARY KEY, " +
                CASE_TITLE + " TEXT, " +
                CASE_DATE + " DATE, " +
                CASE_LIKE_COUNT + " NUMBER, " +
                CASE_TYPE + " TEXT, " +
                CASE_STATUS + " TEXT, " +
                CASE_OPENER_PHONE + " TEXT, " +
                CASE_OPENER_ID + " NUMBER, " +
                CASE_TOWN + " NUMBER, " +
                CASE_ADDRESS + " TEXT, " +
                CASE_DESCRIPTION + " TEXT, " +
                CASE_IMAGE_URL + " TEXT);");
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + CASE_TABLE);
        onCreate(db);
    }


    public static List<Case> getData(SQLiteDatabase db) {
        Cursor cursor = db.query(CASE_TABLE, null, null, null, null, null, null);
        List<Case> caseList = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                caseList.add(getCaseForSql(cursor));
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return caseList;
        }
        cursor.close();
        return caseList;
    }

    public static boolean addCase(SQLiteDatabase db, Case aCase) {
        Cursor cursor = db.query(CASE_TABLE, null, WHERE_CASE_ID, new String[]{aCase.getCaseId()}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        db.insert(CASE_TABLE, CASE_ID, getCaseValues(aCase));
        cursor.close();
        return true;
    }

    public static boolean updateCase(SQLiteDatabase db, Case aCase) {
        Cursor cursor = db.query(CASE_TABLE, null, WHERE_CASE_ID, new String[]{aCase.getCaseId()}, null, null, null);
        if (cursor.moveToFirst()) {
            db.update(CASE_TABLE, getCaseValues(aCase), WHERE_CASE_ID, new String[]{aCase.getCaseId()});
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static void removeCase(SQLiteDatabase db, String caseId) {
        db.delete(CASE_TABLE, WHERE_CASE_ID, new String[]{caseId});
    }

    public static Case getCase(SQLiteDatabase db, String caseId) {
        Cursor cursor = db.query(CASE_TABLE, null, WHERE_CASE_ID, new String[]{caseId}, null, null, null);
        if (cursor.moveToFirst()) {
            Case gCase = getCaseForSql(cursor);
            cursor.close();
            return gCase;
        } else {
            cursor.close();
            return null;
        }
    }

    /* --- Private Methods --- */

    private static ContentValues getCaseValues(Case aCase) {
        ContentValues values = new ContentValues();
        values.put(CASE_ID, aCase.getCaseId());
        values.put(CASE_TITLE, aCase.getCaseTitle());
        values.put(CASE_DATE, aCase.getCaseDate());
        values.put(CASE_LIKE_COUNT, aCase.getCaseLikeCount());
        values.put(CASE_TYPE, aCase.getCaseType());
        values.put(CASE_STATUS, aCase.getCaseStatus());
        values.put(CASE_OPENER_PHONE, aCase.getCaseOpenerPhone());
        values.put(CASE_OPENER_ID, aCase.getCaseOpenerId());
        values.put(CASE_TOWN, aCase.getCaseTown());
        values.put(CASE_ADDRESS, aCase.getCaseAddress());
        values.put(CASE_DESCRIPTION, aCase.getCaseDesc());
        values.put(CASE_IMAGE_URL, aCase.getCaseImageUrl());
        return values;
    }

    private static Case getCaseForSql(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(CASE_ID);
        int caseTitleIndex = cursor.getColumnIndex(CASE_TITLE);
        int caseDateIndex = cursor.getColumnIndex(CASE_DATE);
        int caseLikeCountIndex = cursor.getColumnIndex(CASE_LIKE_COUNT);
        int caseTypeIndex = cursor.getColumnIndex(CASE_TYPE);
        int caseStatusIndex = cursor.getColumnIndex(CASE_STATUS);
        int caseOpenerPhoneIndex = cursor.getColumnIndex(CASE_OPENER_PHONE);
        int caseOpenerIdIndex = cursor.getColumnIndex(CASE_OPENER_ID);
        int caseTownIndex = cursor.getColumnIndex(CASE_TOWN);
        int caseAddressIndex = cursor.getColumnIndex(CASE_ADDRESS);
        int caseDescriptionIndex = cursor.getColumnIndex(CASE_DESCRIPTION);
        int caseImageUrlIndex = cursor.getColumnIndex(CASE_IMAGE_URL);

        return new Case(cursor.getString(idIndex),
                cursor.getString(caseTitleIndex),
                cursor.getString(caseDateIndex),
                Integer.parseInt(cursor.getString(caseLikeCountIndex)),
                cursor.getString(caseTypeIndex),
                cursor.getString(caseStatusIndex),
                cursor.getString(caseOpenerPhoneIndex),
                cursor.getString(caseOpenerIdIndex),
                cursor.getString(caseTownIndex),
                cursor.getString(caseAddressIndex),
                cursor.getString(caseDescriptionIndex),
                cursor.getString(caseImageUrlIndex));
    }
}
