package com.example.apptravelreviewhelper;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private Context context;
    private List<Location> locationList;

    // Constructor để truyền dữ liệu từ MainActivity vào Adapter
    public LocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp giao diện item_location.xml cho từng dòng
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        // Lấy dữ liệu của địa điểm tại vị trí hiện tại
        Location location = locationList.get(position);

        // Đổ dữ liệu vào các TextView
        holder.tvName.setText(location.getName());
        holder.tvAddress.setText(location.getAddress());
        holder.tvRating.setText("Rating: " + location.getRating());
        Glide.with(context)
                .load(location.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery) // Ảnh hiển thị tạm trong lúc chờ tải
                .into(holder.imgLocation);

        // ====== Xử lý nút lưu (ngôi sao) ======
        boolean isSaved = SavedLocationManager.isSaved(context, location.getId());
        holder.ivSave.setImageResource(
                isSaved ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off
        );

        holder.ivSave.setOnClickListener(v -> {
            boolean nowSaved = SavedLocationManager.toggleSave(context, location.getId());
            holder.ivSave.setImageResource(
                    nowSaved ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off
            );
            Toast.makeText(
                    context,
                    nowSaved ? "Đã lưu địa điểm" : "Đã bỏ lưu",
                    Toast.LENGTH_SHORT
            ).show();
        });
        // ====== Hết phần xử lý nút lưu ======

        // Sự kiện khi click vào một item địa điểm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            // Truyền toàn bộ thông tin của địa điểm này sang màn hình chi tiết
            intent.putExtra("name", location.getName());
            intent.putExtra("address", location.getAddress());
            intent.putExtra("description", location.getDescription());
            intent.putExtra("rating", location.getRating());
            intent.putExtra("imageUrl", location.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    // Class ViewHolder giúp giữ các view (TextView, ImageView) để không phải tìm lại nhiều lần
    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvRating;
        ImageView imgLocation;
        ImageView ivSave;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_location.xml
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvRating = itemView.findViewById(R.id.tvRating);
            imgLocation = itemView.findViewById(R.id.imgLocation);
            ivSave = itemView.findViewById(R.id.ivSave);
        }
    }
}