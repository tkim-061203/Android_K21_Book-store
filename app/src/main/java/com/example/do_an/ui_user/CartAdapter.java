//package com.example.do_an.ui_user.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.do_an.R;
//import com.example.do_an.models.CartItem;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
//
//    private Context context;
//    private List<CartItem> cartList;
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onQuantityChanged();
//        void onItemRemoved(int position);
//    }
//
//    public CartAdapter(Context context, List<CartItem> cartList, OnItemClickListener listener) {
//        this.context = context;
//        this.cartList = cartList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
//        return new CartViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//        CartItem item = cartList.get(position);
//
//        holder.tvTitle.setText(item.getTitle());
//        holder.tvPrice.setText("$" + item.getPrice());
//        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
//
//        Picasso.get().load(item.getImageUrl()).into(holder.imgBook);
//
//        // Nút tăng số lượng
//        holder.btnIncrease.setOnClickListener(v -> {
//            item.setQuantity(item.getQuantity() + 1);
//            notifyItemChanged(position);
//            listener.onQuantityChanged();
//        });
//
//        // Nút giảm số lượng
//        holder.btnDecrease.setOnClickListener(v -> {
//            if (item.getQuantity() > 1) {
//                item.setQuantity(item.getQuantity() - 1);
//                notifyItemChanged(position);
//                listener.onQuantityChanged();
//            }
//        });
//
//        // Nút xóa
//        holder.btnDelete.setOnClickListener(v -> {
//            cartList.remove(position);
//            notifyItemRemoved(position);
//            listener.onItemRemoved(position);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return cartList.size();
//    }
//
//    static class CartViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgBook, btnDelete;
//        TextView tvTitle, tvPrice, tvQuantity;
//        Button btnIncrease, btnDecrease;
//
//        public CartViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgBook = itemView.findViewById(R.id.imgBook);
//            btnDelete = itemView.findViewById(R.id.btnDelete);
//            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvPrice = itemView.findViewById(R.id.tvPrice);
//            tvQuantity = itemView.findViewById(R.id.tvQuantity);
//            btnIncrease = itemView.findViewById(R.id.btnIncrease);
//            btnDecrease = itemView.findViewById(R.id.btnDecrease);
//        }
//    }
//}