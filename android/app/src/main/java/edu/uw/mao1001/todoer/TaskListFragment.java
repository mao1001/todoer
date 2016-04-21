package edu.uw.mao1001.todoer;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.Calendar;
import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;

/**
 * Created by Nick on 4/17/2016.
 */
public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "TaskListFragment";

    private String title;
    private String selection;
    private String orderBy;
    private SimpleCursorAdapter adapter;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    public TaskListFragment() {
        //Required blank constructor
    }

    /**
     * Returns an instance of this class based on passed in parameters
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself)
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *                (excluding the ORDER BY itself).
     * @param title Title of the action bar to be changed to
     * @return
     */
    public static TaskListFragment newInstance(String selection, String orderBy, String title) {
        TaskListFragment fragment = new TaskListFragment();
        fragment.selection = selection;
        fragment.orderBy = orderBy;
        fragment.title = title;
        return fragment;
    }

    /**
     * Returns an instance of this class with default values
     * Selection will be all row that ARE NOT completed
     * Data will be sorted with deadline DESCENDING
     * @param context
     * @return
     */
    public static TaskListFragment newInstance(Context context) {
        return TaskListFragment.newInstance(
                TodoItem.COMPLETED + "=0",
                TodoItem.DEADLINE + " DESC",
                context.getString(R.string.title_todo)
        );
    }

    //-----------------------------------------//
    //   F R A G M E N T   O V E R R I D E S   //
    //-----------------------------------------//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        getActivity().setTitle(title);
        initializeAdapter(rootView);

        return rootView;
    }

    //-------------------------------------//
    //   L O A D E R   O V E R R I D E S   //
    //-------------------------------------//

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection =
                {
                        TodoItem.ID,
                        TodoItem.TITLE,
                        TodoItem.DETAILS,
                        TodoItem.DEADLINE,
                        TodoItem.COMPLETED,
                        TodoItem.TIME_CREATED
                };

        //Creates loader for query
        CursorLoader loader = new CursorLoader(
                getActivity(),
                TodoListProvider.CONTENT_URI,
                projection,
                selection,
                null,
                orderBy
        );
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    //---------------------------------//
    //   P U B L I C   M E T H O D S   //
    //---------------------------------//

    /**
     * Reloads data based on passed in query parameter
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *                (excluding the ORDER BY itself)
     */
    public void reloadDate(String orderBy) {
        this.orderBy = orderBy;
        getLoaderManager().restartLoader(0, null, this);
    }

    //-----------------------------------//
    //   P R I V A T E   M E T H O D S   //
    //-----------------------------------//

    /**
     * Initializes the adapter for listview
     * @param rootView
     */
    private void initializeAdapter(View rootView) {
        //controller
        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item,
                null,
                new String[] {TodoItem.TITLE},
                new int[] {R.id.txtItem},
                0
        );

        //ViewBinder to determine what to data to display where.
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(TodoItem.TITLE)) {

                    TextView titleView = (TextView)view.findViewById(R.id.list_title);
                    titleView.setText(cursor.getString(columnIndex));

                    String statusText = cursor.getString(columnIndex + 3);

                    //For some reason the created date going in is fine
                    //but coming out is the milisecond form.
                    TextView createdLabel = (TextView)view.findViewById(R.id.list_created_label);
                    Calendar test = Calendar.getInstance();
                    Long omg = Long.parseLong(cursor.getString(columnIndex + 4));
                    test.setTimeInMillis(omg);
                    String wtf = TodoItem.getFormmatedFullDate(test);
                    createdLabel.setText(wtf);

                    TextView statusView = (TextView)view.findViewById(R.id.list_status);
                    if (statusText.equals("0")) {
                        statusText = cursor.getString(columnIndex + 2);
                        statusView.setText(getString(R.string.status_due_label));
                    } else {
                        statusView.setText(getString(R.string.status_completed_label));
                    }

                    TextView dateView = (TextView)view.findViewById(R.id.list_date);
                    dateView.setText(statusText);
                }

                return true;
            }
        });

        //List view adapter to figure to hook up with data
        AdapterView listView = (AdapterView)rootView.findViewById(R.id.taskListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToDetailView("" + id);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Moves the user to a detail view of a task
     * @param id Id of the task to move to
     */
    private void moveToDetailView(String id) {
        int parentId = ((ViewGroup)getView().getParent()).getId();
        Fragment fragment = DetailFragment.newInstance(id);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        int targetId;
        if (parentId == R.id.container) {
            targetId = R.id.container;
        } else {
            targetId = R.id.right_pane;
        }

        ft.replace(targetId, fragment, "DetailFragment").commit();
    }
}
