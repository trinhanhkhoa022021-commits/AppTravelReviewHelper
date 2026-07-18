package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<Location> locationList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ giao diện RecyclerView (Lưu ý: ID phải khớp với file activity_main.xml)
        recyclerView = findViewById(R.id.recyclerView);
        // Nếu file XML của bạn vẫn để id là recyclerViewLocations thì đổi chữ recyclerView ở trên thành recyclerViewLocations nhé.

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và Adapter
        locationList = new ArrayList<>();
        adapter = new LocationAdapter(this, locationList);
        recyclerView.setAdapter(adapter);

        // Khởi tạo Firestore và lấy dữ liệu
        db = FirebaseFirestore.getInstance();
        fetchLocationsFromFirebase();

        // 2. Ánh xạ và xử lý sự kiện cho Thanh Menu dưới cùng (Bottom Navigation)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Mặc định chọn tab "Điểm Hot" khi vừa mở app
        bottomNavigationView.setSelectedItemId(R.id.nav_hot);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_hot) {
                // Đang ở màn hình chính (Điểm Hot), không cần làm gì cả
                return true;
            } else if (itemId == R.id.nav_search) {
                // Chuyển sang trang Tìm kiếm
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Tắt hiệu ứng chuyển động cho mượt
                return false;
            } else if (itemId == R.id.nav_saved) {
                Toast.makeText(MainActivity.this, "Sắp ra mắt: Trang Đã lưu", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_booking) {
                Toast.makeText(MainActivity.this, "Sắp ra mắt: Trang Đặt chỗ", Toast.LENGTH_SHORT).show();
                return true;

            } else if (itemId == R.id.nav_account) {
                // Chuyển sang màn hình Tài khoản
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
                return false;
            }

            return false;
        });
    }

    private void fetchLocationsFromFirebase() {
        db.collection("Locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            locationList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Location loc = document.toObject(Location.class);
                                locationList.add(loc);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
                            Log.w("MainActivity", "Lỗi:", task.getException());
                        }
                    }
                });
    }
}