package edu.uw.mao1001.todoer;

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



        getContentResolver().insert()

//        TaskListFragment fragment = (TaskListFragment)getSupportFragmentManager().findFragmentByTag("TaskListFragment");
//        fragment = new TaskListFragment();
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.container, fragment, "TaskListFragment");
//        ft.commit();
//    }

        private String buildURI(String city) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendQueryParameter("q", city)
                    .appendQueryParameter("format", "JSON")
                    .appendQueryParameter("units", "imperial")
                    .appendQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_API_KEY);

            //Log.i(TAG, builder.build().toString());
            return builder.build().toString();
        }

    }
