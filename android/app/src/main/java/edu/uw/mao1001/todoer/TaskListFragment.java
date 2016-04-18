package edu.uw.mao1001.todoer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.uw.todoer.provider.TodoListProvider;

/**
 * Created by Nick on 4/17/2016.
 */
public class TaskListFragment extends Fragment {

    private static final String TAG = "TaskListFragment";

    private ArrayAdapter<String> adapter;

    public TaskListFragment() {
        //Required blank constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        Log.v(TAG, "onCreateView");

        //controller
        adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.list_item, R.id.txtItem, new ArrayList<String>());

        ListView listView = (ListView)rootView.findViewById(R.id.taskListView);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Movie movie = (Movie)parent.getItemAtPosition(position);
//                Log.v(TAG, "You clicked on: "+movie);
//
//                //tell the activity to do stuff!
//                //((OnMovieSelectedListener)getActivity()).movieSelected(movie);
//                callback.movieSelected(movie);
//
//            }
//        });
//        Log.v(TAG, "In create bundle: "+savedInstanceState);
//        if(savedInstanceState != null && savedInstanceState.getString("searchTerm") != null) {
//            fetchData(savedInstanceState.getString("searchTerm"));
//        }



        adapter.add("Data1");
        adapter.add("Data2");
        adapter.add("Data3");

        return rootView;
    }


    public void fetchData() {

    }
}
