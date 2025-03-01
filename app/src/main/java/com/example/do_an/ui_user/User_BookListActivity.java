package com.example.do_an.ui_user;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.example.do_an.ui_admin.Product;
import com.example.do_an.ui_admin.ProductActivity;
import com.example.do_an.ui_admin.ProductAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class User_BookListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Book> productList;  // List of all products
    private List<Book> filteredList;  // List for filtered products based on search query
    private String categoriesID, categoriesName;
    private ProductAdapter bookAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_list);

        // Nhận categoryID từ Intent
        categoriesID = getIntent().getStringExtra("categoryID");
        categoriesName = getIntent().getStringExtra("categoryName");

        // Hiển thị tên danh mục
        TextView txtCategoryTitle = findViewById(R.id.txtCategoryTitle);
        txtCategoryTitle.setText("Danh mục: " + categoriesName);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // Initialize the product lists
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Khởi tạo adapter và set vào RecyclerView
        bookAdapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(bookAdapter);

        loadBooksFromFirebase();
    }

    private void loadBooksFromFirebase() {
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        filteredList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            book.setId(document.getId());
                            productList.add(book);
                            filteredList.add(book);
                        }

                        bookAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(User_BookListActivity.this, "Error getting products: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

