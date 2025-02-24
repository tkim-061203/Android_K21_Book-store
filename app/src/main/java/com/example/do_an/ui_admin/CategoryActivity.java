package com.example.do_an.ui_admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private EditText searchBox;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> filteredList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize UI components
        searchBox = findViewById(R.id.searchBox);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        categoryAdapter = new CategoryAdapter(filteredList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onDeleteClick(Category category) {
                deleteCategoryFromFirestore(category.getId());  // Now passing the category ID
            }
        });

        recyclerView.setAdapter(categoryAdapter);

        fetchCategories();  // Fetch categories from Firestore

        setupSearch();  // Setup search functionality

        // Add Category Button
        FloatingActionButton buttonAddCategory = findViewById(R.id.button_AddCategory);
        buttonAddCategory.setOnClickListener(view -> {
            Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_category);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_category) {
                return true;
            } else if (itemId == R.id.bottom_book) {
                startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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
        });
    }

    // Fetch categories from Firestore
    private void fetchCategories() {
        db.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore", "Error fetching categories", e);
                            return;
                        }

                        categoryList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Category category = doc.toObject(Category.class);
                            category.setId(doc.getId());  // Ensure you are adding the document ID
                            categoryList.add(category);
                        }
                        filteredList.clear();
                        filteredList.addAll(categoryList);
                        categoryAdapter.notifyDataSetChanged();
                    }
                });
    }

    // Setup search functionality
    private void setupSearch() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Filter categories based on search input
    private void filterCategories(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(categoryList);
        } else {
            for (Category category : categoryList) {
                if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(category);
                }
            }
        }
        categoryAdapter.notifyDataSetChanged();
    }

    // Delete category from Firestore
    private void deleteCategoryFromFirestore(String categoryId) {
        db.collection("categories").document(categoryId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CategoryActivity.this, "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                    fetchCategories();  // Re-fetch categories after deletion
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CategoryActivity.this, "Error deleting category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
