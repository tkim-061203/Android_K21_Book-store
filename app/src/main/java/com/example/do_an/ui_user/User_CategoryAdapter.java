package com.example.do_an.ui_user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an.R;

import java.util.List;


public class User_CategoryAdapter extends RecyclerView.Adapter<User_CategoryAdapter.ViewHolder> {
    private Context context;
    private List<User_Category> categoryList;

    public User_CategoryAdapter(Context context, List<User_Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User_Category category = categoryList.get(position);
        holder.textViewCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.tvCategoryName);
            if (textViewCategory == null) {
                Log.e("User_CategoryAdapter", "TextView with ID tvCategoryName is null");
            }
        }
    }
}