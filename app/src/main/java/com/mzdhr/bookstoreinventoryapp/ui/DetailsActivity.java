package com.mzdhr.bookstoreinventoryapp.ui;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mzdhr.bookstoreinventoryapp.R;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // Object
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final int PRODUCT_SINGLE_LOADER = 1010;
    private boolean mModeEdit;
    private Uri mCurrentProductUri;

    private MenuItem mEditIconMenuItem;

    // Views
    private ImageView mEditImageButton;
    private EditText mProductNameEditText;
    private EditText mProductPriceEditText;
    private EditText mProductQuantityEditText;
    private Button mQuantityMinusButton;
    private Button mQuantityPlusButton;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;
    private EditText mSupplierEmailEditText;
    //private Button mOrderFromSupplierButton;
    //private Button mDeleteThisProductButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        findViews();
        //disableEditing();

        // Hide the keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        mEditImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mModeEdit){
//                    disableEditing();
//                    saveChanges();
//                } else {
//                    enableEditing();
//                }
//            }
//        });

        mQuantityMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeQuantity();
            }
        });

        mQuantityPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity();
            }
        });
        
//        mDeleteThisProductButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteConfirmation();
//            }
//        });

        // Trigger the Loader
        getLoaderManager().initLoader(PRODUCT_SINGLE_LOADER, null, this);
    }
    
    private void findViews() {
        //mEditImageButton = (ImageView) findViewById(R.id.pen_edit_image_view);
        mProductNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
        mProductPriceEditText = (EditText) findViewById(R.id.product_price_edit_view);
        mProductQuantityEditText = (EditText) findViewById(R.id.product_quantity_edit_view);
        mQuantityMinusButton = (Button) findViewById(R.id.product_quantity_minus_button);
        mQuantityPlusButton = (Button) findViewById(R.id.product_quantity_plus_button);
        mSupplierNameEditText = (EditText) findViewById(R.id.supplier_name_edit_text);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.supplier_phone_edit_text);
        mSupplierEmailEditText = (EditText) findViewById(R.id.supplier_email_edit_text);
        //mOrderFromSupplierButton = (Button) findViewById(R.id.order_more_product_button);
        //mDeleteThisProductButton = (Button) findViewById(R.id.delete_product_button);
    }
    
    private void removeQuantity() {
        if (TextUtils.isEmpty(mProductQuantityEditText.getText().toString())) {
            mProductQuantityEditText.setText("0");
        }
        int currentQuantity = Integer.parseInt(mProductQuantityEditText.getText().toString());

        if (currentQuantity > 0){
            currentQuantity--;
            mProductQuantityEditText.setText(String.valueOf(currentQuantity));
        } else {
            Toast.makeText(this, "Can not set negative quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private void addQuantity() {
        if (TextUtils.isEmpty(mProductQuantityEditText.getText().toString())) {
            mProductQuantityEditText.setText("0");
        }
        int currentQuantity = Integer.parseInt(mProductQuantityEditText.getText().toString());
        currentQuantity++;
        mProductQuantityEditText.setText(String.valueOf(currentQuantity));
    }

    private void disableEditing() {
        mProductNameEditText.setEnabled(false);
        mProductPriceEditText.setEnabled(false);
        mProductQuantityEditText.setEnabled(false);
        mQuantityMinusButton.setEnabled(false);
        mQuantityPlusButton.setEnabled(false);
        mSupplierNameEditText.setEnabled(false);
        mSupplierPhoneEditText.setEnabled(false);
        mSupplierEmailEditText.setEnabled(false);
        //mOrderFromSupplierButton.setEnabled(false);
        //mDeleteThisProductButton.setEnabled(false);
        mModeEdit = false;
    }


    private void enableEditing() {
        mProductNameEditText.setEnabled(true);
        mProductPriceEditText.setEnabled(true);
        mProductQuantityEditText.setEnabled(true);
        mQuantityMinusButton.setEnabled(true);
        mQuantityPlusButton.setEnabled(true);
        mSupplierNameEditText.setEnabled(true);
        mSupplierPhoneEditText.setEnabled(true);
        mSupplierEmailEditText.setEnabled(true);
        //mOrderFromSupplierButton.setEnabled(true);
        //mDeleteThisProductButton.setEnabled(true);
        mModeEdit = true;
    }

    private void saveChanges(){
        String productName = mProductNameEditText.getText().toString();
        String productPrice = mProductPriceEditText.getText().toString();
        String productQuantity = mProductQuantityEditText.getText().toString();
        String supplierName = mSupplierNameEditText.getText().toString();
        String supplierPhone = mSupplierPhoneEditText.getText().toString();
        String supplierEmail = mSupplierEmailEditText.getText().toString();

        // Checking values are not empty
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please Fill Product Name", Toast.LENGTH_SHORT).show();
            enableEditing();
            return;
        }
        if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Please Fill Price Name", Toast.LENGTH_SHORT).show();
            enableEditing();
            return;
        }
        if (TextUtils.isEmpty(productQuantity)) {
            Toast.makeText(this, "Please Fill Quantity Name", Toast.LENGTH_SHORT).show();
            enableEditing();
            return;
        }
        if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, "Please Fill Supplier Name", Toast.LENGTH_SHORT).show();
            enableEditing();
            return;
        }
        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(this, "Please Fill Supplier Phone", Toast.LENGTH_SHORT).show();
            enableEditing();
            return;
        }
        if (TextUtils.isEmpty(supplierEmail)) {
            Toast.makeText(this, "Please Fill Suppler Email", Toast.LENGTH_SHORT).show();
            enableEditing();
            return;
        }

        // Preparing values to insert
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_IMAGE, 0);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, supplierEmail);
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhone);

        // Inserting
        int insertUri = getContentResolver().update(mCurrentProductUri, values, null, null);
        // Checking Insert
        if (insertUri == 0){
            Toast.makeText(this, "Changes Unsaved!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "saveChanges: Update Successful");
            // Update the UI to the new values
            mProductNameEditText.setText(productName);
            mProductPriceEditText.setText(String.valueOf(productPrice));
            mProductQuantityEditText.setText(String.valueOf(productQuantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
            mSupplierEmailEditText.setText(supplierEmail);
            // Notify the user
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }

        disableEditing();
        mEditIconMenuItem.setIcon(R.drawable.ic_edit);
    }
    
    private void deleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_message);

        builder.setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });

        builder.setNegativeButton(R.string.no_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct(){
        if (mCurrentProductUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0){
                Toast.makeText(this, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_successful), Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    private void orderMore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.contact_supplier_title);
        builder.setMessage(R.string.contact_supplier_sub_title);
        builder.setNegativeButton(R.string.by_phone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String supplierPhone = mSupplierPhoneEditText.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone));
                if (!TextUtils.isEmpty(supplierPhone)){
                    startActivity(intent);
                } else {
                    Toast.makeText(DetailsActivity.this, R.string.no_phone, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setPositiveButton(R.string.by_email, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String supplierEmail = mSupplierEmailEditText.getText().toString();
                String productName = mProductNameEditText.getText().toString();

                Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + supplierEmail));
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.order_more_product));
                String bodyMessage = getString(R.string.order_more_product_sub) + productName;
                intent.putExtra(android.content.Intent.EXTRA_TEXT, bodyMessage);
                startActivity(intent);

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // --------------
    // Menu Section
    // --------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        mEditIconMenuItem = menu.findItem(R.id.edit_book_menu_button);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_book_menu_button:
                if (mModeEdit){
                    saveChanges();
//                    disableEditing();
//                    mEditIconMenuItem.setIcon(R.drawable.ic_edit);
                } else {
                    enableEditing();
                    mEditIconMenuItem.setIcon(R.drawable.ic_save);
                }
                break;

            case R.id.delete_book_menu_button:
                deleteConfirmation();
                break;

            case R.id.order_more_book_menu_button:
                // TODO: 12/6/17 Contact the supplier Dialog, by mail or phone!
                orderMore();
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
                DatabaseContract.ProductEntry._ID,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_NAME,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_IMAGE,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,
                DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,   // Selection
                null,   // SelectionArgs
                null    // Order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int currentID = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry._ID));
            String productName = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_NAME));
            int productPrice = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_PRICE));
            int productQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
            int productImage = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_IMAGE));
            String productSupplierName = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
            String productSupplierEmail = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL));
            String productSupplierPhoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER));

            mProductNameEditText.setText(productName);
            mProductPriceEditText.setText(String.valueOf(productPrice));
            mProductQuantityEditText.setText(String.valueOf(productQuantity));
            mSupplierNameEditText.setText(productSupplierName);
            mSupplierPhoneEditText.setText(productSupplierPhoneNumber);
            mSupplierEmailEditText.setText(productSupplierEmail);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
