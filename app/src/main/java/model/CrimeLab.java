package model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private Context context;
    private List<Crime> crimes;

    private CrimeLab(Context context) {
        this.context = context;
        crimes = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setDate(new Date());
            crime.setSolved(i % 2 == 0);
            crimes.add(crime);
        }
    }

    public static CrimeLab get(Context context) {
        if(crimeLab == null) {
            crimeLab = new CrimeLab(context);
            return crimeLab;
        }
        return crimeLab;
    }

    public List<Crime> getCrimes() {
        return crimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime crime : crimes) {
            if(crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
