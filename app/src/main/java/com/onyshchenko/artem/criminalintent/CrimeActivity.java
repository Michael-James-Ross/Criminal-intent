package com.onyshchenko.artem.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.artem.onyshchenko.crime_id";

    public static Intent newIntent(Context context, UUID id) {
        Intent i = new Intent(context, CrimeActivity.class);
        i.putExtra(EXTRA_CRIME_ID, id);
        return i;
    }

    @Override
    protected Fragment createFragnment() {
        return new CrimeFragment();
    }
}
