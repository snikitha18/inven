package com.mzdhr.bookstoreinventoryapp.ui;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.Toolbar;
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

    // Objects
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e){
            Log.d(TAG, "onCreate: " + e);
        }

        findViews();

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
            Toast.makeText(this, R.string.can_not_set_negative_quantity, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, R.string.please_fill_product_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, R.string.please_fill_price, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productQuantity)) {
            Toast.makeText(this, R.string.please_fill_quantity, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, R.string.please_fill_supplier_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(this, R.string.please_fill_supplier_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierEmail)) {
            Toast.makeText(this, R.string.please_fill_supplier_email, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, R.string.saved_failed, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "addProduct: Insert Successful");
            Toast.makeText(this, R.string.saved_successful, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddActivity.this, R.string.image_added, Toast.LENGTH_SHORT).show();
                break;
            case R.id.save_book_menu_button:
                addProduct();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
