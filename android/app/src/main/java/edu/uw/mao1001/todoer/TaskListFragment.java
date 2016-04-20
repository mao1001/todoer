package edu.uw.mao1001.todoer;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import edu.uw.todoer.provider.TodoItem;
import edu.uw.todoer.provider.TodoListProvider;

/**
 * Created by Nick on 4/17/2016.
 */
public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "TaskListFragment";

    private SimpleCursorAdapter adapter;

    public TaskListFragment() {
        //Required blank constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        getActivity().setTitle("Task List");

        //controller
        AdapterView listView = (AdapterView)rootView.findViewById(R.id.taskListView);

        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item,
                null,
                new String[] {TodoItem.TITLE},
                new int[] {R.id.txtItem},
                0);

        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToDetailView();
            }
        });



        return rootView;
    }

    private void moveToDetailView() {
        int parentId = ((ViewGroup)getView().getParent()).getId();
        Fragment fragment = new DetailFragment();
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
                null,
                null,
                null
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
