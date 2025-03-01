package com.example.do_an.ui_user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.example.do_an.ui_admin.AddCategoryActivity;
import com.example.do_an.ui_admin.CategoryActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class User_BookListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Book> filteredList;  // List for filtered books based on search query
    private String categoriesID, categoriesName;
    private BookAdapter bookAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_list);

        // Nhận categoryID từ Intent
        categoriesID = getIntent().getStringExtra("categoryID");
        categoriesName = getIntent().getStringExtra("categoryName");

        TextView txtCategoryTitle = findViewById(R.id.txtCategoryTitle);
        txtCategoryTitle.setText("Category: " + categoriesName);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        int spanCount = getResources().getConfiguration().screenWidthDp > 600 ? 4 : 3; // 4 for tablets

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        filteredList = new ArrayList<>();

        // Khởi tạo adapter với danh sách rỗng trước
        bookAdapter = new BookAdapter(this, filteredList);
        recyclerView.setAdapter(bookAdapter);

        loadBooksFromFirebase(); // Tải sách từ Firebase

        ImageButton btnBack = findViewById(R.id.btnBack);

        // Handle Back Button Click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open CategoryActivity
                Intent intent = new Intent(User_BookListActivity.this, User_CategoryActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });
    }

    private void loadBooksFromFirebase() {
        if (categoriesName == null || categoriesName.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("products")
                .whereEqualTo("category", categoriesName) // Lọc theo category name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (filteredList == null) {
                            filteredList = new ArrayList<>();
                        }
                        filteredList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy dữ liệu của từng sản phẩm
                            String name = document.getString("name");
                            String author = document.getString("author");
                            String category = document.getString("category");
                            String description = document.getString("description");
                            String imageUrl = document.getString("imageUrl");
                            Long price = document.getLong("price");

                            Log.d("Firestore", "Product found - Name: " + name + ", Category: " + category);

                            // Chuyển thành đối tượng Book
                            Book book = new Book();
                            book.setId(document.getId());
                            book.setName(name);
                            book.setAuthor(author);
                            book.setCategory(category);
                            book.setDescription(description);
                            book.setImageUrl(imageUrl);
                            book.setPrice(Double.valueOf(price));

                            // Thêm vào danh sách hiển thị
                            filteredList.add(book);
                        }

                        // Cập nhật RecyclerView
                        bookAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(User_BookListActivity.this, "Lỗi khi tải sách: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
