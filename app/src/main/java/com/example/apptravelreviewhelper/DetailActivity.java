package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptravelreviewhelper.adapters.ReviewAdapter;
import com.example.apptravelreviewhelper.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgDetail;
    private Button btnOpenMap, btnBookTour; // Đã thêm khai báo btnBookTour
    private TextView tvDetailName, tvDetailAddress, tvDetailRating, tvDetailDescription;

    // Các view cho phần Review
    private LinearLayout layoutWriteReview, layoutMyReview;
    private RatingBar ratingBar, myRatingBar;
    private EditText edtReviewComment;
    private Button btnSubmitReview, btnEditReview, btnDeleteReview;
    private TextView tvMyComment;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String locationName;
    private String currentReviewId = null; // Lưu ID của bài đánh giá (nếu có)

    // Khai báo cho danh sách cộng đồng
    private RecyclerView rvReviews;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Ánh xạ
        imgDetail = findViewById(R.id.imgDetail);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailAddress = findViewById(R.id.tvDetailAddress);
        tvDetailRating = findViewById(R.id.tvDetailRating);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);

        layoutWriteReview = findViewById(R.id.layoutWriteReview);
        layoutMyReview = findViewById(R.id.layoutMyReview);
        ratingBar = findViewById(R.id.ratingBar);
        myRatingBar = findViewById(R.id.myRatingBar);
        edtReviewComment = findViewById(R.id.edtReviewComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        btnEditReview = findViewById(R.id.btnEditReview);
        btnDeleteReview = findViewById(R.id.btnDeleteReview);
        tvMyComment = findViewById(R.id.tvMyComment);

        btnOpenMap = findViewById(R.id.btnOpenMap);
        btnBookTour = findViewById(R.id.btnBookTour); // Ánh xạ nút đặt tour

        // Lấy dữ liệu locationName TỪ INTENT TRƯỚC (Rất quan trọng)
        locationName = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String description = getIntent().getStringExtra("description");
        double rating = getIntent().getDoubleExtra("rating", 0.0);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        tvDetailName.setText(locationName);
        tvDetailAddress.setText(address);
        tvDetailRating.setText("Đánh giá: " + rating + " ⭐");
        tvDetailDescription.setText(description);
        Glide.with(this).load(imageUrl).placeholder(android.R.drawable.ic_menu_gallery).into(imgDetail);

        // Cài đặt RecyclerView cho danh sách cộng đồng
        rvReviews = findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);
        rvReviews.setAdapter(reviewAdapter);

        // Gọi hàm tải danh sách đánh giá SAU KHI đã có locationName
        loadCommunityReviews();

        // Kiểm tra xem user đã review chưa
        checkUserReview();

        // Sự kiện click Đánh giá
        btnSubmitReview.setOnClickListener(v -> submitReview());

        btnEditReview.setOnClickListener(v -> {
            layoutMyReview.setVisibility(View.GONE);
            layoutWriteReview.setVisibility(View.VISIBLE);
            btnSubmitReview.setText("Cập nhật Đánh Giá");
        });

        btnDeleteReview.setOnClickListener(v -> deleteReview());

        // Sự kiện click Mở Bản Đồ (Đã dời ra khỏi hàm loadCommunityReviews)
        btnOpenMap.setOnClickListener(v -> {
            String exactLocation = locationName + ", " + tvDetailAddress.getText().toString();
            String searchQuery = "địa điểm du lịch nổi tiếng gần " + exactLocation;

            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(searchQuery));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            try {
                startActivity(mapIntent);
            } catch (android.content.ActivityNotFoundException e) {
                Uri browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(searchQuery));
                startActivity(new Intent(Intent.ACTION_VIEW, browserUri));
            }
        });

        // Sự kiện click Đặt Tour
        btnBookTour.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, BookingActivity.class);
            // Truyền tên khu du lịch sang màn hình Đặt Tour
            intent.putExtra("LOCATION_NAME", locationName);
            startActivity(intent);
        });
    }

    private void checkUserReview() {
        if (currentUser == null) return;

        db.collection("Reviews")
                .whereEqualTo("locationName", locationName)
                .whereEqualTo("userEmail", currentUser.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        currentReviewId = document.getId();

                        double savedRating = document.getDouble("rating");
                        String savedComment = document.getString("comment");

                        myRatingBar.setRating((float) savedRating);
                        tvMyComment.setText(savedComment);

                        ratingBar.setRating((float) savedRating);
                        edtReviewComment.setText(savedComment);

                        layoutWriteReview.setVisibility(View.GONE);
                        layoutMyReview.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void submitReview() {
        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        float userRating = ratingBar.getRating();
        String comment = edtReviewComment.getText().toString().trim();

        if (userRating == 0 || TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Vui lòng nhập đủ sao và bình luận!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> review = new HashMap<>();
        review.put("locationName", locationName);
        review.put("userEmail", currentUser.getEmail());
        review.put("rating", userRating);
        review.put("comment", comment);
        review.put("timestamp", System.currentTimeMillis());

        if (currentReviewId == null) {
            db.collection("Reviews").add(review).addOnSuccessListener(docRef -> {
                Toast.makeText(DetailActivity.this, "Đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
                checkUserReview();
                loadCommunityReviews();
            });
        } else {
            db.collection("Reviews").document(currentReviewId).update(review).addOnSuccessListener(aVoid -> {
                Toast.makeText(DetailActivity.this, "Đã cập nhật đánh giá!", Toast.LENGTH_SHORT).show();
                checkUserReview();
                loadCommunityReviews();
            });
        }
    }

    private void deleteReview() {
        if (currentReviewId != null) {
            db.collection("Reviews").document(currentReviewId).delete().addOnSuccessListener(aVoid -> {
                Toast.makeText(DetailActivity.this, "Đã xóa đánh giá!", Toast.LENGTH_SHORT).show();
                currentReviewId = null;
                ratingBar.setRating(0);
                edtReviewComment.setText("");
                btnSubmitReview.setText("Gửi Đánh Giá");

                layoutMyReview.setVisibility(View.GONE);
                layoutWriteReview.setVisibility(View.VISIBLE);

                loadCommunityReviews();
            });
        }
    }

    private void loadCommunityReviews() {
        if (locationName == null) return;

        db.collection("Reviews")
                .whereEqualTo("locationName", locationName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reviewList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Review review = doc.toObject(Review.class);

                        if (currentUser != null && review.getUserEmail().equals(currentUser.getEmail())) {
                            continue;
                        }

                        reviewList.add(review);
                    }
                    reviewAdapter.notifyDataSetChanged();
                });
    }
}