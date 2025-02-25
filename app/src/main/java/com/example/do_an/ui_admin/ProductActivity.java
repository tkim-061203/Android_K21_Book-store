package com.example.do_an.ui_admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private EditText searchBox;  // Search EditText
    private RecyclerView recyclerView;  // RecyclerView for displaying products
    private ProductAdapter productAdapter;
    private List<Product> productList;  // List of all products
    private List<Product> filteredList;  // List for filtered products based on search query
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the product lists
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize the search box
        searchBox = findViewById(R.id.searchBox);

        // Initialize ProductAdapter with filteredList
        productAdapter = new ProductAdapter(this, filteredList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onDeleteClick(Product product) {
                deleteProduct(product);  // Call deleteProduct when delete button is clicked
            }

            @Override
            public void onUpdateClick(Product product) {
                Log.d("UpdateProductActivity", "Updating product with ID: " + product.getId());
                updateProduct(product);
            }
        });

        // Set the adapter to RecyclerView
        recyclerView.setAdapter(productAdapter);

        // Fetch all products and update filteredList
        fetchProducts();

        // Set up the search functionality
        setupSearch();

        // Add Product Button
        FloatingActionButton buttonAddProduct = findViewById(R.id.button_AddProduct);
        buttonAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_book);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_category) {
                    startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_book) {
                    return true;
                } else if (itemId == R.id.bottom_order) {
                    startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_setting) {
                    startActivity(new Intent(getApplicationContext(), Setting.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    // Method to fetch all products from Firestore
    private void fetchProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        filteredList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            productList.add(product);
                            filteredList.add(product);
                        }

                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ProductActivity.this, "Error getting products: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Setup the search functionality
    private void setupSearch() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Filter products based on the search query
    private void filterProducts(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }

        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the product list after adding or updating a product
        fetchProducts();
    }
    private void deleteProduct(Product product) {
        if (product == null || product.getId() == null) {
            Toast.makeText(ProductActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Attempt to delete the product from Firestore
        db.collection("products").document(product.getId())  // Use the product's id to identify the document
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Successfully deleted the product from Firestore
                    Toast.makeText(ProductActivity.this, "Product deleted successfully!", Toast.LENGTH_SHORT).show();

                    // Remove the product from the lists and update the adapter
                    productList.remove(product);
                    filteredList.remove(product);
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Toast.makeText(ProductActivity.this, "Error deleting product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void updateProduct(Product product) {
        Intent intent = new Intent(ProductActivity.this, UpdateProductActivity.class);
        intent.putExtra("product_id", product.getId());  // Pass product ID to the update activity
        intent.putExtra("product_name", product.getName());  // Pass other details as needed
        intent.putExtra("product_author", product.getAuthor());
        intent.putExtra("product_category", product.getCategory());
        intent.putExtra("product_imageUrl", product.getImageUrl());
        intent.putExtra("product_price", product.getPrice());

        startActivity(intent);
    }
}
