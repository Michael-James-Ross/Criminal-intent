package com.onyshchenko.artem.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

import com.onyshchenko.artem.criminalintent.model.Crime;
import com.onyshchenko.artem.criminalintent.model.CrimeLab;

public class CrimeListFragment extends Fragment {
    private String TAG = "CrimeListFragment";
    private String UPDATED_CRIME_POSITION_KEY = "com.artem.onyshchenko.updated_crime_position";
    private String SUBTITLE_VISIBLE_KEY = "SUBTITLE_VISIBLE_KEY";
    private RecyclerView crimeRecyclerView;
    private CrimeAdapter adapter;
    private int updatedCrimePosition = -1;
    private boolean subtitleVisible = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeRecyclerView = (RecyclerView)v.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null) {
            updatedCrimePosition = savedInstanceState.getInt(UPDATED_CRIME_POSITION_KEY, -1);
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_KEY);
        }
        updateUI(updatedCrimePosition);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(UPDATED_CRIME_POSITION_KEY, updatedCrimePosition);
        savedInstanceState.putBoolean(SUBTITLE_VISIBLE_KEY, subtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(updatedCrimePosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menuItemShowSubtitle);
        if(subtitleItem != null) {
            if(subtitleVisible) {
                subtitleItem.setTitle(R.string.hide_subtitle);
            } else {
                subtitleItem.setTitle(R.string.show_subtitle);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItemNewCrime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;

            case R.id.menuItemShowSubtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimesSize = crimeLab.getCrimes().size();
        String subtitleText = getResources().getQuantityString(R.plurals.subtitle_plural, crimesSize, crimesSize);
        if(!subtitleVisible) {
            subtitleText = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitleText);
    }

    private void updateUI(int position) {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(adapter == null) {
            adapter = new CrimeAdapter(crimes);
            crimeRecyclerView.setAdapter(adapter);
        } else {
            if(position != -1) {
                adapter.setCrimes(crimes);
                adapter.notifyItemChanged(position);
            } else {
                adapter.setCrimes(crimes);
                adapter.notifyDataSetChanged();
            }
        }
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements SearchView.OnClickListener{
        private Crime crime;
        private TextView titleTxtView;
        private TextView dateTxtView;
        private CheckBox solvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTxtView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            dateTxtView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            solvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        private void bindCrime(Crime crime) {
            this.crime = crime;
            titleTxtView.setText(crime.getTitle());
            dateTxtView.setText(crime.getDate().toString());
            solvedCheckBox.setChecked(crime.isSolved());
        }

        @Override
        public void onClick(View v) {
            updatedCrimePosition = this.getAdapterPosition();
            startActivity(CrimePagerActivity.newIntent(getActivity(), crime.getId()));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> crimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.crimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = crimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            this.crimes = crimes;
        }
    }
}
