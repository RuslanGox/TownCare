package com.example.ruslan.towncare.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 15-Jun-17.
 */

public class CaseSql {
    private static final String CASE_TABLE = "Cases";
    private static final String CASE_ID = "caseid";
    private static final String CASE_TITLE = "casetitle";
    private static final String CASE_DATE = "casedate";
    private static final String CASE_LIKE_COUNT = "caselikecount";
    private static final String CASE_UNLIKE_COUNT = "caseunlikecount";
    private static final String CASE_TYPE = "casetype";
    private static final String CASE_STATUS = "casestatus";
    private static final String CASE_OPENER_PHONE = "caseopenerphone";
    private static final String CASE_OPENER_ID = "caseopenerid";
    private static final String CASE_ADDRESS = "caseaddress";
    private static final String CASE_DESCRIPTION = "casedescription";
    private static final String CASE_IMAGE_URL = "caseimageurl";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CASE_TABLE + "(" +
                CASE_ID + " TEXT PRIMARY KEY, " +
                CASE_TITLE + " TEXT, " +
                CASE_DATE + " DATE, " +
                CASE_LIKE_COUNT + " NUMBER, " +
                CASE_UNLIKE_COUNT + " NUMBER, " +
                CASE_TYPE + " TEXT, " +
                CASE_STATUS + " TEXT, " +
                CASE_OPENER_PHONE + " TEXT, " +
                CASE_OPENER_ID + " NUMBER, " +
                CASE_ADDRESS + " TEXT, " +
                CASE_DESCRIPTION + " TEXT, " +
                CASE_IMAGE_URL + " TEXT);");
