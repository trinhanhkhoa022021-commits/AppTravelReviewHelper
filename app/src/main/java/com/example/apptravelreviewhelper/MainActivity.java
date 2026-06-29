package com.example.apptravelreviewhelper;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        // Ánh xạ giao diện RecyclerView
        recyclerView = findViewById(R.id.recyclerViewLocations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và Adapter
        locationList = new ArrayList<>();
        adapter = new LocationAdapter(this, locationList);
        recyclerView.setAdapter(adapter);

        // Khởi tạo Firestore và lấy dữ liệu
        db = FirebaseFirestore.getInstance();
        fetchLocationsFromFirebase();
    }

    private void fetchLocationsFromFirebase() {
        db.collection("Locations")
                .get()
                // Sử dụng OnCompleteListener chuẩn thay vì Lambda để tránh lỗi Expression expected
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            locationList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Chuyển dữ liệu từ Firebase thành đối tượng Location
                                Location loc = document.toObject(Location.class);
                                locationList.add(loc);
                            }
                            // Báo cho giao diện biết để load danh sách
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
                            Log.w("MainActivity", "Lỗi:", task.getException());
                        }
                    }
                });
    }
}