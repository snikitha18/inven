package com.mzdhr.bookstoreinventoryapp.model;

import android.content.Intent;

/**
 * Created by mohammad on 12/4/17.
 */

public class Product {
    private Integer mDatabaseID;
    private String mProductName;
    private Integer mProductPrice;
    private Integer mProductQuantity;
    private Integer mProductImage;
    private String mSupplierName;
    private String mSupplierEmail;
    private String mSupplierPhone;

    public Product() {
    }

    public Product(Integer databaseID, String productName, Integer productPrice, Integer productQuantity, Integer productImage, String supplierName, String supplierEmail, String supplierPhone) {
        mDatabaseID = databaseID;
        mProductName = productName;
        mProductPrice = productPrice;
        mProductQuantity = productQuantity;
        mProductImage = productImage;
        mSupplierName = supplierName;
        mSupplierEmail = supplierEmail;
        mSupplierPhone = supplierPhone;
    }


    public Integer getDatabaseID() {
        return mDatabaseID;
    }

    public String getProductName() {
        return mProductName;
    }

    public Integer getProductPrice() {
        return mProductPrice;
    }

    public Integer getProductQuantity() {
        return mProductQuantity;
    }

    public Integer getProductImage() {
        return mProductImage;
    }

    public String getSupplierName() {
        return mSupplierName;
    }

    public String getSupplierEmail() {
        return mSupplierEmail;
    }

    public String getSupplierPhone() {
        return mSupplierPhone;
    }


    public void setDatabaseID(Integer databaseID) {
        mDatabaseID = databaseID;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public void setProductPrice(Integer productPrice) {
        mProductPrice = productPrice;
    }

    public void setProductQuantity(Integer productQuantity) {
        mProductQuantity = productQuantity;
    }

    public void setProductImage(Integer productImage) {
        mProductImage = productImage;
    }

    public void setSupplierName(String supplierName) {
        mSupplierName = supplierName;
    }

    public void setSupplierEmail(String supplierEmail) {
        mSupplierEmail = supplierEmail;
    }

    public void setSupplierPhone(String supplierPhone) {
        mSupplierPhone = supplierPhone;
    }
}
