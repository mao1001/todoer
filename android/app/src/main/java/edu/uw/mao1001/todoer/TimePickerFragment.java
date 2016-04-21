package edu.uw.mao1001.todoer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import java.util.Calendar;

/**
 * Created by Nick on 4/21/2016.
 */
public class TimePickerFragment extends DialogFragment {

    private static final String TAG = "DatePickerFragment";
    private TimePickerDialog.OnTimeSetListener listener;
    private Calendar date;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    /**
     * Required blank constructor
     */
    public TimePickerFragment() {}

    /**
     * Returns an instantiated fragment. This is preferable tot he blank fragment.
     * @param listener Listener for time picker
     * @param date Date to start display with
     * @return An instance of this class instantiated.
     */
    public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener, Calendar date) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.listener = listener;
        fragment.date = date;
        return fragment;
    }

    //-----------------------------------------//
    //   F R A G M E N T   O V E R R I D E S   //
    //-----------------------------------------//

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
    }
}
