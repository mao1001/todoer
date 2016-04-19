package edu.uw.mao1001.todoer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static boolean landscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Attempts to find right_pane
        landscape = findViewById(R.id.right_pane) != null;

        int targetId;
        if (landscape) {
            //We are in landscape
            targetId = R.id.left_pane;
        } else {
            targetId = R.id.container;
        }

        //Instantiate the main task list.
        Fragment fragment = new TaskListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
        Log.d(TAG, "Clicked: " + item.toString());

        switch (item.toString()) {
            case "New Task":
                launchNewTask();
                break;
        }


        return super.onOptionsItemSelected(item);
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

//    ContentValues newValues = new ContentValues();
//newValues.put(TodoListProvider.TaskEntry.COL_TITLE, "This is a test title");
//        newValues.put(TodoListProvider.TaskEntry.COL_DETAILS, "These are some details");
//
//
//        Uri newUri = getContentResolver().insert(
//        TodoListProvider.CONTENT_URI,
//        newValues
//        );
//
//
//        Log.d(TAG, "Item inserted");
//
//
//        String[] projection = {TodoListProvider.TaskEntry.COL_TITLE, TodoListProvider.TaskEntry.COL_DETAILS};
//        Cursor cursor = getContentResolver().query(TodoListProvider.CONTENT_URI, projection, null, null, null);
//        cursor.moveToFirst();
//
//        String name = cursor.getString(cursor.getColumnIndexOrThrow(TodoListProvider.TaskEntry.COL_TITLE));
//        //String field0 = cursor.getString(0);
//        //String field1 = cursor.getString(1);
//
//        Log.d(TAG, name);


