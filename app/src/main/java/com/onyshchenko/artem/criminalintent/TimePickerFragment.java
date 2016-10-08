package com.onyshchenko.artem.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static  final String EXTRA_TIME = "com.artem.onyshchenko.extra_time";
    private static final String ARG_TIME = "arg_time";

    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, date);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        final TimePicker timePicker = (TimePicker)view.findViewById(R.id.dialogTimePicker);
        Date date = (Date)getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        } else {
            //timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle("Time of crime")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new Date();
                        date.setHours(timePicker.getCurrentHour());
                        date.setMinutes(timePicker.getCurrentMinute());
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode ,Date date) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
