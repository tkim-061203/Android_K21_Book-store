package com.example.do_an.ui_user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import java.util.ArrayList;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.do_an.R;
import com.example.do_an.ui_admin.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerBooks;
    private EditText searchBox;
    BookAdapter bookAdapter;
    ArrayList<Book> bookList = new ArrayList<>();
    ArrayList<Book> filteredList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        searchBox = findViewById(R.id.searchBox);
        RecyclerView recyclerView = findViewById(R.id.recycler);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("https://cogaihu.com/wp-content/uploads/2024/09/458789789_478113745044766_3570699670058121927_n.jpg?w=816");
        arrayList.add("https://bizweb.dktcdn.net/100/376/170/themes/750292/assets/slider_2.jpg?1693887815786");
        arrayList.add("https://thietkelogo.edu.vn/uploads/images/thiet-ke-do-hoa-khac/banner-sach/1.png");
        arrayList.add("https://newshop.vn/public/uploads/news/nhung-cuon-sach-van-hoc-hay.jpg");
        arrayList.add("https://interfaceingame.com/wp-content/uploads/need-for-speed-heat/need-for-speed-heat-banner.jpg");
        arrayList.add("https://images.unsplash.com/photo-1692610365998-c628604f5d9f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyODZ8fHxlbnwwfHx8fHw%3D&auto=format&fit=crop&w=500&q=60");

        ImageAdapter adapter = new ImageAdapter(MainActivity.this, arrayList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onClick(ImageView imageView, String path) {
                startActivity(new Intent(MainActivity.this, ImageViewActivity.class).putExtra("image", path), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imageView, "image").toBundle());
            }
        });

        // Sách bạn có thể sẽ thích
        recyclerBooks = findViewById(R.id.recyclerBooks);
        // Get screen width to determine number of columns
        int spanCount = getResources().getConfiguration().screenWidthDp > 600 ? 4 : 3; // 4 for tablets

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerBooks.setLayoutManager(layoutManager);


        bookAdapter = new BookAdapter(this, filteredList);
        recyclerBooks.setAdapter(bookAdapter);

        fetchBooksFromFirestore();

        setupSearch();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        // Using the OnItemSelectedListener correctly
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_home) {
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
                    startActivity(new Intent(getApplicationContext(), User_CategoryActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchBooksFromFirestore() {
        // Ensure Firestore is properly initialized
        if (db == null) {
            Toast.makeText(MainActivity.this, "Firestore not initialized!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch books from Firestore
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the lists before adding new data
                        bookList.clear();
                        filteredList.clear();

                        // Iterate through Firestore documents and convert to Book objects
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            book.setId(document.getId()); // Set the document ID as the book's ID
                            bookList.add(book);
                            filteredList.add(book); // Add to filteredList initially
                        }

                        // Notify the adapter that the data has changed
                        bookAdapter.notifyDataSetChanged();
                    } else {
                        // Show error if the fetch failed
                        Toast.makeText(MainActivity.this, "Error getting books: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSearch() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredbookList(s.toString());  // Filter books based on search query
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filteredbookList(String query) {
        filteredList.clear();  // Clear the current filtered list

        // If the query is empty, show all books by adding all items from bookList
        if (query.isEmpty()) {
            filteredList.addAll(bookList);  // Add all books to the filtered list
        } else {
            // Loop through bookList and add books that match the query
            for (Book book : bookList) {
                if (book.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(book);  // Add matching books to the filtered list
                }
            }
        }

        // Notify the adapter to refresh the RecyclerView with the filtered list
        bookAdapter.notifyDataSetChanged();
    }
}