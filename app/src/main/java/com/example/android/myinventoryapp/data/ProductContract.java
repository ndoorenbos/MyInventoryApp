package com.example.android.myinventoryapp.data;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.ByteArrayOutputStream;

/**
 * Created by ndoor on 1/23/2017.
 * Defines table contents for the product database
 */

public final class ProductContract {

    // Prevent someone from accidentally instantiating this contract class provide an empty
    // constructor
    private ProductContract() {
    }

    // Tag for the log messages
    public static final String LOG_TAG = ProductContract.class.getSimpleName();

    // Content Authority, name for entire content provider
    public static final String CONTENT_AUTHORITY = "com.example.android.myinventoryapp";

    // Base for all URI's for apps to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible path to append to base content URI for possible URI's
    public static final String PATH_PRODUCT = "product";

    // Inner class that defines the table contents of the product table
    public static final class ProductEntry implements BaseColumns {

        // Content URI to access the product data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

        // The MIME type of the {@link #CONTENT_URI} for a list of products
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        // The MIME type of the {@link #CONTENT_URI} for a single product
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final Bitmap NO_PRODUCT_IMAGE = null;

        // Name of the ProductEntry table in the database
        public static final String TABLE_NAME = "product";

        // Column for the unique ID given to a product
        public static final String _ID = BaseColumns._ID;

        // Column for the name of the product
        public static final String COLUMN_PRODUCT_NAME = "name";

        // Column for the product code
        public static final String COLUMN_PRODUCT_CODE = "code";

        // Column for the quantity of product in stock
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        // Column for the product price
        public static final String COLUMN_PRODUCT_PRICE = "price";

        // Column for the name of the image of the product
        public static final String COLUMN_PRODUCT_IMAGE = "image";

        // Column for the name of the supplier
        public static final String COLUMN_PRODUCT_SUPPLIER = "supplier";

        // Column for the supplier's address line 1
        public static final String COLUMN_PRODUCT_ADDY1 = "addy1";

        // Column for the supplier's address line 2
        public static final String COLUMN_PRODUCT_ADDY2 = "addy2";

        // Column for the supplier's city
        public static final String COLUMN_PRODUCT_CITY = "city";

        // Column for the supplier's state
        public static final String COLUMN_PRODUCT_STATE = "state";

        // Column for the supplier's zip code
        public static final String COLUMN_PRODUCT_ZIP = "zip";

        // Column for the supplier's phone number
        public static final String COLUMN_PRODUCT_PHONE = "phone";

        // Column for the supplier's e-mail address
        public static final String COLUMN_PRODUCT_EMAIL = "email";

        // Column for the supplier's website address
        public static final String COLUMN_PRODUCT_WEBSITE = "website";

        // Constants for the state selection spinner
        public static final int STATE_UNKNOWN = 0;
        public static final int STATE_ALABAMA = 1;
        public static final int STATE_ALASKA = 2;
        public static final int STATE_ARIZONA = 3;
        public static final int STATE_ARKANSAS = 4;
        public static final int STATE_CALIFORNIA = 5;
        public static final int STATE_COLORADO = 6;
        public static final int STATE_CONNECTICUT = 7;
        public static final int STATE_DELAWARE = 8;
        public static final int STATE_FLORIDA = 9;
        public static final int STATE_GEORGIA = 10;
        public static final int STATE_HAWAII = 11;
        public static final int STATE_IDAHO = 12;
        public static final int STATE_ILLINOIS = 13;
        public static final int STATE_INDIANA = 14;
        public static final int STATE_IOWA = 15;
        public static final int STATE_KANSAS = 16;
        public static final int STATE_KENTUCKY = 17;
        public static final int STATE_LOUISIANA = 18;
        public static final int STATE_MAINE = 19;
        public static final int STATE_MARYLAND = 20;
        public static final int STATE_MASSACHUSETTS = 21;
        public static final int STATE_MICHIGAN = 22;
        public static final int STATE_MINNESOTA = 23;
        public static final int STATE_MISSISSIPPI = 24;
        public static final int STATE_MISSOURI = 25;
        public static final int STATE_MONTANA = 26;
        public static final int STATE_NEBRASKA = 27;
        public static final int STATE_NEVADA = 28;
        public static final int STATE_NEW_HAMPSHIRE = 29;
        public static final int STATE_NEW_JERSEY = 30;
        public static final int STATE_NEW_MEXICO = 31;
        public static final int STATE_NEW_YORK = 32;
        public static final int STATE_NORTH_CAROLINA = 33;
        public static final int STATE_NORTH_DAKOTA = 34;
        public static final int STATE_OHIO = 35;
        public static final int STATE_OKLAHOMA = 36;
        public static final int STATE_OREGON = 37;
        public static final int STATE_PENNSYLVANIA = 38;
        public static final int STATE_RHODE_ISLAND = 39;
        public static final int STATE_SOUTH_CAROLINA = 40;
        public static final int STATE_SOUTH_DAKOTA = 41;
        public static final int STATE_TENNESSEE = 42;
        public static final int STATE_TEXAS = 43;
        public static final int STATE_UTAH = 44;
        public static final int STATE_VERMONT = 45;
        public static final int STATE_VIRGINIA = 46;
        public static final int STATE_WASHINGTON = 47;
        public static final int STATE_WEST_VIRGINIA = 48;
        public static final int STATE_WISCONSIN = 49;
        public static final int STATE_WYOMING = 50;

