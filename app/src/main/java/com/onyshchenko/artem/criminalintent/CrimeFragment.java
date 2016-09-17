package com.onyshchenko.artem.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import model.Crime;
import model.CrimeLab;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private Crime crime;
    private EditText crimeTitleEditTxt;
    private Button crimeDateBtn;
    private CheckBox isSolvedCheckBox;

    @Override
    public void onCreate(Bundle saveInstatnceState) {
        super.onCreate(saveInstatnceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        crime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        gatherWidgets(v);
        setEventListenres();
        return v;
    }

    public static CrimeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, id);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    public void gatherWidgets(View v) {
        crimeTitleEditTxt = (EditText)v.findViewById(R.id.crimeTitleEditTxt);
        if(crimeTitleEditTxt != null) {
            crimeTitleEditTxt.setText(crime.getTitle());
        }
        crimeDateBtn = (Button)v.findViewById(R.id.crimeDateBtn);
        if(crimeDateBtn != null) {
            crimeDateBtn.setText(crime.getDate().toString());
        }
        if(crimeDateBtn != null) {
            crimeDateBtn.setText(crime.getDate().toString());
            crimeDateBtn.setEnabled(false);
        }
        isSolvedCheckBox = (CheckBox)v.findViewById(R.id.isSolvedCheckBox);
        isSolvedCheckBox.setChecked(crime.isSolved());
    }

    public void setEventListenres() {
        if(crimeTitleEditTxt != null) {
            crimeTitleEditTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    crime.setTitle(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        if(isSolvedCheckBox != null) {
            isSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    crime.setSolved(isChecked);
                }
            });
        }
    }
}
