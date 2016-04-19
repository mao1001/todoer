package edu.uw.mao1001.todoer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nick on 4/19/2016.
 */
public class NewTaskFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "NewTaskFragment";

    private static TextView deadlineField;
    private static Calendar deadline;
    private static Calendar completed;
    private static Calendar created;



    static final int DIALOG_ID = 0;

    public NewTaskFragment() {
        //Required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

        deadline = Calendar.getInstance();
        initializeDatePicker(rootView);


        return rootView;
    }

    private void initializeDatePicker(View rootView) {
        deadlineField = (TextView)rootView.findViewById(R.id.input_deadline);

        deadlineField.setText(getFormattedDate(deadline));

        deadlineField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(NewTaskFragment.this, NewTaskFragment.deadline);
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        deadline.set(year, monthOfYear, dayOfMonth);
        deadlineField.setText(getFormattedDate(deadline));
    }


    private String getFormattedDate(Calendar date) {
        return new SimpleDateFormat("EEE, MMM d, y").format(date.getTime());
    }
}

