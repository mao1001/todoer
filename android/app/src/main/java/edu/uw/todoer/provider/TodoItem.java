package edu.uw.todoer.provider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Convenience class of constants for working with the TodoListProvider
 */
public class TodoItem {

    public static final String ID = TodoListProvider.TaskEntry._ID;
    public static final String TITLE = TodoListProvider.TaskEntry.COL_TITLE;
    public static final String DETAILS = TodoListProvider.TaskEntry.COL_DETAILS;
    public static final String DEADLINE = TodoListProvider.TaskEntry.COL_DEADLINE;
    public static final String COMPLETED = TodoListProvider.TaskEntry.COL_COMPLETED;
    public static final String TIME_CREATED = TodoListProvider.TaskEntry.COL_TIME_CREATED;

    public static String getFormmatedFullDate(Calendar date) {
        return new SimpleDateFormat("h:mm a. EEE, MMM d, y").format(date.getTime());
    }

    public static String getFormattedDate(Calendar date) {
        return new SimpleDateFormat("EEE, MMM d, y").format(date.getTime());
    }

    public static String getFormmattedTime(Calendar date) {
        return new SimpleDateFormat("h:mm a").format(date.getTime());
    }
}
