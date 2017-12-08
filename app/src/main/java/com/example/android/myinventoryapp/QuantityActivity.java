package com.example.android.myinventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myinventoryapp.data.ProductContract.ProductEntry;

public class QuantityActivity extends AppCompatActivity {

    // Variables for quantity
    int mCurrentQuantity = 0;
    int mChangeQuantity = 1;

    // Uri sent with the Intent
    Uri mCurrentUri;

    // Change defining variables/constants
    public static final int ADD_PRODUCT = 1;
    public static final int REMOVE_PRODUCT = 0;
    private static final int DO_NOTHING = -1;
    int addOrSubtract = DO_NOTHING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);

        // Get the current quantity passed throw the intent
        Intent intent = getIntent();
        mCurrentQuantity = intent.getIntExtra("currentQuantity", 0);
        addOrSubtract = intent.getIntExtra("addOrSubtract", 0);
        mCurrentUri = intent.getData();

        // Find the views we need to monitor
        TextView minusTextView = (TextView) findViewById(R.id.quantity_minus_button);
        TextView plusTextView = (TextView) findViewById(R.id.quantity_plus_button);
        TextView inStockTextView = (TextView) findViewById(R.id.current_quantity_text_view);
        TextView changeTypeTextView = (TextView) findViewById(R.id.change_type_text_view);
        final EditText quantityEditText = (EditText) findViewById(R.id.change_quantity_edit_text);

        // Set the current  and sales quantity and the change type
        inStockTextView.setText(String.valueOf(mCurrentQuantity));
        quantityEditText.setText(String.valueOf(mChangeQuantity));
        if (addOrSubtract == ADD_PRODUCT) {
            changeTypeTextView.setText(R.string.quantity_to_add);
        } else {
            changeTypeTextView.setText(R.string.quantity_to_sell);
        }

        // Set OnClick Listeners to the minus and plus buttons
        minusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeQuantity > 0 && mCurrentQuantity > 0) {
                    mChangeQuantity = mChangeQuantity - 1;
                    quantityEditText.setText(String.valueOf(mChangeQuantity));
                }
            }
        });
        plusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeQuantity = mChangeQuantity + 1;
                quantityEditText.setText(String.valueOf(mChangeQuantity));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_selling.xml file
        getMenuInflater().inflate(R.menu.menu_selling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar, action buttons or overflow menu
        switch (item.getItemId()) {
            // Respond to the "Save" option selected
            case R.id.menu_selling_save:
                updateWithSales();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // This method updates the product
    private void updateWithSales() {
        // Determine the change type and calculate the change
        if (addOrSubtract == ADD_PRODUCT) {
            mCurrentQuantity = mCurrentQuantity + mChangeQuantity;
        } else if (addOrSubtract == REMOVE_PRODUCT) {
            mCurrentQuantity = mCurrentQuantity - mChangeQuantity;
        }

        // Create a ContentValues object and put the quantity in with the correct column name
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mCurrentQuantity);

        // Update the product with the content URI: mCurrentProductUri and pass in the new
        // ContentValues. Pass in null for the selection and selection args because
        // mCurrentProductUri will already identify the correct row in the database that we
        // want to modify
        int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

        // Show a toast message about the success or failure of the update
        if (rowsAffected == 0) {
            // If no rows were affected there was an error with the update
            Toast.makeText(QuantityActivity.this, getString(R.string.product_update_failed),
                    Toast.LENGTH_LONG).show();
        } else {
            // Otherwise, the update was successful
            Toast.makeText(QuantityActivity.this, getString(R.string.product_update_success),
                    Toast.LENGTH_LONG).show();
        }
    }
}