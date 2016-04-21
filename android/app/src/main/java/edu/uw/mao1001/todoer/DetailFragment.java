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
import android.widget.TextView;
import java.util.Calendar;

import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;

/**
 * Created by Nick on 4/20/2016.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";

    private String ID;
    private String title;
    private String details;
    private String deadline;
    private String completed;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    /**
     * Required blank constructor
     */
    public DetailFragment() {}

    /**
     * Returns an instantiated fragment. This is preferable tot he blank fragment.
     * @param id ID of the task to be initiated with
     * @return An instance of this class instantiated.
     */
    public static DetailFragment newInstance(String id) {
        DetailFragment fragment = new DetailFragment();
        fragment.ID = id;
        return fragment;
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//

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

    /**
     * Required!!! This view will crash without an ID.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("id", ID);
        super.onSaveInstanceState(outState);
    }

    //-----------------------------------//
    //   P R I V A T E   M E T H O D S   //
    //-----------------------------------//

    /**
     * Deletes the task in this detail view
     * This will also close the detail view and return the user to a more appropriate view
     */
    private void deleteTask() {
        getActivity().getContentResolver().delete(
                TodoListProvider.CONTENT_URI,
                TodoItem.ID + "=" + ID,
                null
        );

        exit();
    }

    /**
     * Exits this view and returns the user
     * to the master list view if in portrait
     * or create a new task view if in landscape
     */
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

    /**
     * Method to fetch the data of a task based on the
     * ID field instantiated on this view
     */
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

    /**
     * Instantiates the buttons with their apporpriate click events
     * @param rootView
     */
    private void initializeButtons(View rootView) {
        final Button complete = (Button)rootView.findViewById(R.id.complete_button);

        if (!completed.equals("0")) {
            Button btn = (Button)rootView.findViewById(R.id.complete_button);
            btn.setHint(R.string.button_un_complete_task_label);
        }

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleComplete();
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

    /**
     * Loads the data into the corresponding UI elements. Should be called
     * after fetchData
     * @param rootView
     */
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

    /**
     * Toggles the complete status of the task
     */
    private void toggleComplete() {
        Button btn = (Button)getView().findViewById(R.id.complete_button);
        TextView completedLabel = (TextView)getView().findViewById(R.id.detail_completed);
        if (completed.equals("0")) {
            this.completed = TodoItem.getFormmatedFullDate(Calendar.getInstance());
            btn.setHint(R.string.button_un_complete_task_label);
            completedLabel.setText(completed);
        } else {
            this.completed = "0";
            btn.setHint(R.string.button_complete_task_label);
            completedLabel.setText(getString(R.string.status_incomplete));
        }

        ContentValues values = new ContentValues();
        values.put(TodoItem.COMPLETED, completed);
        getActivity().getContentResolver().update(
                TodoListProvider.CONTENT_URI,
                values,
                TodoItem.ID + "=" + ID,
                null
        );
    }
}
