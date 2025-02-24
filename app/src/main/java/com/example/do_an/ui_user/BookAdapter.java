package com.example.do_an.ui_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.do_an.R;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Book> bookList;

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookName.setText(book.getName());
        holder.bookWriter.setText(book.getWriter());
        holder.bookRating.setRating(book.getRating());

        Glide.with(context).load(book.getImageUrl()).into(holder.bookCover);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookName, bookWriter;
        RatingBar bookRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookName = itemView.findViewById(R.id.bookName);
            bookWriter = itemView.findViewById(R.id.bookWriter);
            bookRating = itemView.findViewById(R.id.bookRating);
        }
    }
}