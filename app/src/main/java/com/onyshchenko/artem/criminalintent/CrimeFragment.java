package com.onyshchenko.artem.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

import com.onyshchenko.artem.criminalintent.model.Crime;
import com.onyshchenko.artem.criminalintent.model.CrimeLab;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private Crime crime;
    private EditText crimeTitleEditTxt;
    private Button crimeDateBtn;
    private Button crimeTimeBtn;
    private CheckBox isSolvedCheckBox;
    private final String DIALOG_DATE = "dialog_date";
    private final String DIALOG_TIME = "dialog_time";
    private final int REQUEST_DATE = 0;
    private final int REQUEST_TIME = 1;

    @Override
    public void onCreate(Bundle saveInstatnceState) {
        super.onCreate(saveInstatnceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuItemDeleteCrime:
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.deleteCrime(crime.getId());
                Intent intent = new Intent(getContext(), CrimeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            updateDate();
        }
        crimeTimeBtn = (Button)v.findViewById(R.id.crimeTimeBtn);
        if(crimeTimeBtn != null) {
            updateTime();
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

        if(crimeTimeBtn != null) {
            crimeTimeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(crime.getDate());
                    FragmentManager fragmentManager = getFragmentManager();
                    timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    timePickerFragment.show(fragmentManager, DIALOG_TIME);
                }
            });
        }

        if(crimeDateBtn != null) {
            crimeDateBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(crime.getDate());
                    datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    datePickerFragment.show(fragmentManager, DIALOG_DATE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_DATE) {
            if(data != null) {
                Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                crime.setDate(date);
                updateDate();
            }
        }

        if(requestCode == REQUEST_TIME) {
            if(data != null) {
                Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                crime.setDate(date);
                updateTime();
            }
        }
    }

    private void updateDate() {
        crimeDateBtn.setText(new DateFormat().getLongDateFormat(getActivity()).format(crime.getDate()));
    }

    private void updateTime() {
        crimeTimeBtn.setText(new DateFormat().getTimeFormat(getActivity()).format(crime.getDate()));
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getContext()).updateCrime(crime);
    }
}
