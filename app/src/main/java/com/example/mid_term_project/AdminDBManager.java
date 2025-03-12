package com.example.mid_term_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AdminDBManager {
    private static final String TAG = "AdminDBManager";

    private AdminDatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public AdminDBManager(Context c) {
        context = c;
    }

    public AdminDBManager open() throws SQLException {
        dbHelper = new AdminDatabaseHelper(context);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(TAG, "Error opening database: " + e.getMessage());
            throw e;
        }
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public long insert(String name, String desc) throws SQLException {
        ContentValues contentValue = new ContentValues();
        contentValue.put(AdminDatabaseHelper.SUBJECT, name);
        contentValue.put(AdminDatabaseHelper.DESC, desc);

        try {
            return database.insertOrThrow(AdminDatabaseHelper.TABLE_NAME, null, contentValue);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
            throw e;
        }
    }

    public Cursor fetch() throws SQLException {
        String[] columns = new String[] {
                AdminDatabaseHelper._ID,
                AdminDatabaseHelper.SUBJECT,
                AdminDatabaseHelper.DESC
        };

        try {
            Cursor cursor = database.query(
                    AdminDatabaseHelper.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    AdminDatabaseHelper.SUBJECT + " ASC" // Sort by name
            );

            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching data: " + e.getMessage());
            throw e;
        }
    }

    public int update(long _id, String name, String desc) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AdminDatabaseHelper.SUBJECT, name);
        contentValues.put(AdminDatabaseHelper.DESC, desc);

        try {
            return database.update(
                    AdminDatabaseHelper.TABLE_NAME,
                    contentValues,
                    AdminDatabaseHelper._ID + " = ?",
                    new String[]{String.valueOf(_id)}
            );
        } catch (SQLException e) {
            Log.e(TAG, "Error updating data: " + e.getMessage());
            throw e;
        }
    }

    public void delete(long _id) throws SQLException {
        try {
            database.delete(
                    AdminDatabaseHelper.TABLE_NAME,
                    AdminDatabaseHelper._ID + " = ?",
                    new String[]{String.valueOf(_id)}
            );
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting data: " + e.getMessage());
            throw e;
        }
    }
}