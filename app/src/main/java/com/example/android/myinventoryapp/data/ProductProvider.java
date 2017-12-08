package com.example.android.myinventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.myinventoryapp.R;
import com.example.android.myinventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by ndoor on 1/23/2017.
 * {@link ContentProvider} for the inventory app
 */

public class ProductProvider extends ContentProvider {

    // Tag for the log messages
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    // URI matcher code for the content URI for the product table
    private static final int PRODUCT = 100;

    // URI matcher code for the content URI for a single product in the product table
    private static final int PRODUCT_ID = 101;

    // UriMatcher object to match a content URI to a corresponding code. The input passed into the
    // constructor represents the code to return for the root URI.
    // It is common to use NO_MATCH as the input for this case.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.myinventoryapp/product" will
        // map to the integer code {@link #PRODUCT}. This URI is used to provide access to MULTIPLE
        // rows of the product table
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT, PRODUCT);

        // The content URI of the form "content://com.example.android.myinventoryapp/product/#" will
        // map to the integer code {@link #PRODUCT_ID}. This URI is used to provide access to ONE
        // single row of the product table
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY,
                ProductContract.PATH_PRODUCT + "/#", PRODUCT_ID);
    }

    // Database helper object
    private ProductDbHelper mDbHelper;

    // Initialize the provider and the database helper object
    @Override
    public boolean onCreate() {
        // Initialize a ProductDbHelper object to gain access to the inventory database
        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    // Perform the query for the given URI. Use the given projection, selection, selection arguments,
    // and sort order.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCT:
                // For the PRODUCT code, query the product table directly with the given projection,
                // selection, selection arguments, and sort order. The cursor may contain multiple
                // rows of the product table.
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                // For the PRODUCT_ID code, extract out the ID from the URI. The selection will be
                // "_id=?" and the selection argument will be a String array containing the actual
                // ID. For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?".
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                // Perform a query on the product table with the _id to return a Cursor containing
                // that row of the table
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(R.string.unknown_uri) + uri);
        }

        // Set notification URI on the Cursor so we know what content URI the Cursor was created
        // for. If the data at this URI changes then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // Returns the MIME type of data for the content URI
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
               throw new IllegalStateException(String.format(
                       String.valueOf(R.string.unknown_uri_match), uri, match));
        }
    }

    // Insert new data into the provider using the given uri and ContentValues
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCT:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException(
                        String.valueOf(R.string.unsupported_uri_insert) + uri);
        }
    }

    // This method inserts a product into the database with the given content values, then returns
    // the new content URI for that specific row in the database
    private Uri insertProduct(Uri uri, ContentValues values) {
        // Check that the name value is not null
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.needs_name));
        }

        // Check that the state value is a valid
        Integer state = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_STATE);
        if (state == null || !ProductEntry.isValidState(state)) {
            throw new IllegalArgumentException(String.valueOf(R.string.needs_state));
        }

        // Otherwise, get write-able database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new product with the given values
        long id = database.insert(
                ProductEntry.TABLE_NAME,    // The table name
                null,                       // The name of the column in which the framework can
                // insert NULL in the event that the ContentValues is
                // empty (if this is set to "null" then the framework
                // will not insert a row when there are no values)
                values);

        // If the ID is -1 then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, String.valueOf(R.string.row_insert_failed) + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the product content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID of the newly inserted row appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    // Delete the data at the given selection and selection arguments
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get write-able database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCT:
                // Delete all rows that match the selection and selection arguments for case PRODUCT
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                // Delete a single row given by the ID in the URI
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(
                        String.valueOf(R.string.unsupported_uri_delete) + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the given URI
        // has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    // Updates the data at the given selection and selection arguments, with the new ContentValues
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                // For the PRODUCT_ID code, extract out the ID from the URI, so we know which row
                // to update. Selection will be "_id=?" and selection arguments will be a String
                // array containing the actual ID.
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String [] {String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        String.valueOf(R.string.unsupported_uri_update) + uri);
        }
    }

    // This method updates products in the database with the given content values, applying the
    // changes to the rows specified in the selection and selection arguments (could be 0 or 1 or
    // more products), then returns the number of rows that were successfully updated
    private int updateProduct(Uri uri, ContentValues values, String selection,
                              String[] selectionArgs) {
        // If the {@link ProductEntry#COLUMN_PRODUCT_NAME} key is present, check that the name
        // value is not null
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.needs_name));
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_QUANTITY} key is present, check that the
        // quantity value is greater than or equal to 0 count
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(
                    String.valueOf(ProductEntry.COLUMN_PRODUCT_QUANTITY));
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException(String.valueOf(R.string.needs_quantity));
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_PRICE} key is present, check that the price
        // value is greater than or equal to 0 dollars
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Double price = values.getAsDouble(String.valueOf(ProductEntry.COLUMN_PRODUCT_PRICE));
            if (price != null && price < 0) {
                throw new IllegalArgumentException(String.valueOf(R.string.needs_price));
            }
        }

        // If the {@link ProductEntry#COLUMN_PRODUCT_STATE} key is present, check that the state
        // value is a valid, can be 0-50
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_STATE)) {
            Integer state = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_STATE);
            if (state == null || !ProductEntry.isValidState(state)) {
                throw new IllegalArgumentException(String.valueOf(R.string.needs_state));
            }
        }

        // No need to check the following, any value is valid (including null)
        // Product code, image id string, Supplier name, address lines, city, zip-code, phone,
        // e-mail, or website

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get write-able database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated= database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated then notify all listeners that the data at the given URI
        // has changed
        if (rowsUpdated !=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}