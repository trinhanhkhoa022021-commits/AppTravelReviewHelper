package com.example.apptravelreviewhelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // Tạm thời để trống phần load ảnh, chúng ta sẽ dùng thư viện Glide xử lý sau để tránh code bị rối lúc này
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    // Class ViewHolder giúp giữ các view (TextView, ImageView) để không phải tìm lại nhiều lần
    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvRating;
        ImageView imgLocation;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_location.xml
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvRating = itemView.findViewById(R.id.tvRating);
            imgLocation = itemView.findViewById(R.id.imgLocation);
        }
    }
}