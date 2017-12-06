package com.mzdhr.bookstoreinventoryapp.adapter;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mzdhr.bookstoreinventoryapp.R;
import com.mzdhr.bookstoreinventoryapp.database.DatabaseContract;
import com.mzdhr.bookstoreinventoryapp.model.Product;
import com.mzdhr.bookstoreinventoryapp.ui.DetailsActivity;
import com.mzdhr.bookstoreinventoryapp.ui.MainActivity;

/**
 * Created by mohammad on 12/4/17.
 */

public class ProductAdapter extends ArrayAdapter<Product>{
    public static final String TAG = ProductAdapter.class.getSimpleName();
    public ProductAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        // Getting the current item on the list view
        final Product currentProduct = getItem(position);
        // Getting views
        TextView productNameTextView = (TextView) listItemView.findViewById(R.id.product_name_text_view);
        TextView productPriceTextView = (TextView) listItemView.findViewById(R.id.product_price_text_view);
        final TextView productQuantityTextView = (TextView) listItemView.findViewById(R.id.product_quantity_text_view);
        TextView productSellButton = (Button) listItemView.findViewById(R.id.product_sell_button);
        // Setting values
        productNameTextView.setText(currentProduct.getProductName());
        productPriceTextView.setText(String.valueOf(currentProduct.getProductPrice()));
        productQuantityTextView.setText(String.valueOf(currentProduct.getProductQuantity()));

        // Listener on Buttons
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentPetUri = ContentUris.withAppendedId(DatabaseContract.ProductEntry.CONTENT_URI_PRODUCT, currentProduct.getDatabaseID());
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.setData(currentPetUri);
                getContext().startActivity(intent);
            }
        });

        productSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME: 12/5/17 I don't like this way!
                int currentQuantity = Integer.parseInt(productQuantityTextView.getText().toString());
                Log.d(TAG, "onClick: " + currentQuantity);
                if (currentQuantity > 0){
                    currentQuantity--;
                    updateQuantityInDatabase(currentProduct, currentQuantity);
                    productQuantityTextView.setText(String.valueOf(currentQuantity));
                } else {
                    Toast.makeText(getContext(), "No Product Left! order from supplier or delete this product!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return listItemView;
    }


    private void updateQuantityInDatabase(Product product, int currentQuantity) {
        // Preparing the new value
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, currentQuantity);
        // Insert value by Product Uri
        Uri updateSingleProductUri = ContentUris.withAppendedId(DatabaseContract.ProductEntry.CONTENT_URI_PRODUCT, product.getDatabaseID());
        getContext().getContentResolver().update(updateSingleProductUri, values, null, null);
    }

}
