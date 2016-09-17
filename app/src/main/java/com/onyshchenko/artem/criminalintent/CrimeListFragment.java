package com.onyshchenko.artem.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

import model.Crime;
import model.CrimeLab;

public class CrimeListFragment extends Fragment {
    private String TAG = "CrimeListFragment";
    private String UPDATED_CRIME_POSITION_KEY = "com.artem.onyshchenko.updated_crime_position";
    private RecyclerView crimeRecyclerView;
    private CrimeAdapter adapter;
    private int updatedCrimePosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeRecyclerView = (RecyclerView)v.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null) {
            updatedCrimePosition = savedInstanceState.getInt(UPDATED_CRIME_POSITION_KEY, -1);
        }
        updateUI(updatedCrimePosition);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(UPDATED_CRIME_POSITION_KEY, updatedCrimePosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(updatedCrimePosition);
    }

    private void updateUI(int position) {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(adapter == null) {
            adapter = new CrimeAdapter(crimes);
            crimeRecyclerView.setAdapter(adapter);
        } else {
            if(position != -1) {
                adapter.notifyItemChanged(position);
            }
        }
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
            startActivity(CrimeActivity.newIntent(getActivity(), crime.getId()));
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
    }
}
