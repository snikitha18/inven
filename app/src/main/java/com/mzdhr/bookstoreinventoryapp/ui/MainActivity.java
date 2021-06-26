package com.mzdhr.bookstoreinventoryapp.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.mzdhr.bookstoreinventoryapp.R;
import com.mzdhr.bookstoreinventoryapp.adapter.ProductAdapter;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Objects
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ALL_PRODUCT_LOADER = 1000;
    private ProductAdapter mAdapter = null;

    // Views
    private ListView mListView;
    private LinearLayout mEmptyStatView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_book);
        } catch (NullPointerException e){

        }

        findViews();

        // Preparing Adapter, ListView, Empty View
        mAdapter = new ProductAdapter(this, null);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyStatView);

        getLoaderManager().initLoader(ALL_PRODUCT_LOADER, null, this);
    }

    private void findViews() {
        mListView = (ListView) findViewById(R.id.list);
        mEmptyStatView = (LinearLayout) findViewById(R.id.empty_view);
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
                ProductEntry.COLUMN_PRODUCT_NAME    // Order by alphabetical.
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
