package com.example.android.myinventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myinventoryapp.data.ProductContract.ProductEntry;

import java.text.DecimalFormat;

/**
 * Created by ndoor on 1/23/2017.
 * (@link ProductCursorAdapter} is an adapter for the list or grid view that uses a {@link Cursor}
 * of product data as its data source. This adapter knows how to create list items for each new row
 * of product data in the {@link Cursor}.
 */

public class ProductCursorAdapter extends CursorAdapter {

    // Tag for the log messages
    public static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();

    // Format for the price displayed
    private DecimalFormat priceFormat = new DecimalFormat("'$'###,###,###.00");

    /**
     * Constructs a new {@link ProductCursorAdapter}
     * @param context is the app context
     * @param cursor is the cursor from which to get the data
     */
    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet
     * @param context is the app content
     * @param cursor is the cursor from which to get the data, cursor is moved to correct position
     * @param parent is the parent to which the new view is attached
     * @return the newly created list item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by the cursor) to the
     * given list item layout
     * @param view is the existing view, returned earlier by newView() method
     * @param context is the app context
     * @param cursor is the cursor from which to get the data, cursor is moved to correct row
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the inventory_list_item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.item_name_text_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price_text_view);
        TextView quantityTextView = (TextView) view.findViewById(R.id.item_qty_text_view);
        LinearLayout editItemLinearLayout =
                (LinearLayout) view.findViewById(R.id.list_item_edit_layout);
        TextView sellButtonTextView = (TextView) view.findViewById(R.id.item_sell_text_view);

        // Find the columns of product attributes that we are interested in
        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the product attributes from the cursor for the current product
        final int productID = cursor.getInt(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        double productPrice = cursor.getDouble(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);

        nameTextView.setText(productName);
        priceTextView.setText(priceFormat.format(productPrice));
        quantityTextView.setText(Integer.toString(productQuantity));

        // Set OnClickListener for the Edit of the item
        editItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Form the content URI that represents the specific product that was clicked on by
                // appending the "id" (passed as an input to this method) onto the
                // {@link Product_Entry#CONTENT_URI}
                Uri mCurrentProductURI =
                        ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productID);

                // User clicked the list item information, create an intent to the editor activity
                Intent editIntent = new Intent(context, ItemEditorActivity.class);

                // Set the URI on the data field of the intent
                editIntent.setData(mCurrentProductURI);

                // Launch the {@link ItemEditorActivity} to display the data for the current product
                context.startActivity(editIntent);
            }
        });

        // Set OnClickListener for the sale of the item
        sellButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQuantity > 0) {
                    int mQuantity = productQuantity - 1;

                    // Create a ContentValues object and put the quantity in the correct column
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mQuantity);

                    // Form the content URI that represents the specific product that was clicked on by
                    // appending the "id" (passed as an input to this method) onto the
                    // {@link Product_Entry#CONTENT_URI}
                    Uri mCurrentProductURI =
                            ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productID);

                    // Update the product with the content URI: mCurrentProductUri and pass in the new
                    // ContentValues. Pass in null for the selection and selection args because
                    // mCurrentProductUri will already identify the correct row in the database that we
                    // want to modify
                    int rowsAffected =
                            context.getContentResolver().update(mCurrentProductURI, values, null, null);

                    // Show a toast message about the success or failure of the update
                    if (rowsAffected == 0) {
                        // If no rows were affected there was an error with the update
                        Toast.makeText(context, context.getString(R.string.product_update_failed),
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Otherwise, the update was successful
                        Toast.makeText(context, context.getString(R.string.product_update_success),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Show a toast, cannot subtract if 0
                    Toast.makeText(context, context.getString(R.string.no_products_to_sell),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}