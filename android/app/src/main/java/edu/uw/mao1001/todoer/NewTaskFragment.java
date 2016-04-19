package edu.uw.mao1001.todoer;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nick on 4/19/2016.
 */
public class NewTaskFragment extends Fragment {
    private static final String TAG = "NewTaskFragment";

    public NewTaskFragment() {
        //Required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

        return rootView;
    }
}