//db.execSQL("create table CASEs (stid TEXT PRIMARY KEY, name TEXT, phone NUMBER, address TEXT, checked NUMBER, imageUrl TEXT, birthDate DATE, birthTime DATE");
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + CASE_TABLE);
        onCreate(db);
    }

    public static void addCase() {

    }

    public static List<Case> getData(SQLiteDatabase db) {
        Cursor c = db.query(CASE_TABLE, null, null, null, null, null, null);
        List<Case> caseList = new LinkedList<>();
        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(CASE_ID);
            int caseTitleIndex = c.getColumnIndex(CASE_TITLE);
            int caseDateIndex = c.getColumnIndex(CASE_DATE);
            int caseLikeCountIndex = c.getColumnIndex(CASE_LIKE_COUNT);
            int caseUnlikeCountIndex = c.getColumnIndex(CASE_UNLIKE_COUNT);
            int caseTypeIndex = c.getColumnIndex(CASE_TYPE);
            int caseStatusIndex = c.getColumnIndex(CASE_STATUS);
            int caseOpenerPhoneIndex = c.getColumnIndex(CASE_OPENER_PHONE);
            int caseOpenerIdIndex = c.getColumnIndex(CASE_OPENER_ID);
            int caseAddressindex = c.getColumnIndex(CASE_ADDRESS);
            int caseDescriptionIndex = c.getColumnIndex(CASE_DESCRIPTION);
            int caseImageUrlIndex = c.getColumnIndex(CASE_IMAGE_URL);
            do {
                caseList.add(new Case(c.getString(idIndex),
                        c.getString(caseTitleIndex),
                        c.getString(caseDateIndex),
                        Integer.parseInt(c.getString(caseLikeCountIndex)),
                        Integer.parseInt(c.getString(caseUnlikeCountIndex)),
                        c.getString(caseTypeIndex),
                        c.getString(caseStatusIndex),
                        c.getString(caseOpenerPhoneIndex),
                        c.getString(caseOpenerIdIndex),
                        c.getString(caseAddressindex),
                        c.getString(caseDescriptionIndex),
                        c.getString(caseImageUrlIndex)
                ));
            } while (c.moveToNext());
        } else {
            c.close();
            return caseList;
        }
        c.close();
        return caseList;
    }

    public static boolean addCase(SQLiteDatabase db, Case aCase) {
        Cursor cursor = db.query(CASE_TABLE, null, "caseid=?", new String[]{aCase.getCaseId()}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(CASE_ID, aCase.getCaseId());
        values.put(CASE_TITLE, aCase.getCaseTitle());
        values.put(CASE_DATE, aCase.getCaseDate());
        values.put(CASE_LIKE_COUNT, aCase.getCaseLikeCount());
        values.put(CASE_UNLIKE_COUNT, aCase.getCaseUnLikeCount());
        values.put(CASE_TYPE, aCase.getCaseType());
        values.put(CASE_STATUS, aCase.getCaseStatus());
        values.put(CASE_OPENER_PHONE, aCase.getCaseOpenerPhone());
        values.put(CASE_OPENER_ID, aCase.getCaseOpener());
        values.put(CASE_ADDRESS, aCase.getCaseAddress());
        values.put(CASE_DESCRIPTION, aCase.getCaseDesc());
        values.put(CASE_IMAGE_URL, aCase.getCaseImageUrl());
        db.insert(CASE_TABLE, CASE_ID, values);
        cursor.close();
        return true;
    }

    public static boolean updateCase(SQLiteDatabase db, Case aCase) {
        Cursor cursor = db.query(CASE_TABLE, null, "caseid=?", new String[]{aCase.getCaseId()}, null, null, null);
        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            // todo: check if we realy need to send Id
            values.put(CASE_ID, aCase.getCaseId());
            values.put(CASE_TITLE, aCase.getCaseTitle());
            values.put(CASE_DATE, aCase.getCaseDate());
            values.put(CASE_LIKE_COUNT, aCase.getCaseLikeCount());
            values.put(CASE_UNLIKE_COUNT, aCase.getCaseUnLikeCount());
            values.put(CASE_TYPE, aCase.getCaseType());
            values.put(CASE_STATUS, aCase.getCaseStatus());
            values.put(CASE_OPENER_PHONE, aCase.getCaseOpenerPhone());
            values.put(CASE_OPENER_ID, aCase.getCaseOpener());
            values.put(CASE_ADDRESS, aCase.getCaseAddress());
            values.put(CASE_DESCRIPTION, aCase.getCaseDesc());
            values.put(CASE_IMAGE_URL, aCase.getCaseImageUrl());
            db.update(CASE_TABLE, values, "caseid=?", new String[]{aCase.getCaseId()});
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static void removeCase(SQLiteDatabase db, String caseId) {
        db.delete(CASE_TABLE, "caseid=?", new String[]{caseId});
    }

    public static Case getCase(SQLiteDatabase db, String caseId) {
        Cursor c = db.query(CASE_TABLE, null, "caseid=?", new String[]{caseId}, null, null, null);
        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(CASE_ID);
            int caseTitleIndex = c.getColumnIndex(CASE_TITLE);
            int caseDateIndex = c.getColumnIndex(CASE_DATE);
            int caseLikeCountIndex = c.getColumnIndex(CASE_LIKE_COUNT);
            int caseUnlikeCountIndex = c.getColumnIndex(CASE_UNLIKE_COUNT);
            int caseTypeIndex = c.getColumnIndex(CASE_TYPE);
            int caseStatusIndex = c.getColumnIndex(CASE_STATUS);
            int caseOpenerPhoneIndex = c.getColumnIndex(CASE_OPENER_PHONE);
            int caseOpenerIdIndex = c.getColumnIndex(CASE_OPENER_ID);
            int caseAddressindex = c.getColumnIndex(CASE_ADDRESS);
            int caseDescriptionIndex = c.getColumnIndex(CASE_DESCRIPTION);
            int caseImageUrlIndex = c.getColumnIndex(CASE_IMAGE_URL);

            Case gCase = new Case(c.getString(idIndex),
                    c.getString(caseTitleIndex),
                    c.getString(caseDateIndex),
                    Integer.parseInt(c.getString(caseLikeCountIndex)),
                    Integer.parseInt(c.getString(caseUnlikeCountIndex)),
                    c.getString(caseTypeIndex),
                    c.getString(caseStatusIndex),
                    c.getString(caseOpenerPhoneIndex),
                    c.getString(caseOpenerIdIndex),
                    c.getString(caseAddressindex),
                    c.getString(caseDescriptionIndex),
                    c.getString(caseImageUrlIndex)
            );
            c.close();
            return gCase;
        } else {
            c.close();
            return null;
        }
    }


}
