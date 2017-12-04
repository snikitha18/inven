package com.mzdhr.bookstoreinventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract.ProductEntry;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Access our database with helper.
        mDatabaseHelper = new DatabaseHelper(this);

        // Inserting.
        insertData();

        // Reading.
        readData();
    }

    private void insertData() {
        // Get writable database.
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // Prepare the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Book : Invisible");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 80);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 3);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, 0);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Amazon");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "support@amazon.com");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "00 1 206-266-2992");

        // Insert, and return the column ID inserted, if return -1 that means data was not inserted correctly.
        long newRowId= db.insert(ProductEntry.PRODUCT_TABLE_NAME, null, values);
        Log.d(TAG, "insertData: " + newRowId);
    }

    private Cursor readData(){
        // Get Readable database.
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // Setting Projection.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };

        // Setting Cursor.
        Cursor cursor = db.query(
                ProductEntry.PRODUCT_TABLE_NAME,
                projection,
                null,   // Selection.
                null,   // Selection Args.
                null,   // Group By.
                null,   // Having.
                null    // Order by.
        );

        // Reading data that Cursor has.
        try {
            // Print title and header.
            Log.d(TAG, "--------------------------------------------------------------------------------------------");
            Log.d(TAG, "--------------------------------------------------------------------------------------------");
            Log.d(TAG, "The Product table contains " + cursor.getCount() + " Books.\n\n");
            Log.d(TAG, "ID - Name - Price - Quantity - Image - Supplier - Supplier Email - Supplier Phone" + "\n\n");

            // Loop through data that cursor has.
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
                String productName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
                int productPrice = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
                int productQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
                int productImage = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE));
                String productSupplierName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
                String productSupplierEmail = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL));
                String productSupplierPhoneNumber = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER));

                // Print the values for each column of the current row in the cursor.
                Log.d(TAG, "" + currentID + " - " + productName + " - " + productPrice + " - " + productQuantity + " - " + productImage + " - " + productSupplierName + " - " + productSupplierEmail + " - " + productSupplierPhoneNumber);
            }

            // Print Footer
            Log.d(TAG, "--------------------------------------------------------------------------------------------");
            Log.d(TAG, "--------------------------------------------------------------------------------------------");
        } finally {
            cursor.close();
        }
        // FIXME: 12/3/17 You are closing the cursor object before returning it. As a result,
        // the method which will receive the Cursor will not be able to use it. You need to simplify this method to return after the line 77.
        return cursor;
    }

}
