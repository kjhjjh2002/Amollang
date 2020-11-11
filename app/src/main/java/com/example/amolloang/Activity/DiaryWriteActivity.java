package com.example.amolloang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amolloang.Database.DiaryContract;
import com.example.amolloang.Database.DiaryDbHelper;
import com.example.amolloang.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryWriteActivity extends AppCompatActivity {

    private String TAG = "DiaryWriteActivity";

    private long diaryId = -1;

    private EditText titleEditText;
    private EditText contentsEditText;

    private Button calendarButton;
    private ImageView moodButton;

    private String setDate;

    private int year;
    private int month;
    private int day;

    private boolean isSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        initDateToday();

        initToolbar();

        initTextView();

        initButtons();

    }

    private void initDateToday() {

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        Date date = new Date();

        year = Integer.parseInt(yearFormat.format(date));
        month = Integer.parseInt(monthFormat.format(date));
        day = Integer.parseInt(dayFormat.format(date));

        setDate = year + "-" + month + "-" + day;
    }

    private void initToolbar(){

        Toolbar toolbar = findViewById(R.id.diary_write_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#FF9335"));
    }

    private void initTextView(){

        titleEditText = findViewById(R.id.diary_write_title_editText);
        contentsEditText = findViewById(R.id.diary_write_contents_editText);

        Intent intent = getIntent();

        if (intent != null) {

            diaryId = intent.getLongExtra("id", -1);

            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");
            String date = intent.getStringExtra("date");
            String mood = intent.getStringExtra("mood");

            titleEditText.setText(title);
            contentsEditText.setText(contents);

            setDate = date;

            if (setDate != null && mood != null) {

                int moodImage = Integer.parseInt(mood);

                moodButton.setImageResource(moodImage);
                calendarButton.setText(setDate);

                // getImageResource 가 불가하여 Tag 에 이미지 넣어줌
                moodButton.setTag(moodImage);
            }
        }

    }

    private void initButtons(){

        calendarButton = findViewById(R.id.diary_write_calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoodImage();
                showDateDialog();
            }
        });

        moodButton = findViewById(R.id.diary_write_moodButton);
        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoodDialog moodDialog = new MoodDialog(DiaryWriteActivity.this);
                moodDialog.callDialog(moodButton);
            }
        });

    }

    private String getMoodImage() {

        // tag = R.drawable.mood_n
        return String.valueOf(moodButton.getTag());
    }



    private void showDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int y = year;
                int m = month;
                int d = dayOfMonth;

                setDate = y + "-" + m + "-" + d;
                calendarButton.setText(setDate);
            }
        }, year, month-1, day);

        datePickerDialog.setMessage("일기 날짜선택");
        datePickerDialog.show();

    }


    @Override
    public void onBackPressed() {

        String title = titleEditText.getText().toString();
        String contents = contentsEditText.getText().toString();

        // 변경점이 있을때 나가면 자동저장
        if(!title.equals("") && !contents.equals("") && !isSave)
            saveDiary();


        super.onBackPressed();
    }

    private void saveDiary() {

        String title = titleEditText.getText().toString();
        String contents = contentsEditText.getText().toString();
        String date = setDate;
        String mood = getMoodImage();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DiaryContract.DiaryEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(DiaryContract.DiaryEntry.COLUMN_NAME_CONTENTS, contents);
        contentValues.put(DiaryContract.DiaryEntry.COLUMN_NAME_DATE, date);
        contentValues.put(DiaryContract.DiaryEntry.COLUMN_NAME_MOOD, mood);

        SQLiteDatabase db = DiaryDbHelper.getInstance(this).getWritableDatabase();

        if (diaryId == -1) {
            //insert
            long newRowId = db.insert(DiaryContract.DiaryEntry.TABLE_NAME,
                    null,
                    contentValues);

            if (newRowId == -1) {
                Toast.makeText(this, "에러: 저장에 문제가 있습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        } else {
            //update
            int count = db.update(DiaryContract.DiaryEntry.TABLE_NAME,
                    contentValues, DiaryContract.DiaryEntry._ID +
                            " = " + diaryId, null);

            if (count == 0) {
                Toast.makeText(this, "에러: 수정에 문제가 있습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        // Back Button
        if (id == android.R.id.home)
            onBackPressed();


        // Save Button
        if (id == R.id.action_button) {
            isSave = true;

            saveDiary();
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_menu, menu);
        return true;
    }






}