package edu.uw.mao1001.todoer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import java.util.Calendar;

/**
 * Created by Nick on 4/19/2016.
 */
public class DatePickerFragment extends DialogFragment {

    private static final String TAG = "DatePickerFragment";
    private Calendar date;
    private DatePickerDialog.OnDateSetListener listener;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    /**
     * Required blank constructor
     */
    public DatePickerFragment() {}

    /**
     * Returns an instantiated fragment. This is preferable tot he blank fragment.
     * @param listener Listener for date picker
     * @param date Date to start display with
     * @return An instance of this class instantiated.
     */
    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener, Calendar date) {
        DatePickerFragment fragment = new DatePickerFragment();
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
        return new DatePickerDialog(getActivity(), listener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
    }
}