        // This method checks if state is one of the 51 acceptable integer inputs
        public static boolean isValidState(int state) {
            switch (state) {
                case ProductEntry.STATE_UNKNOWN:
                    return true;
                case ProductEntry.STATE_ALABAMA:
                    return true;
                case ProductEntry.STATE_ALASKA:
                    return true;
                case ProductEntry.STATE_ARIZONA:
                    return true;
                case ProductEntry.STATE_ARKANSAS:
                    return true;
                case ProductEntry.STATE_CALIFORNIA:
                    return true;
                case ProductEntry.STATE_COLORADO:
                    return true;
                case ProductEntry.STATE_CONNECTICUT:
                    return true;
                case ProductEntry.STATE_DELAWARE:
                    return true;
                case ProductEntry.STATE_FLORIDA:
                    return true;
                case ProductEntry.STATE_GEORGIA:
                    return true;
                case ProductEntry.STATE_HAWAII:
                    return true;
                case ProductEntry.STATE_IDAHO:
                    return true;
                case ProductEntry.STATE_ILLINOIS:
                    return true;
                case ProductEntry.STATE_INDIANA:
                    return true;
                case ProductEntry.STATE_IOWA:
                    return true;
                case ProductEntry.STATE_KANSAS:
                    return true;
                case ProductEntry.STATE_KENTUCKY:
                    return true;
                case ProductEntry.STATE_LOUISIANA:
                    return true;
                case ProductEntry.STATE_MAINE:
                    return true;
                case ProductEntry.STATE_MARYLAND:
                    return true;
                case ProductEntry.STATE_MASSACHUSETTS:
                    return true;
                case ProductEntry.STATE_MICHIGAN:
                    return true;
                case ProductEntry.STATE_MINNESOTA:
                    return true;
                case ProductEntry.STATE_MISSISSIPPI:
                    return true;
                case ProductEntry.STATE_MISSOURI:
                    return true;
                case ProductEntry.STATE_MONTANA:
                    return true;
                case ProductEntry.STATE_NEBRASKA:
                    return true;
                case ProductEntry.STATE_NEVADA:
                    return true;
                case ProductEntry.STATE_NEW_HAMPSHIRE:
                    return true;
                case ProductEntry.STATE_NEW_JERSEY:
                    return true;
                case ProductEntry.STATE_NEW_MEXICO:
                    return true;
                case ProductEntry.STATE_NEW_YORK:
                    return true;
                case ProductEntry.STATE_NORTH_CAROLINA:
                    return true;
                case ProductEntry.STATE_NORTH_DAKOTA:
                    return true;
                case ProductEntry.STATE_OHIO:
                    return true;
                case ProductEntry.STATE_OKLAHOMA:
                    return true;
                case ProductEntry.STATE_OREGON:
                    return true;
                case ProductEntry.STATE_PENNSYLVANIA:
                    return true;
                case ProductEntry.STATE_RHODE_ISLAND:
                    return true;
                case ProductEntry.STATE_SOUTH_CAROLINA:
                    return true;
                case ProductEntry.STATE_SOUTH_DAKOTA:
                    return true;
                case ProductEntry.STATE_TENNESSEE:
                    return true;
                case ProductEntry.STATE_TEXAS:
                    return true;
                case ProductEntry.STATE_UTAH:
                    return true;
                case ProductEntry.STATE_VERMONT:
                    return true;
                case ProductEntry.STATE_VIRGINIA:
                    return true;
                case ProductEntry.STATE_WASHINGTON:
                    return true;
                case ProductEntry.STATE_WEST_VIRGINIA:
                    return true;
                case ProductEntry.STATE_WISCONSIN:
                    return true;
                case ProductEntry.STATE_WYOMING:
                    return true;
                default:
                    return false;
            }
        }

        // This method converts a bitmap to a byte[]
        public static byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        // This method converts a byte[] to a bitmap
        public static Bitmap getPhoto(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
   }
}