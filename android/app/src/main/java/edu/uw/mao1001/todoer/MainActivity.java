package edu.uw.mao1001.todoer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskListFragment fragment = (TaskListFragment)getSupportFragmentManager().findFragmentByTag("TaskListFragment");
        if (fragment != null) {
            fragment = new TaskListFragment();
        }
    }
}
