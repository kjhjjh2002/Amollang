package com.example.amolloang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.amolloang.Database.DiaryContract;
import com.example.amolloang.Database.DiaryDbHelper;
import com.example.amolloang.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    String TAG = "ProfileActivity";

    int mood_1=0;
    int mood_2=0;
    int mood_3=0;
    int mood_4=0;
    int mood_5=0;
    int mood_6=0;
    int mood_7=0;
    int mood_8=0;
    int mood_9=0;
    int mood_10=0;
    int mood_11=0;
    int mood_12=0;

    ArrayList<Integer> moodList;
    ArrayList<Integer> moodIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initAdLoader();

        initTextView();

        initButton();

        initMoodIdList();

        initMoodData();

        initMoodChart();

    }

    private void initAdLoader(){

        AdLoader.Builder builder = new AdLoader.Builder(
                this.getApplicationContext(), "ca-app-pub-3940256099942544/2247696110"
        );

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView templateView = findViewById(R.id.profileAdView);
                templateView.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void initTextView(){

        String count = String.valueOf(getDiaryCount());

        TextView totalDiaryCount = findViewById(R.id.total_diary_count_textView);
        totalDiaryCount.setText(count);
    }

    private void initButton(){

        Button button = findViewById(R.id.diary_profile_back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initMoodIdList(){

        moodIdList = new ArrayList<>();

        // 감정목록 이미지 추가
        moodIdList.add(R.drawable.mood_1);
        moodIdList.add(R.drawable.mood_2);
        moodIdList.add(R.drawable.mood_3);
        moodIdList.add(R.drawable.mood_4);
        moodIdList.add(R.drawable.mood_5);
        moodIdList.add(R.drawable.mood_6);
        moodIdList.add(R.drawable.mood_7);
        moodIdList.add(R.drawable.mood_8);
        moodIdList.add(R.drawable.mood_9);
        moodIdList.add(R.drawable.mood_10);
        moodIdList.add(R.drawable.mood_11);
        moodIdList.add(R.drawable.mood_12);
    }

    private void initMoodChart(){

        BarChart barChart = findViewById(R.id.profileChart);

        List<BarEntry> entries = new ArrayList<>();

        for(int i=0; i<moodIdList.size();i++)
            entries.add(new BarEntry(i+1, moodList.get(i), moodIdList.get(i)));


        for (int i=0; i<moodIdList.size();i++){

            @SuppressLint("UseCompatLoadingForDrawables")
            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(moodIdList.get(i));
            Bitmap bitmap = drawable.getBitmap();

            Drawable icon = BitmapResizePrc(bitmap, 70, 70);
            entries.get(i).setIcon(icon);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "지금까지 나의 기분");
        barChart.getLegend().setTextColor(Color.WHITE);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getXAxis().setEnabled(false);

        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setTextColor(Color.WHITE);

        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(2000);
        barChart.invalidate();

        barChart.getDescription().setEnabled(false);
    }

    private BitmapDrawable BitmapResizePrc( Bitmap Src, int newHeight, int newWidth){

        BitmapDrawable Result = null;

        int width = Src.getWidth();
        int height = Src.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // rotate the Bitmap
        //matrix.postRotate(45);

        Bitmap resizedBitmap = Bitmap.createBitmap(Src, 0, 0, width, height, matrix, true);

        // check
        width = resizedBitmap.getWidth();
        height = resizedBitmap.getHeight();

        Log.i(TAG, "Result : " + ((newHeight==height)&&(newWidth==width)) );

        Result = new BitmapDrawable(resizedBitmap);

        return Result;
    }

    private int getDiaryCount(){

        SQLiteDatabase db = DiaryDbHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ DiaryContract.DiaryEntry.TABLE_NAME, null);

        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

    private void initMoodData(){

        ArrayList<String> moodId = new ArrayList<>();

        SQLiteDatabase db = DiaryDbHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+"mood"+" FROM "+DiaryContract.DiaryEntry.TABLE_NAME, null);

        while (cursor.moveToNext()){
            moodId.add(cursor.getString(0));
        }

        cursor.close();

        moodList = new ArrayList<>();

        // 공간 12개 창출
        for(int i=0; i<12; i++){
            moodList.add(0);
        }

        for(int i = 0; i< moodId.size(); i++){

            int id = Integer.parseInt(moodId.get(i));

            switch (id){
                case R.drawable.mood_1:
                    int val = moodList.get(0);
                    moodList.set(0, val+1);
                    mood_1++;
                    break;
                case R.drawable.mood_2:
                    int val2 = moodList.get(1);
                    moodList.set(1, val2+1);
                    mood_2++;
                    break;
                case R.drawable.mood_3:
                    int val3 = moodList.get(2);
                    moodList.set(2, val3+1);
                    mood_3++;
                    break;
                case R.drawable.mood_4:
                    int val4 = moodList.get(3);
                    moodList.set(3, val4+1);
                    mood_4++;
                    break;
                case R.drawable.mood_5:
                    int val5 = moodList.get(4);
                    moodList.set(4, val5+1);
                    mood_5++;
                    break;
                case R.drawable.mood_6:
                    int val6 = moodList.get(5);
                    moodList.set(5, val6+1);
                    mood_6++;
                    break;
                case R.drawable.mood_7:
                    int val7 = moodList.get(6);
                    moodList.set(6, val7+1);
                    mood_7++;
                    break;
                case R.drawable.mood_8:
                    int val8 = moodList.get(7);
                    moodList.set(7, val8+1);
                    mood_8++;
                    break;
                case R.drawable.mood_9:
                    int val9 = moodList.get(8);
                    moodList.set(8, val9+1);
                    mood_9++;
                    break;
                case R.drawable.mood_10:
                    int val10 = moodList.get(9);
                    moodList.set(9, val10+1);
                    mood_10++;
                    break;
                case R.drawable.mood_11:
                    int val11 = moodList.get(10);
                    moodList.set(10, val11+1);
                    mood_11++;
                    break;
                case R.drawable.mood_12:
                    int val12 = moodList.get(11);
                    moodList.set(11, val12+1);
                    mood_12++;
                    break;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}