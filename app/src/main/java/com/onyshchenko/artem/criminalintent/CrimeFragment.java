package com.onyshchenko.artem.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import model.Crime;

public class CrimeFragment extends Fragment {
    private Crime crime;
    private EditText crimeTitleEditTxt;

    @Override
    public void onCreate(Bundle saveInstatnceState) {
        super.onCreate(saveInstatnceState);
        crime = new Crime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        gatherWidgets(v);
        setEventListenres();
        return v;
    }

    public void gatherWidgets(View v) {
        crimeTitleEditTxt = (EditText)v.findViewById(R.id.crimeTitleEditTxt);
    }

    public void setEventListenres() {
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
}
