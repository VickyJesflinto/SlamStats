package com.example.slamstat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        String title = getIntent().getStringExtra("title");
        String source = getIntent().getStringExtra("source");
        String url = getIntent().getStringExtra("url");

        ImageView imageView = findViewById(R.id.iv_news_image);
        TextView titleTextView = findViewById(R.id.tv_title);
        TextView sourceTextView = findViewById(R.id.tv_news_source);
        Button btnUrl = findViewById(R.id.btn_url);

        titleTextView.setText(title);
        sourceTextView.setText(source);

        btnUrl.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }
}
