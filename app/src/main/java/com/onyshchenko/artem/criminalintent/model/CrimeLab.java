package com.onyshchenko.artem.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onyshchenko.artem.criminalintent.database.CrimeBaseHelper;
import com.onyshchenko.artem.criminalintent.database.CrimeCursorWrapper;
import com.onyshchenko.artem.criminalintent.database.CrimeDbShema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private Context context;
    private SQLiteDatabase database;

    private CrimeLab(Context context) {
        this.context = context.getApplicationContext();
        database = new CrimeBaseHelper(this.context).getWritableDatabase();
    }

    public static CrimeLab get(Context context) {
        if(crimeLab == null) {
            crimeLab = new CrimeLab(context);
            return crimeLab;
        }
        return crimeLab;
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        database.insert(CrimeDbShema.CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        database.update(CrimeDbShema.CrimeTable.NAME, contentValues, CrimeDbShema.CrimeTable.Cols.UUID + " = ?", new String [] {uuidString});
    }

    public void deleteCrime(UUID crimeId) {

    }

    public List<Crime> getCrimes() {
        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null, null);
        List<Crime> crimes = new ArrayList<>();
        try {
            crimeCursorWrapper.moveToFirst();
            while(!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(CrimeDbShema.CrimeTable.Cols.UUID + " = ?", new String [] { id.toString() });
        try {
            if(crimeCursorWrapper.getCount() == 0) {
                return null;
            }
            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getCrime();
        } finally {
            crimeCursorWrapper.close();
        }
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDbShema.CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeDbShema.CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeDbShema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeDbShema.CrimeTable.Cols.SOLVED, crime.isSolved());
        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause , String [] whereArgs ) {
        Cursor cursor = database.query(CrimeDbShema.CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
}
