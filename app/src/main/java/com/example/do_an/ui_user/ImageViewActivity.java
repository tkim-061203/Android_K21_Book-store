package com.example.do_an.ui_user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.do_an.R;


public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView imageView = findViewById(R.id.imageView);

        Glide.with(ImageViewActivity.this).load(getIntent().getStringExtra("image")).into(imageView);
    }
}