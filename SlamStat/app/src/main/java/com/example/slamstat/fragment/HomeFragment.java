package com.example.slamstat.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slamstat.DB.DbConfig;
import com.example.slamstat.models.NBANews;
import com.example.slamstat.adapter.NBANewsAdapter;
import com.example.slamstat.R;
import com.example.slamstat.api.ApiService;
import com.example.slamstat.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final int DELAY_MILLIS = 2000;  // 2 seconds delay
    private String apiKey = "5abac1bbbcmsh7caa37cb8c257e6p12257bjsn02d2e52ffb10";
    private String apiHost = "nba-latest-news.p.rapidapi.com";
    private ApiService apiService;
    private NBANewsAdapter nbaNewsAdapter;
    private TextView error, tv1, tv2, tvname;
    private ImageView loadingIcon;
    private Button btnTryAgain;
    private Context context;
    private final ArrayList<NBANews> nbaNews = new ArrayList<>();
    private ExecutorService executorService;
    private Handler mainHandler;
    private SharedPreferences sharedPreferences;
    private DbConfig dbConfig;

    private ObjectAnimator rotateAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rv_home);
        error = view.findViewById(R.id.error);
        loadingIcon = view.findViewById(R.id.loading_icon);
        btnTryAgain = view.findViewById(R.id.btn_try_again);
        context = getContext();
        tv1 = view.findViewById(R.id.tv_homefullname);
        tv2 = view.findViewById(R.id.tv_recommended);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        sharedPreferences = requireActivity().getSharedPreferences("user_pref", requireActivity().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);
        tvname = view.findViewById(R.id.tv_homefullname);
        dbConfig = new DbConfig(requireActivity());
        Cursor cursor = dbConfig.getUserDataById(userId);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                tvname.setText("Hi, " + name + " !");
            } while (cursor.moveToNext());
        }
        nbaNewsAdapter = new NBANewsAdapter(nbaNews, context);
        recyclerView.setAdapter(nbaNewsAdapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        btnTryAgain.setOnClickListener(v -> fetchData());

        fetchData();
    }

    private void fetchData() {
        showLoadingIcon();
        hideError();
        executorService.execute(() -> {
            Call<List<NBANews>> call = apiService.getNBANews();
            try {
                Response<List<NBANews>> response = call.execute();
                mainHandler.postDelayed(() -> {  // Introduce a delay
                    if (response.isSuccessful()) {
                        List<NBANews> data = response.body();
                        if (data != null) {
                            nbaNews.clear();
                            int limit = Math.min(data.size(), 10);  // Limit to 10 items
                            for (int i = 0; i < limit; i++) {
                                nbaNews.add(data.get(i));
                            }
                            nbaNewsAdapter.notifyDataSetChanged();
                        } else {
                            showToast("No data available");
                        }
                    } else {
                        showToast("Failed to fetch data");
                    }
                    hideLoadingIcon();
                }, DELAY_MILLIS);
            } catch (Exception e) {
                mainHandler.post(() -> {
                    showError();
                    hideLoadingIcon();
                });
            }
        });
    }

    private void showToast(String message) {
        mainHandler.post(() -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void showLoadingIcon() {
        mainHandler.post(() -> {
            loadingIcon.setVisibility(View.VISIBLE);
            rotateAnimator = ObjectAnimator.ofFloat(loadingIcon, "rotation", 0f, 360f);
            rotateAnimator.setDuration(1000);  // Rotation duration in milliseconds
            rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Repeat indefinitely
            rotateAnimator.start();
        });
    }

    private void hideLoadingIcon() {
        mainHandler.post(() -> {
            rotateAnimator.end();
            loadingIcon.setVisibility(View.GONE);
        });
    }

    private void showError() {
        mainHandler.post(() -> {
            error.setVisibility(View.VISIBLE);
            btnTryAgain.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
        });
    }

    private void hideError() {
        mainHandler.post(() -> {
            error.setVisibility(View.GONE);
            btnTryAgain.setVisibility(View.GONE);
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
        });
    }
}
