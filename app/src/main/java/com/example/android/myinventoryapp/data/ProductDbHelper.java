package com.example.android.myinventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myinventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by ndoor on 1/24/2017.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    // Tag used for log messages
    public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();

    // Database name
    private static final String DATABASE_NAME = "inventory.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ProductDbHelper}
     * @param context of the app
     */
    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the product table
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " +
                ProductEntry.TABLE_NAME + " (" +
                ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                ProductEntry.COLUMN_PRODUCT_CODE + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0, " +
                ProductEntry.COLUMN_PRODUCT_PRICE + " DOUBLE DEFAULT 0, " +
                ProductEntry.COLUMN_PRODUCT_IMAGE + " BLOB DEFAULT NULL, " +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_ADDY1 + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_ADDY2 + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_CITY + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_STATE + " INTEGER, " +
                ProductEntry.COLUMN_PRODUCT_ZIP + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_PHONE + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_EMAIL + " TEXT, " +
                ProductEntry.COLUMN_PRODUCT_WEBSITE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database is still at version 1, nothing to do yet
    }
}