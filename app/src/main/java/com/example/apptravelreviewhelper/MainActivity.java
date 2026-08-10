package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ giao diện RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
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
                return true;
            } else if (itemId == R.id.nav_search) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return false;
            } else if (itemId == R.id.nav_saved) {
                Intent intent = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return false;
            } else if (itemId == R.id.nav_booking) {
                Intent intent = new Intent(MainActivity.this, BookingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return false;
            } else if (itemId == R.id.nav_account) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return false;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchLocationsFromFirebase() {
        db.collection("Locations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        locationList.clear();
                        if (task.getResult().isEmpty()) {
                            Log.d("MainActivity", "Collection 'Locations' is empty.");
                            Toast.makeText(MainActivity.this, "Không có địa điểm nào để hiển thị.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    Location loc = document.toObject(Location.class);
                                    loc.setId(document.getId());
                                    locationList.add(loc);
                                    Log.d("MainActivity", "Loaded location: " + loc.getName());
                                } catch (Exception e) {
                                    Log.e("MainActivity", "Error mapping document " + document.getId() + ": " + e.getMessage());
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        String errorMsg = (task.getException() != null) ? task.getException().getMessage() : "Unknown error";
                        Log.e("MainActivity", "Firestore Fetch Error: " + errorMsg);
                        
                        if (errorMsg.contains("PERMISSION_DENIED")) {
                            Toast.makeText(MainActivity.this, "Lỗi: Chưa cấu hình quyền truy cập Firestore (Rules)!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi lấy dữ liệu: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}