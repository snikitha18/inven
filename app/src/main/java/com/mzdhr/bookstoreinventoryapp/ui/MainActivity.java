package com.mzdhr.bookstoreinventoryapp.ui;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mzdhr.bookstoreinventoryapp.R;
import com.mzdhr.bookstoreinventoryapp.adapter.ProductAdapter;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract.ProductEntry;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseHelper;
import com.mzdhr.bookstoreinventoryapp.model.Product;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ALL_PRODUCT_LOADER = 1000;
    // Objects
    private DatabaseHelper mDatabaseHelper;
    private ProductAdapter mAdapter = null;
    private ArrayList<Product> mProducts;
    private int mPressedProduct;

    // Views
    private ListView mListView;
    private LinearLayout mEmptyStatView;
    private ImageView mEmptyStateImageView;
    private TextView mEmptyStateTextView;
    private FloatingActionButton mFloatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_book);
        } catch (NullPointerException e){

        }

        // FindViews
        mListView = (ListView) findViewById(R.id.list);
        mEmptyStatView = (LinearLayout) findViewById(R.id.empty_view);
        mEmptyStateImageView = (ImageView) findViewById(R.id.empty_image_view);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view_text);
        //mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        // Preparing Adapter
        mAdapter = new ProductAdapter(this, null);

        // Preparing ListView, and setting Adapter and Empty-View to it.
        mProducts = new ArrayList<>();
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyStatView);

        // Set listener to the button
//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                startActivity(intent);
//            }
//        });

        getLoaderManager().initLoader(ALL_PRODUCT_LOADER, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Trigger the Loader again
        getLoaderManager().restartLoader(ALL_PRODUCT_LOADER, null, this);
    }


    // --------------
    // Menu Section
    // --------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_book_menu_button:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    // --------------
    // Loader Section
    // --------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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

        return new CursorLoader(this,
                ProductEntry.CONTENT_URI_PRODUCT,
                projection,
                null,   // Selection
                null,   // SelectionArgs
                null    // Order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);


//        mProducts.clear();
//        mAdapter.clear();
//
//        if (cursor == null || cursor.getCount() < 1) {
//            Log.d(TAG, "onLoadFinished: Cursor is null or there is less than 1 row in the cursor");
//            // Setting the Empty State view
//            mEmptyStateTextView.setText(R.string.no_product_found_text);
//            mEmptyStateImageView.setImageResource(R.drawable.empty_products);
//            return;
//        }
//
//        // Loop through data that cursor has.
//        while (cursor.moveToNext()) {
//            int currentID = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
//            String productName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
//            int productPrice = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
//            int productQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
//            int productImage = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE));
//            String productSupplierName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
//            String productSupplierEmail = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL));
//            String productSupplierPhoneNumber = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER));
//
//            Product product = new Product(
//                    currentID,
//                    productName,
//                    productPrice,
//                    productQuantity,
//                    productImage,
//                    productSupplierName,
//                    productSupplierEmail,
//                    productSupplierPhoneNumber);
//
//            mProducts.add(0, product);
//        }
//
//        mAdapter.addAll(mProducts);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
