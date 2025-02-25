package com.example.do_an.ui_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.do_an.R;

import java.text.DecimalFormat;
import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product title
        holder.productTitle.setText(product.getName());

        // Set product author
        holder.productAuthor.setText("Author: " + product.getAuthor());

        // Format price with commas
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(product.getPrice()) + " VND";
        holder.productPrice.setText(formattedPrice);

        // Set category, with a default value if it's empty
        String category = product.getCategory();
        if (category == null || category.isEmpty()) {
            category = "Uncategorized";
        }
        holder.productCategory.setText("Category: " + category);

        // Load product image using Glide with null check
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_image_24)  // Placeholder image
                    .error(R.drawable.baseline_image_24)  // Error image
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.baseline_image_24);  // Default image if no image URL
        }

        // Set the delete button click listener
        holder.deleteButton.setOnClickListener(view -> listener.onDeleteClick(product));

        // Set the update button click listener (assuming you have an "Update" button in item_product layout)
        holder.updateButton.setOnClickListener(view -> listener.onUpdateClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle, productPrice, productCategory, productAuthor;
        ImageButton deleteButton, updateButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCategory = itemView.findViewById(R.id.productCategory);
            productAuthor = itemView.findViewById(R.id.productAuthor);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            updateButton = itemView.findViewById(R.id.updateButton);  // Assuming this button exists in the layout
        }
    }

    public interface OnProductClickListener {

        void onDeleteClick(Product product);

        void onUpdateClick(Product product);  // Method for handling the update click
    }
}
