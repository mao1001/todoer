package edu.uw.mao1001.todoer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uw.todoer.provider.TodoItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static boolean landscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTaskList(null);

    }

    private void loadTaskList(Bundle options) {
        //Attempts to find right_pane
        landscape = findViewById(R.id.right_pane) != null;
        Fragment fragment;
        if (options != null) {
            fragment = TaskListFragment.newInstance(
                    options.getString("selection"),
                    options.getString("orderBy"),
                    options.getString("title")
            );
        } else {
            fragment = TaskListFragment.newInstance(this);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int targetId;
        if (landscape) {
            //We are in landscape
            Fragment rightFragment = new NewTaskFragment();

            targetId = R.id.left_pane;

            ft.replace(R.id.right_pane, rightFragment, "NewTaskFragment");
        } else {
            targetId = R.id.container;
        }

        Log.v(TAG, "" + targetId);

        //Instantiate the main task list.

        ft.replace(targetId, fragment, "TaskListFragment");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.toString()) {
            case "Show Task List":
                loadTaskList(null);
                break;
            case "Show Completed":
                Bundle options = new Bundle();
                options.putString("selection", TodoItem.COMPLETED + "!=0");
                options.putString("orderBy", TodoItem.DEADLINE + " DESC");
                options.putString("title", getString(R.string.title_completed));
                loadTaskList(options);
                break;
            case "New Task":
                launchNewTask();
                break;
            case "By create date":
                resortData(TodoItem.TIME_CREATED + " ASC");
                break;
            case "By due date":
                resortData(TodoItem.DEADLINE + " DESC");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resortData(String orderBy) {
        TaskListFragment fragment = ((TaskListFragment)getSupportFragmentManager().findFragmentByTag("TaskListFragment"));
        fragment.reloadDate(orderBy);
    }

    private void launchNewTask() {
        int targetId;
        if (landscape) {
            targetId = R.id.right_pane;
        } else {
            targetId = R.id.container;
        }

        Fragment fragment = new NewTaskFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(targetId, fragment, "NewTaskFragment");
        ft.commit();
    }
}




