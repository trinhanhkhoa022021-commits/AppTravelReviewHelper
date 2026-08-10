package com.example.apptravelreviewhelper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BillHistoryAdapter adapter;
    private FirebaseFirestore db;
    private List<Bill> billList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = FirebaseFirestore.getInstance();
        billList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewHistory);
        tvEmpty = findViewById(R.id.tvEmptyHistory);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Setup Pull to Refresh
        swipeRefreshLayout.setOnRefreshListener(this::loadHistoryFromFirestore);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        loadHistoryFromFirestore();
    }

    private void loadHistoryFromFirestore() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        
        db.collection("Bookings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    billList.clear();
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            try {
                                Bill bill = document.toObject(Bill.class);
                                if (bill != null) {
                                    billList.add(bill);
                                }
                            } catch (Exception e) {
                                android.util.Log.e("HistoryActivity", "Error mapping Bill: " + e.getMessage());
                            }
                        }
                    }
                    
                    updateUI();
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(HistoryActivity.this, "Lỗi khi nạp dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    android.util.Log.e("HistoryActivity", "Firestore Error: " + e.getMessage());
                    updateUI(); // Đảm bảo trạng thái Empty hiện lên nếu lỗi
                });
    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new BillHistoryAdapter(this, billList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        if (billList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}