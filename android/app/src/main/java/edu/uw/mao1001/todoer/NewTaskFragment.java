package edu.uw.mao1001.todoer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;
/**
 * Created by Nick on 4/19/2016.
 */
public class NewTaskFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "NewTaskFragment";

    private static TextView deadlineField;
    private static Calendar deadline;
    private static Calendar completed;
    private static Calendar created;

    public NewTaskFragment() {
        //Required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);
        getActivity().setTitle("New Task");

        Button btn = (Button)rootView.findViewById(R.id.save_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        Button btn2 = (Button)rootView.findViewById(R.id.cancel_button);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });


        deadline = Calendar.getInstance();
        initializeDatePicker(rootView);

        return rootView;
    }

    private void validate() {
        EditText titleField = (EditText)getView().findViewById(R.id.input_title);
        String title = titleField.getText().toString();

        EditText detailField = (EditText)getView().findViewById(R.id.input_detail);
        String detail = detailField.getText().toString();

        if (detail.equals("") || title.equals("")) {
//            Log.v(TAG, "Fields cannot be empty");
        } else {
//            Log.v(TAG, "Title  : " + title);
//            Log.v(TAG, "Detail : " + detail);
//            Log.v(TAG, "Date   : " + getFormattedDate(deadline));
            saveTask(title, detail, getFormattedDate(deadline));
        }
    }

    private void saveTask(String title, String detail, String deadline) {
        ContentValues newValues = new ContentValues();

        created = Calendar.getInstance();

        newValues.put(TodoListProvider.TaskEntry.COL_TITLE, title);
        newValues.put(TodoListProvider.TaskEntry.COL_DETAILS, detail);
        newValues.put(TodoListProvider.TaskEntry.COL_DEADLINE, deadline);
        newValues.put(TodoListProvider.TaskEntry.COL_TIME_CREATED, getFormattedDate(created));


        getActivity().getContentResolver().insert(
                TodoListProvider.CONTENT_URI,
                newValues
        );
        Log.d(TAG, "Item inserted");

        cancel();

//
//        String[] projection = {TodoListProvider.TaskEntry.COL_TITLE, TodoListProvider.TaskEntry.COL_DETAILS};
//        Cursor cursor = getActivity().getContentResolver().query(TodoListProvider.CONTENT_URI, projection, null, null, null);
//        cursor.moveToFirst();
//
//        String name = cursor.getString(cursor.getColumnIndexOrThrow(TodoListProvider.TaskEntry.COL_TITLE));
//        //String field0 = cursor.getString(0);
//        //String field1 = cursor.getString(1);
//
//        Log.d(TAG, name);
    }

    private void cancel() {
        int id = ((ViewGroup)getView().getParent()).getId();
        Log.v(TAG, "" + id);
        if (id == R.id.container) {
            //If in portrait. Replace self with the task list again
            Fragment fragment = TaskListFragment.newInstance("");
            getFragmentManager().beginTransaction().replace(R.id.container, fragment, "TaskListFragment").commit();
        } else if (id == R.id.right_pane) {
            //If in landscape. Reset the fields.
            EditText titleField = (EditText)getView().findViewById(R.id.input_title);
            titleField.setText("");
            EditText detailField = (EditText)getView().findViewById(R.id.input_detail);
            detailField.setText("");
            deadline = Calendar.getInstance();
            initializeDatePicker(getView());
        }
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

