package com.onyshchenko.artem.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragnment() {
        return new CrimeListFragment();
    }
}
