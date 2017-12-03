package com.mzdhr.bookstoreinventoryapp.database;

import android.provider.BaseColumns;

/**
 * Created by mohammad on 12/2/17.
 *
 * Why Contract Class?
 * 1.Define Schema.
 * 2.Remove Spelling Errors.
 * 3.Easy of updating database Schema.
 */

public class DatabaseContract {

    // So no one create an object from this class
    private DatabaseContract(){}

    public static final class ProductEntry implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String PRODUCT_TABLE_NAME = "product_table_name";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "product_price";
        public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
        public static final String COLUMN_PRODUCT_IMAGE = "product_image";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "product_supplier_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_EMAIL = "product_supplier_email";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "product_supplier_phone_number";

        // Other Constants

    }


}
