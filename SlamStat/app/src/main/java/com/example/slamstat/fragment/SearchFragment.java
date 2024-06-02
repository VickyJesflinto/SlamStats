package com.example.slamstat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchFragment extends Fragment {
    private ApiService apiService;
    private NBANewsAdapter nbaNewsAdapter;
    private TextView error;
    private SearchView searchView;
    private ProgressBar progressBar;
    private Context context;
    private final ArrayList<NBANews> nbaNews = new ArrayList<>();
    private ExecutorService executorService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rv_search);
        error = view.findViewById(R.id.error);
        searchView = view.findViewById(R.id.search_view);  // This should be androidx.appcompat.widget.SearchView
        progressBar = view.findViewById(R.id.progress_bar);
        context = getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        nbaNewsAdapter = new NBANewsAdapter(nbaNews, context);
        recyclerView.setAdapter(nbaNewsAdapter);

        executorService = Executors.newSingleThreadExecutor();

        fetchData();

        setupSearchView();
    }

    private void fetchData() {
        showProgressBar();
        executorService.execute(() -> {
            Call<List<NBANews>> call = apiService.getNBANews();
            try {
                Response<List<NBANews>> response = call.execute();
                if (response.isSuccessful()) {
                    List<NBANews> data = response.body();
                    if (data != null) {
                        nbaNews.clear();
                        nbaNews.addAll(data);
                        getActivity().runOnUiThread(() -> {
                            nbaNewsAdapter.notifyDataSetChanged();
                            hideProgressBar();
                        });
                    } else {
                        showToast("No data available");
                        hideProgressBar();
                    }
                } else {
                    showToast("Failed to fetch data");
                    hideProgressBar();
                }
            } catch (Exception e) {
                showError();
                hideProgressBar();
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showProgressBar();
                filterNewsWithDelay(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showProgressBar();
                filterNewsWithDelay(newText);
                return false;
            }
        });
    }

    private void filterNewsWithDelay(String query) {
        // Show progress bar and simulate network delay
        showProgressBar();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            List<NBANews> filteredList = new ArrayList<>();
            for (NBANews news : nbaNews) {
                if (news.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(news);
                }
            }
            nbaNewsAdapter.updateList(filteredList);
            hideProgressBar();
        }, 2000);  // 1 second delay
    }

    private void showToast(String message) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void showError() {
        getActivity().runOnUiThread(() -> {
            error.setVisibility(View.VISIBLE);
        });
    }

    private void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
        });
    }
}
