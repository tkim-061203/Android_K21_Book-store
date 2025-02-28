package com.example.do_an.ui_user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an.R;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.util.Log;
import com.bumptech.glide.Glide;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

public class User_CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<com.example.do_an.ui_admin.Category> categoryList = new ArrayList<>();
    private List<com.example.do_an.ui_admin.Category> filteredList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(categoryAdapter);
        loadCategoriesFromFirebase();

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_Cate);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_cart) {
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_Cate) {
                    return true;
                }
                return false;
            }
        });
    }

    private void loadCategoriesFromFirebase() {
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
                            com.example.do_an.ui_user.Category category = doc.toObject(Category.class);
                            category.setId(doc.getId());  // Ensure you are adding the document ID
                            categoryList.add(category);
                        }
                        filteredList.clear();
                        filteredList.addAll(categoryList);
                        categoryAdapter.notifyDataSetChanged();
                    }
                });
    }
}