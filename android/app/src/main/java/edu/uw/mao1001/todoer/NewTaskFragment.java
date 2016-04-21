package edu.uw.mao1001.todoer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;
/**
 * Created by Nick on 4/19/2016.
 */
public class NewTaskFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "NewTaskFragment";

    private static TextView deadlineField;

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

        Button btn = (Button)rootView.findViewById(R.id.save_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    saveTask();
                }
            }
        });

        deadline = Calendar.getInstance();
        initializeDatePicker(rootView);

        return rootView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        deadline.set(year, monthOfYear, dayOfMonth);
        deadlineField.setText(TodoItem.getFormattedDate(deadline));
    }

    //-----------------------------------//
    //   P R I V A T E   M E T H O D S   //
    //-----------------------------------//

    private void exit(String newId) {
        int id = ((ViewGroup)getView().getParent()).getId();
        Fragment fragment;
        fragment = DetailFragment.newInstance(newId);
        if (id == R.id.container) {
            //If in portrait. Replace self with the task list again
            getFragmentManager().beginTransaction().replace(R.id.container, fragment, "DetailFragment").commit();
        } else if (id == R.id.right_pane) {
            //If in landscape. Reset the fields.
            getFragmentManager().beginTransaction().replace(R.id.right_pane, fragment, "DetailFragment").commit();
        }
    }

    private void initializeDatePicker(View rootView) {
        deadlineField = (TextView)rootView.findViewById(R.id.input_deadline);

        deadlineField.setText(TodoItem.getFormattedDate(deadline));

        deadlineField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(NewTaskFragment.this, NewTaskFragment.deadline);
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }

    private void saveTask() {
        ContentValues newValues = new ContentValues();

        created = Calendar.getInstance();

        Log.v(TAG, "Deadline:" + TodoItem.getFormattedDate(deadline));
        Log.v(TAG, "Created :" + TodoItem.getFormattedDate(created));

        newValues.put(TodoListProvider.TaskEntry.COL_TITLE, title);
        newValues.put(TodoListProvider.TaskEntry.COL_DETAILS, details);
        newValues.put(TodoListProvider.TaskEntry.COL_DEADLINE, TodoItem.getFormattedDate(deadline));
        newValues.put(TodoListProvider.TaskEntry.COL_TIME_CREATED, TodoItem.getFormattedDate(created));


        Uri uri = getActivity().getContentResolver().insert(
                TodoListProvider.CONTENT_URI,
                newValues
        );

        Context context = getContext();
        Toast.makeText(context, context.getString(R.string.toast_created_task), Toast.LENGTH_SHORT).show();

        exit(uri.getPathSegments().get(1));
    }

    private boolean validate() {
        EditText titleField = (EditText)getView().findViewById(R.id.input_title);
        title = titleField.getText().toString();

        EditText detailField = (EditText)getView().findViewById(R.id.input_detail);
        details = detailField.getText().toString();

        if (details.equals("") || title.equals("")) {
            return false;
        } else {
            return true;
        }
    }
}

