package com.mzdhr.bookstoreinventoryapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mzdhr.bookstoreinventoryapp.R;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    // Views
    private EditText mProductNameEditText;
    private EditText mProductPriceEditText;
    private EditText mProductQuantityEditText;
    private Button mQuantityMinusButton;
    private Button mQuantityPlusButton;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;
    private EditText mSupplierEmailEditText;
    private Button mAddProductImageButton;
    private Button mAddProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();

        mAddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

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

        mAddProductImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is not required
                Toast.makeText(AddActivity.this, "Image Added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews() {
        mProductNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
        mProductPriceEditText = (EditText) findViewById(R.id.product_price_edit_view);
        mProductQuantityEditText = (EditText) findViewById(R.id.product_quantity_edit_view);
        mQuantityMinusButton = (Button) findViewById(R.id.product_quantity_minus_button);
        mQuantityPlusButton = (Button) findViewById(R.id.product_quantity_plus_button);
        mSupplierNameEditText = (EditText) findViewById(R.id.supplier_name_edit_text);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.supplier_phone_edit_text);
        mSupplierEmailEditText = (EditText) findViewById(R.id.supplier_email_edit_text);
        mAddProductImageButton = (Button) findViewById(R.id.add_product_image_button);
        mAddProductButton = (Button) findViewById(R.id.add_product_button);
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

    private void addProduct(){
        String productName = mProductNameEditText.getText().toString();
        String productPrice = mProductPriceEditText.getText().toString();
        String productQuantity = mProductQuantityEditText.getText().toString();
        String supplierName = mSupplierNameEditText.getText().toString();
        String supplierPhone = mSupplierPhoneEditText.getText().toString();
        String supplierEmail = mSupplierEmailEditText.getText().toString();

        // Checking values are not empty
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please Fill Product Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Please Fill Price Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productQuantity)) {
            Toast.makeText(this, "Please Fill Quantity Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, "Please Fill Supplier Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(this, "Please Fill Supplier Phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierEmail)) {
            Toast.makeText(this, "Please Fill Suppler Email", Toast.LENGTH_SHORT).show();
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
        Uri insertUri = getContentResolver().insert(DatabaseContract.ProductEntry.CONTENT_URI_PRODUCT, values);
        // Checking Insert
        if (insertUri == null){
            Log.d(TAG, "addProduct: Insert Failed!");
            Toast.makeText(this, "Failed Insertion a Product Record", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "addProduct: Insert Successful");
        }

        finish();
    }


    // --------------
    // Menu Section
    // --------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_image_book_menu_button:
                Toast.makeText(AddActivity.this, "Image Added!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.save_book_menu_button:
                addProduct();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
