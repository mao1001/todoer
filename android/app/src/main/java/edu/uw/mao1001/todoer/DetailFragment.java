package edu.uw.mao1001.todoer;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;

/**
 * Created by Nick on 4/20/2016.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";

    private String title;
    private String details;
    private String deadline;
    private String completed;

    private String ID;

    public DetailFragment() {}

    public static DetailFragment newInstance(String id) {
        DetailFragment fragment = new DetailFragment();
        fragment.ID = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);
        getActivity().setTitle("Task Detail");

        if (savedInstanceState != null) {
            ID = savedInstanceState.getString("id");
        }

        fetchData();
        loadData(rootView);
        initializeButtons(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("id", ID);
        super.onSaveInstanceState(outState);
    }

    private void initializeButtons(View rootView) {
        Button complete = (Button)rootView.findViewById(R.id.complete_button);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTask();
            }
        });

        Button delete = (Button)rootView.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
    }

    private void deleteTask() {
        getActivity().getContentResolver().delete(
                TodoListProvider.CONTENT_URI,
                TodoItem.ID + "=" + ID,
                null
        );

        exit();
    }

    private void completeTask() {
        if (completed.equals("0")) {
            this.completed = TodoItem.getFormattedDate(Calendar.getInstance());
            ContentValues values = new ContentValues();
            values.put(TodoItem.COMPLETED, completed);
            getActivity().getContentResolver().update(
                    TodoListProvider.CONTENT_URI,
                    values,
                    TodoItem.ID + "=" + ID,
                    null
            );

            TextView completedLabel = (TextView)getView().findViewById(R.id.detail_completed);
            completedLabel.setText(completed);
        }

        exit();
    }

    private void exit() {
        int id = ((ViewGroup)getView().getParent()).getId();
        Log.v(TAG, "" + id);
        if (id == R.id.container) {
            //If in portrait. Replace self with the task list again
            Fragment fragment = TaskListFragment.newInstance(getContext());
            getFragmentManager().beginTransaction().replace(R.id.container, fragment, "TaskListFragment").commit();
        } else if (id == R.id.right_pane) {
            Fragment fragment = new NewTaskFragment();
            getFragmentManager().beginTransaction().replace(R.id.right_pane, fragment, "NewTaskFragment").commit();
        }
    }

    private void loadData(View rootView) {
        TextView titleLabel = (TextView)rootView.findViewById(R.id.detail_title);
        titleLabel.setText(title);

        TextView detailLabel = (TextView)rootView.findViewById(R.id.detail_detail);
        detailLabel.setText(details);

        TextView deadlineLabel = (TextView)rootView.findViewById(R.id.detail_deadline);
        deadlineLabel.setText(deadline);

        TextView completedLabel = (TextView)rootView.findViewById(R.id.detail_completed);
        if (completed.equals("0")) {
            completedLabel.setText(getString(R.string.status_incomplete));
        } else {
            completedLabel.setText(completed);
        }
    }

    private void fetchData() {
        String[] projection =
                {
                        TodoItem.TITLE,
                        TodoItem.DETAILS,
                        TodoItem.DEADLINE,
                        TodoItem.COMPLETED
                };

        Cursor query = getActivity().getContentResolver().query(
                TodoListProvider.CONTENT_URI,
                projection,
                TodoItem.ID + "=" + ID,
                null,
                null
        );

        query.moveToFirst();
        this.title = query.getString(query.getColumnIndexOrThrow(TodoItem.TITLE));
        this.deadline = query.getString(query.getColumnIndexOrThrow(TodoItem.DEADLINE));
        this.details = query.getString(query.getColumnIndexOrThrow(TodoItem.DETAILS));
        this.completed = query.getString(query.getColumnIndexOrThrow(TodoItem.COMPLETED));
    }
}
