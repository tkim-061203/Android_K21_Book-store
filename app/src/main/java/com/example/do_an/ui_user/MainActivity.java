package com.example.do_an.ui_user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.MenuItem;
import java.util.ArrayList;
import android.widget.ImageView;

import com.example.do_an.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerBooks;
    BookAdapter bookAdapter;
    ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("https://images.unsplash.com/photo-1692528131755-d4e366b2adf0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzNXx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60");
        arrayList.add("https://images.unsplash.com/photo-1692862582645-3b6fd47b7513?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0MXx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60");
        arrayList.add("https://images.unsplash.com/photo-1692584927805-d4096552a5ba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0Nnx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60");
        arrayList.add("https://images.unsplash.com/photo-1692854236272-cc49076a2629?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw1MXx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60");
        arrayList.add("https://images.unsplash.com/photo-1681207751526-a091f2c6a538?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyODF8fHxlbnwwfHx8fHw%3D&auto=format&fit=crop&w=500&q=60");
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
        //recyclerBooks.setLayoutManager(new GridLayoutManager(this, 3));

        bookList = new ArrayList<>();
        bookList.add(new Book("Book 1", "Author 1", "https://d3525k1ryd2155.cloudfront.net/f/176/070/9798217070176.IN.0.m.jpg", 4.5f));
        bookList.add(new Book("Book 2", "Author 2", "https://d3525k1ryd2155.cloudfront.net/h/344/237/1570237344.0.l.jpg", 4.0f));
        bookList.add(new Book("Book 3", "Author 3", "https://m.media-amazon.com/images/I/815rJRMLqqL.jpg", 4.8f));
        bookList.add(new Book("Book 4", "Author 4", "https://m.media-amazon.com/images/I/71jWyah3siL._AC_UF1000,1000_QL80_.jpg", 3.9f));
        bookList.add(new Book("Book 5", "Author 5", "https://sachngoaingugiare.com/wp-content/uploads/2024/07/let-1-786x1024.png", 5.0f));
        bookList.add(new Book("Book 6", "Author 6", "https://chantroisangtao.vn/wp-content/uploads/2022/02/Screenshot-2022-02-17-095129-70.png", 4.2f));

        bookAdapter = new BookAdapter(this, bookList);
        recyclerBooks.setAdapter(bookAdapter);


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
                }
                return false;
            }
        });
    }
}