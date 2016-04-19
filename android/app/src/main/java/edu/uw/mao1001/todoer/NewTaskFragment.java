package edu.uw.mao1001.todoer;

import android.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nick on 4/19/2016.
 */
public class NewTaskFragment extends Fragment {
    private static final String TAG = "NewTaskFragment";



    static final int DIALOG_ID = 0;

    public NewTaskFragment() {
        //Required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

        initializeDatePicker(rootView);


        return rootView;
    }

    private void initializeDatePicker(View rootView) {
        TextView editText = (TextView)rootView.findViewById(R.id.input_deadline);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String result = "Year: " + year + " Month: " + month + " Day:" + day;

        editText.setText(result);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }

}

