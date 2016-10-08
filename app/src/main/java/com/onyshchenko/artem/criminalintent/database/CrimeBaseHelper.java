package com.onyshchenko.artem.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command = "create table " + CrimeDbShema.CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeDbShema.CrimeTable.Cols.UUID + ", " +
                CrimeDbShema.CrimeTable.Cols.TITLE + ", " +
                CrimeDbShema.CrimeTable.Cols.DATE + ", " +
                CrimeDbShema.CrimeTable.Cols.SOLVED + ")";

        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
