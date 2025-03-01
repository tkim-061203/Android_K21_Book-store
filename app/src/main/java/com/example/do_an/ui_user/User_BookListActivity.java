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

        TextView txtCategoryTitle = findViewById(R.id.txtCategoryTitle);
        txtCategoryTitle.setText("Danh mục: " + categoriesName);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách trước khi dùng Adapter
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Khởi tạo adapter với danh sách rỗng trước
        bookAdapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(bookAdapter);

        loadBooksFromFirebase(); // Tải sách từ Firebase
    }

    private void loadBooksFromFirebase() {
        if (categoriesID == null || categoriesID.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("products")
                .whereEqualTo("categoryID", categoriesID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (filteredList == null) {
                            filteredList = new ArrayList<>();  // Đảm bảo danh sách không bị null
                        }
                        filteredList.clear();  // Xóa dữ liệu cũ trước khi cập nhật mới

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            book.setId(document.getId());
                            filteredList.add(book);
                        }

                        bookAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(User_BookListActivity.this, "Lỗi khi tải sách: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

