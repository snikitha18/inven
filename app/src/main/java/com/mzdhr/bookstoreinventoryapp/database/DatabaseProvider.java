package com.mzdhr.bookstoreinventoryapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by mohammad on 12/4/17.
 */

public class DatabaseProvider extends ContentProvider{

    private static final int PRODUCTS = 88;
    private static final int PRODUCT_SINGLE = 44;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String TAG = DatabaseProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_PRODUCTS + "/#", PRODUCT_SINGLE);
    }

    private DatabaseHelper mDatabaseHelper;


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                cursor = database.query(DatabaseContract.ProductEntry.PRODUCT_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_SINGLE:
                selection = DatabaseContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DatabaseContract.ProductEntry.PRODUCT_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException(TAG + ": Cannot query unknown Uri ---> " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){

            case PRODUCTS:
                return insertProduct(uri, values);

            default:
                throw new IllegalArgumentException(TAG + " Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        switch (match) {

            case PRODUCT_SINGLE:
                selection = DatabaseContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(DatabaseContract.ProductEntry.PRODUCT_TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){

            case PRODUCT_SINGLE:
                selection = DatabaseContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateSingleProduct(values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException(TAG + "Update is not supported for " + uri);
        }

    }

    // Helper Methods
    private Uri insertProduct(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        long id = database.insert(DatabaseContract.ProductEntry.PRODUCT_TABLE_NAME, null, values);
        if (id == -1){
            Log.d(TAG, "insertProduct: Failed to insert row for " + uri);
        }

        // Notify that calling context
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateSingleProduct(ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int rowUpdated = database.update(DatabaseContract.ProductEntry.PRODUCT_TABLE_NAME, values, selection, selectionArgs);
        return rowUpdated;
    }
}
