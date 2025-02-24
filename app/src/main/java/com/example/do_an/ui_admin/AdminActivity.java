package com.example.do_an.ui_admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.SearchView;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.example.do_an.ui_admin.AddCategoryActivity;
import com.example.do_an.ui_admin.CategoryAdapter;
import com.example.do_an.ui_user.CartActivity;
import com.example.do_an.ui_user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> filteredList = new ArrayList<>(); // Danh sách đã lọc
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        categoryAdapter = new CategoryAdapter(filteredList); // Dùng danh sách đã lọc
        recyclerView.setAdapter(categoryAdapter);

        fetchCategories();
        setupSearch();

        FloatingActionButton buttonAddCategory = findViewById(R.id.button_AddCategory);

        buttonAddCategory.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_category);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_category) {
                    return true;
                } else if (itemId == R.id.bottom_book) {
                    startActivity(new Intent(getApplicationContext(), Product.class));
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
            }
        });
    }

    private void fetchCategories() {
        db.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore", "Lỗi khi lấy danh mục", e);
                            return;
                        }

                        categoryList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Category category = doc.toObject(Category.class);
                            categoryList.add(category);
                        }
                        filteredList.clear();
                        filteredList.addAll(categoryList); // Cập nhật danh sách lọc ban đầu
                        categoryAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCategories(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCategories(newText);
                return false;
            }
        });
    }

    private void filterCategories(String query) {
        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
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
}
