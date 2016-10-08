package com.onyshchenko.artem.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.onyshchenko.artem.criminalintent.model.Crime;

import java.sql.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper{

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeDbShema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbShema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbShema.CrimeTable.Cols.DATE));
        int solved = getInt(getColumnIndex(CrimeDbShema.CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setDate(new Date(date));
        crime.setTitle(title);
        crime.setSolved(solved != 0);

        return crime;
    }
}
