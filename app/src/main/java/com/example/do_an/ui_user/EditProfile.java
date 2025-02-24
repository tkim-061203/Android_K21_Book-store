package com.example.do_an.ui_user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.do_an.R;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgProfile;
    private EditText edtName, edtEmail;
    private Button btnSave, btnCancel;
    private String imageUrl = "https://example.com/avatar.jpg"; // Dữ liệu mẫu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Ánh xạ UI
        imgProfile = findViewById(R.id.img_profile);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // Hiển thị thông tin hiện tại của người dùng (Dữ liệu mẫu)
        edtName.setText("Nguyễn Ngọc Minh Trí");
        edtEmail.setText("tri@example.com");
        Glide.with(this).load(imageUrl).into(imgProfile);

        // Nhấn vào ảnh để chọn ảnh mới
        imgProfile.setOnClickListener(v -> openImageChooser());

        // Xử lý nút Lưu
        btnSave.setOnClickListener(v -> {
            String newName = edtName.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(EditProfile.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                // Giả lập lưu thông tin và quay về màn hình Profile
                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", newName);
                resultIntent.putExtra("email", newEmail);
                resultIntent.putExtra("image", imageUrl);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        // Xử lý nút Hủy
        btnCancel.setOnClickListener(v -> finish());
    }

    // Mở thư viện ảnh để chọn ảnh đại diện
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData().toString();
            Glide.with(this).load(imageUrl).into(imgProfile);
        }
    }
}
