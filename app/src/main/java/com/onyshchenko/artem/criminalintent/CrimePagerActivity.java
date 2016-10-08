package com.onyshchenko.artem.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import com.onyshchenko.artem.criminalintent.model.Crime;
import com.onyshchenko.artem.criminalintent.model.CrimeLab;

public class CrimePagerActivity extends AppCompatActivity {

    public static final String EXTRA_CRIME_ID = "com.artem.onyshchenko.crime_id";
    private ViewPager viewPager;
    private List<Crime> crimes;

    public static Intent newIntent(Context context, UUID id) {
        Intent i = new Intent(context, CrimePagerActivity.class);
        i.putExtra(EXTRA_CRIME_ID, id);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        viewPager = (ViewPager)findViewById(R.id.activityViewPager);
        crimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = crimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for(int i=0; i < crimes.size(); i++) {
            if(crimes.get(i).getId().equals(crimeId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }


}
