package com.example.do_an.ui_admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText etNameProduct, etNameAuthor, etPriceProduct, etImageUrl, etDescription;
    private Spinner spinnerCategoryProduct;
    private Button btnUpdate;

    private FirebaseFirestore db;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the product data passed from the previous activity
        productId = getIntent().getStringExtra("product_id");
        String productName = getIntent().getStringExtra("product_name");
        String author = getIntent().getStringExtra("product_author");
        double price = getIntent().getDoubleExtra("product_price", 0);
        String imageUrl = getIntent().getStringExtra("product_imageUrl");
        String category = getIntent().getStringExtra("product_category");
        String description = getIntent().getStringExtra("product_description");

        // Initialize the views
        etNameProduct = findViewById(R.id.etNameProduct);
        etNameAuthor = findViewById(R.id.etNameAuthor);
        etPriceProduct = findViewById(R.id.etPriceProduct);
        etImageUrl = findViewById(R.id.etImageUrl);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategoryProduct = findViewById(R.id.spinnerCategory_Product);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Pre-fill the fields with the existing product data
        etNameProduct.setText(productName);
        etNameAuthor.setText(author);
        etPriceProduct.setText(String.valueOf(price));
        etImageUrl.setText(imageUrl);
        etDescription.setText(description);

        // You may want to set the spinner's selected item based on the current category (if it's dynamic)
        // For simplicity, this assumes the categories are populated elsewhere in your app.

        // Handle update button click
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });
    }

    private void updateProduct() {
        // Get updated values from the EditText fields
        String updatedName = etNameProduct.getText().toString();
        String updatedAuthor = etNameAuthor.getText().toString();
        double updatedPrice = Double.parseDouble(etPriceProduct.getText().toString());
        String updatedImageUrl = etImageUrl.getText().toString();
        String updatedCategory = spinnerCategoryProduct.getSelectedItem().toString();  // Assuming this spinner has categories
        String updatedDescription = etDescription.getText().toString();

        // Create a map with updated values (using constructor to create the Product object)
        Product updatedProduct = new Product(productId, updatedName, updatedPrice, updatedImageUrl, updatedDescription, updatedCategory, updatedAuthor);

        // Update Firestore with the new data
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products").document(productId)
                .set(updatedProduct)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateProductActivity.this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateProductActivity.this, "Error updating product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
