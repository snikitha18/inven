package com.mzdhr.bookstoreinventoryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract.ProductEntry;

/**
 * Created by mohammad on 12/3/17.
 *
 * Why does SQLiteOpenHelper?
 * 1.Create a SQLite db when it is first accessed.
 * 2.Gives you a connection to that database.
 * 3.Manages updating the database schema if version changes.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Constants
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "productdatabase.db";
    private static final int DATABASE_VERSION = 1;

    // SQL Commands
    private String SQL_CREATE_PRODUCT_TABLE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQL_CREATE_PRODUCT_TABLE =
                "CREATE TABLE " + ProductEntry.PRODUCT_TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL DEFAULT '', "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER, "
                + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                + ProductEntry.COLUMN_PRODUCT_IMAGE + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL DEFAULT '', "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL + " TEXT NOT NULL DEFAULT '', "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL DEFAULT '0' );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If there is a new version of database you need to run this command:
        // db.execSQL(SQL_DELETE_ENTRIES);
        // onCreate(db);
        // Or -> sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);
    }
}
