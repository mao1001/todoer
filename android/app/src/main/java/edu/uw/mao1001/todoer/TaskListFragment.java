package edu.uw.mao1001.todoer;

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

import org.w3c.dom.Text;

import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;

/**
 * Created by Nick on 4/17/2016.
 */
public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "TaskListFragment";

    String selection;
    String orderBy;
    String title;

    private SimpleCursorAdapter adapter;

    public TaskListFragment() {
        //Required blank constructor
    }

    public static TaskListFragment newInstance(String selection, String orderBy, String title) {
        TaskListFragment fragment = new TaskListFragment();
        fragment.selection = selection;
        fragment.orderBy = orderBy;
        fragment.title = title;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        getActivity().setTitle(title);

        //controller
        AdapterView listView = (AdapterView)rootView.findViewById(R.id.taskListView);

        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item,
                null,
                new String[] {TodoItem.TITLE},
                new int[] {R.id.txtItem},
                0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(TodoItem.TITLE)) {

                    //Log.v(TAG, "This is ID: " + cursor.getString(columnIndex - 1));

                    TextView titleView = (TextView)view.findViewById(R.id.list_title);
                    titleView.setText(cursor.getString(columnIndex));

                    String statusText = cursor.getString(columnIndex + 3);
                    //Log.v(TAG, cursor.getString(columnIndex + 2));
                    //Log.v(TAG, cursor.getString(columnIndex + 3));

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

        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToDetailView(id);
            }
        });



        return rootView;
    }

    public void reloadDate(String orderBy) {
        this.orderBy = orderBy;
        getLoaderManager().restartLoader(0, null, this);
    }

    private void moveToDetailView(long id) {
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
}
