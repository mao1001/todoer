package edu.uw.mao1001.todoer;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.uw.todoer.provider.TodoListProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TaskListFragment fragment = (TaskListFragment)getSupportFragmentManager().findFragmentByTag("TaskListFragment");
        fragment = new TaskListFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, "TaskListFragment");
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


