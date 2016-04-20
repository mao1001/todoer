package edu.uw.mao1001.todoer;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    long ID;

    public DetailFragment() {}

    public static DetailFragment newInstance(long id) {
        DetailFragment fragment = new DetailFragment();
        fragment.ID = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);
        getActivity().setTitle("Task Detail");

        if (savedInstanceState != null) {
            ID = savedInstanceState.getLong("id");
        }

        fetchData();

        TextView titleLabel = (TextView)rootView.findViewById(R.id.detail_title);
        titleLabel.setText(title);

        TextView detailLabel = (TextView)rootView.findViewById(R.id.detail_detail);
        detailLabel.setText(details);

        TextView deadlineLabel = (TextView)rootView.findViewById(R.id.detail_deadline);
        deadlineLabel.setText(deadline);


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("id", ID);
        super.onSaveInstanceState(outState);
    }

    public void fetchData() {
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
