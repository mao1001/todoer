package edu.uw.mao1001.todoer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Created by Nick on 4/19/2016.
 */
public class DatePickerFragment extends DialogFragment {

    private static final String TAG = "DatePickerFragment";

    Calendar date;
    DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment() {
    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener, Calendar date) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.listener = listener;
        fragment.date = date;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
    }
}
