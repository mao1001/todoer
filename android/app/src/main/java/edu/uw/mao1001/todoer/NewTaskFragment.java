package edu.uw.mao1001.todoer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;
/**
 * Created by Nick on 4/19/2016.
 */
public class NewTaskFragment extends Fragment  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "NewTaskFragment";

    private static TextView deadlineField;
    private static TextView timeField;

    private static String title;
    private static String details;
    private static Calendar deadline;
    private static Calendar created;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    /**
     * Required blank constructor
     */
    public NewTaskFragment() {}

    //-----------------------------------------//
    //   F R A G M E N T   O V E R R I D E S   //
    //-----------------------------------------//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);
        getActivity().setTitle("New Task");

        deadline = Calendar.getInstance();
        initializeViews(rootView);

        return rootView;
    }

    /**
     * This override is for implementation of DatePickerDialog.OnDateSetListener
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        deadline.set(year, monthOfYear, dayOfMonth);
        deadlineField.setText(TodoItem.getFormattedDate(deadline));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        deadline.set(Calendar.HOUR_OF_DAY, hourOfDay);
        deadline.set(Calendar.MINUTE, minute);
        timeField.setText(TodoItem.getFormmattedTime(deadline));
    }

    //-----------------------------------//
    //   P R I V A T E   M E T H O D S   //
    //-----------------------------------//

    /**
     * Leaves this view and redirects the user to a detailed view
     * of the newly created task
     * @param newId Id of the detail view to exit to
     */
    private void exit(String newId) {
        Fragment fragment;
        int targetId = ((ViewGroup)getView().getParent()).getId();
        fragment = DetailFragment.newInstance(newId);
        getFragmentManager().beginTransaction().replace(targetId, fragment, "DetailFragment").commit();
    }

    /**
     * Initializes views and their click events
     * @param rootView
     */
    private void initializeViews(View rootView) {
        //Hooks up the datepicker
        deadlineField = (TextView)rootView.findViewById(R.id.input_deadline);
        deadlineField.setText(TodoItem.getFormattedDate(deadline));

        deadlineField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(NewTaskFragment.this, NewTaskFragment.deadline);
                newFragment.show(getActivity().getFragmentManager(), "DatePicker");
            }
        });

        //Initializes time picker
        timeField = (TextView)rootView.findViewById(R.id.input_time);
        timeField.setText(TodoItem.getFormmattedTime(deadline));
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = TimePickerFragment.newInstance(NewTaskFragment.this, NewTaskFragment.deadline);
                newFragment.show(getActivity().getFragmentManager(), "TimePicker");
            }
        });

        //Initializes button for the save button
        Button btn = (Button)rootView.findViewById(R.id.save_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    saveTask();
                } else {
                    Context context = getContext();
                    Toast.makeText(context, context.getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Saves the current state of the fragment into a new task
     * then redirects the user to a detailed view of the newly created task
     */
    private void saveTask() {
        created = Calendar.getInstance();

        ContentValues newValues = new ContentValues();
        newValues.put(TodoListProvider.TaskEntry.COL_TITLE, title);
        newValues.put(TodoListProvider.TaskEntry.COL_DETAILS, details);
        newValues.put(TodoListProvider.TaskEntry.COL_DEADLINE, TodoItem.getFormmatedFullDate(deadline));
        newValues.put(TodoListProvider.TaskEntry.COL_TIME_CREATED, TodoItem.getFormmatedFullDate(created));

        Uri uri = getActivity().getContentResolver().insert(
                TodoListProvider.CONTENT_URI,
                newValues
        );

        Context context = getContext();
        Toast.makeText(context, context.getString(R.string.toast_created_task), Toast.LENGTH_SHORT).show();

        // Closes keyboard upon completion
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        exit(uri.getPathSegments().get(1));
    }

    /**
     * Helper method to validate there is valid
     * input in the fields.
     * @return A boolean regarding the validity of inputs
     */
    private boolean validate() {
        EditText titleField = (EditText)getView().findViewById(R.id.input_title);
        title = titleField.getText().toString();

        EditText detailField = (EditText)getView().findViewById(R.id.input_detail);
        details = detailField.getText().toString();

        return !(details.equals("") || title.equals(""));
    }
}