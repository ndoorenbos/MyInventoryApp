package com.example.android.myinventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.myinventoryapp.data.ProductContract.ProductEntry;

// Displays a list of products that were entered and stored in the app
public class CatalogActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Tag for the log messages
    public static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    // Identifier for the product data loader
    private static final int PRODUCT_LOADER = 0;

    // Adapter for the ListView
    ProductCursorAdapter mCursorAdapter;

    // ListView to populate
    ListView mProductListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Find the ListView
        mProductListView = (ListView) findViewById(R.id.catalog_list_view);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items
        View emptyView = findViewById(R.id.catalog_empty_view);
        mProductListView.setEmptyView(emptyView);

        // Setup a CursorAdapter to create a list item for each row of product data in the Cursor.
        // There is no product data yet (until the loader finishes), so pass in null for the Cursor.
        mCursorAdapter = new ProductCursorAdapter(this, null);
        mProductListView.setAdapter(mCursorAdapter);

        // Start the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar, action buttons or overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Add Item" menu option/icon
            case R.id.menu_catalog_add:
                // Create an Intent to go to {@link ItemEditorActivity}
                Intent intent = new Intent(CatalogActivity.this, ItemEditorActivity.class);
                startActivity(intent);
                return true;

            // Respond to a click on the "Delete" menu item
            case R.id.menu_catalog_delete_all:
                // Pop-up confirmation dialog for deletion of all products
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about
        String[] projection = {
                ProductEntry._ID,                       // Column with the product unique ID#
                ProductEntry.COLUMN_PRODUCT_NAME,       // Column with the product name
                ProductEntry.COLUMN_PRODUCT_PRICE,      // Column with the product price
                ProductEntry.COLUMN_PRODUCT_QUANTITY};  // Column with the product quantity

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,       // Parent Activity context
                ProductEntry.CONTENT_URI,   // Provider content URI to query
                projection,                 // Columns to include in the result cursor
                null,                       // No selection clause
                null,                       // No selection arguments
                null);                      // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update (@link ProductCursorAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    // This method creates an AlertDialog for the deletion of all pets
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);

        // Create click listeners for the positive and negative buttons on the dialog
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Delete" button so dismiss the dialog and continue deleting all
                // products from the database
                deleteAllProducts();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Cancel" button so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // This method performs the deletion of all products from the database
    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);

        // Show a toast message about the success/failure with the deletion
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the deletion, show message
            Toast.makeText(this, getString(R.string.delete_all_error), Toast.LENGTH_LONG).show();
        } else {
            // Otherwise the deletion was successful, show message
            Toast.makeText(this, getString(R.string.delete_all_confirm), Toast.LENGTH_LONG).show();
        }
    }
}