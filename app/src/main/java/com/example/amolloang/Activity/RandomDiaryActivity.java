package com.example.amolloang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amolloang.R;

public class RandomDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_diary);

        initView();

        initButton();
    }

    private void initView(){

        TextView titleTextView = findViewById(R.id.diary_rand_title_textView);
        TextView contentsTextView = findViewById(R.id.diary_rand_contents_textView);
        TextView dateTextView = findViewById(R.id.diary_rand_date_textView);
        ImageView moodImageView = findViewById(R.id.diary_rand_mood_imageView);

        Intent intent = getIntent();
        if(intent != null) {

            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");
            String date = intent.getStringExtra("date");
            String mood = intent.getStringExtra("mood");

            titleTextView.setText("Title.\n"+title);
            contentsTextView.setText(contents);
            dateTextView.setText(date);

            int moodImage = Integer.parseInt(mood);

            moodImageView.setImageResource(moodImage);
        }
    }

    private void initButton(){

        Button backButton = findViewById(R.id.rand_diary_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}