package com.example.do_an.ui_user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.do_an.R;

import java.text.DecimalFormat;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        // Set book details in the views
        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthor());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(book.getPrice()) + " VND";
        holder.bookPrice.setText(formattedPrice);

        // Load the image using Glide
        Glide.with(context)
                .load(book.getImageUrl())  // Use the image URL from the Book object
                .placeholder(R.drawable.baseline_image_24)  // Optional placeholder while image is loading
                .error(R.drawable.baseline_image_24)  // Optional error image if Glide fails to load
                .into(holder.bookImage);  // Load into the ImageView

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());  // Pass book id (or any other identifier)
            intent.putExtra("bookName", book.getName());
            intent.putExtra("bookAuthor", book.getAuthor());
            intent.putExtra("bookPrice", formattedPrice); // Pass the formatted price
            intent.putExtra("bookImageUrl", book.getImageUrl());
            context.startActivity(intent);  // Start BookDetailActivity
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, bookAuthor, bookPrice;
        ImageView bookImage;  // ImageView for the book cover image

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookPrice = itemView.findViewById(R.id.bookPrice);
            bookImage = itemView.findViewById(R.id.bookImage);  // Initialize the ImageView
        }
    }
}
