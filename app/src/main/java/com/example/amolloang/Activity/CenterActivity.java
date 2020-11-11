package com.example.amolloang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.amolloang.Database.DiaryContract;
import com.example.amolloang.Database.DiaryDbHelper;
import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CenterActivity extends AppCompatActivity {

    private String TAG = "CenterActivity";

    public static final int REQUEST_CODE_INSERT = 1000;

    private DrawerLayout drawerLayout;
    private View drawerView;

    private AlertDialog alertDialog;

    private TextView randText;

    int randNum;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        checkUnlockPin();

        initDrawerAndToolbar();

        initRandDiary();

        initButtons();

        initMobileAd();

    }

    private void checkUnlockPin(){
        // 잠금해제여부
        boolean isUnlock = getIntent().getBooleanExtra("unlock", false);

        if(!isUnlock){
            if(PreferenceManager.getBoolean(this,"isLock")){
                Intent intent = new Intent(this, PinLockActivity.class);
                intent.putExtra("type", "lock");
                startActivity(intent);
            }
        }
    }

    private void initDrawerAndToolbar(){

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawerView);

        Toolbar toolbar = findViewById(R.id.center_Toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        toolbar.setTitleTextColor(Color.parseColor("#FF9335"));
        getSupportActionBar().setTitle("아몰랑");
    }


    private void initRandDiary(){
        randText = findViewById(R.id.rand_textView);
        randText.setText(getRandTitle());
    }

    private String getRandTitle() {

        ArrayList<String> titleList = new ArrayList<>();

        int count = getDiaryCount();
        randNum = (int) (Math.random()*count+0);

        DiaryDbHelper dbHelper = DiaryDbHelper.getInstance(this);
        Cursor cursor = dbHelper.getReadableDatabase()
                .query(DiaryContract.DiaryEntry.TABLE_NAME,
                        null,null,null,null,null,
                        DiaryContract.DiaryEntry._ID+" DESC");

        while (cursor.moveToNext()){
            // 1: diary title
            titleList.add(cursor.getString(1));
        }

        return titleList.get(randNum);
    }

    private int getDiaryCount(){

        SQLiteDatabase db = DiaryDbHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DiaryContract.DiaryEntry.TABLE_NAME, null);

        int cnt = cursor.getCount();

        cursor.close();
        return cnt;
    }


    private void initButtons(){

        FloatingActionButton addDiaryButton = findViewById(R.id.diary_add_button);
        FloatingActionButton diaryListButton = findViewById(R.id.diary_list_button);
        FloatingActionButton profileButton = findViewById(R.id.user_profile_button);

        addDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DiaryWriteActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        diaryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DiaryListActivity.class);
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button goAlarm = findViewById(R.id.goAlarm);
        goAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        Button goLock = findViewById(R.id.goLock);
        goLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PinLockSettingActivity.class);
                startActivity(intent);
            }
        });

        Button feedback = findViewById(R.id.goFeedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        CardView randDiaryButton = findViewById(R.id.rand_Button);
        randDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRandDiary();
            }
        });
    }

    private void goRandDiary(){

        SQLiteDatabase sqLiteDatabase = DiaryDbHelper.getInstance(this).getReadableDatabase();
        Intent intent = new Intent(CenterActivity.this, RandomDiaryActivity.class);

        String text = randText.getText().toString();
        String sql = "SELECT * FROM "+DiaryContract.DiaryEntry.TABLE_NAME+" WHERE title = '"+text+"';";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        String title ="";
        String contents = "";
        String date = "";
        String mood = "";

        while (cursor.moveToNext()){
            title = cursor.getString(1);
            contents = cursor.getString(2);
            date = cursor.getString(3);
            mood = cursor.getString(4);
        }


        intent.putExtra("id", randNum);
        intent.putExtra("title", title);
        intent.putExtra("contents", contents);
        intent.putExtra("date", date);
        intent.putExtra("mood", mood);

        startActivity(intent);
    }


    private void initMobileAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }


    private void sendFeedback() {

        String[] email = {"absolutetrust2335@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "User Feedback");
        intent.setType("message/rfc822");

        startActivity(Intent.createChooser(intent, "이메일 전송 수단"));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(drawerView);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("아몰랑")
                .setMessage("앱을 종료하시겠습니까?")
                .setIcon(R.drawable.egg_no_background);


        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                exitApplication();
            }
        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ffffff"));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ffffff"));
            }
        });

        alertDialog.show();
    }

    private void exitApplication() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}