package com.example.amolloang.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amolloang.Database.DiaryContract;
import com.example.amolloang.Database.DiaryDbHelper;
import com.example.amolloang.R;

public class DiaryListActivity extends AppCompatActivity {

    private String TAG = "DiaryListActivity";

    public static final int REQUEST_CODE_INSERT = 1000;

    private DiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        initDiaryList();

        initButtons();
    }

    private void initDiaryList(){

        ListView listView = findViewById(R.id.diary_listView);

        // 일기 터치시 편집이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(DiaryListActivity.this, DiaryWriteActivity.class);

                Cursor cursor = (Cursor) diaryAdapter.getItem(position);

                String title = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_TITLE));
                String contents = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_CONTENTS));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_DATE));
                String mood = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_MOOD));

                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);
                intent.putExtra("date", date);
                intent.putExtra("mood", mood);

                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        // 길게 터치시 삭제 다이어로그
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryListActivity.this);

                builder.setTitle("일기삭제");
                builder.setMessage("일기를 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = DiaryDbHelper.getInstance(DiaryListActivity.this).getWritableDatabase();
                        int deletedCount = db.delete(DiaryContract.DiaryEntry.TABLE_NAME,
                                DiaryContract.DiaryEntry._ID+" = "+id, null);
                        if(deletedCount == 0){
                            Toast.makeText(DiaryListActivity.this,"삭제에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            diaryAdapter.swapCursor(getDiaryCursor());
                            Toast.makeText(DiaryListActivity.this,"삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("취소", null);
                builder.show();

                return true;
            }
        });

        // 커서 설정후 어댑터 셋
        Cursor cursor = getDiaryCursor();

        diaryAdapter = new DiaryAdapter(this, cursor);

        listView.setAdapter(diaryAdapter);
    }

    private Cursor getDiaryCursor(){

        DiaryDbHelper dbHelper = DiaryDbHelper.getInstance(this);

        return dbHelper.getReadableDatabase()
                .query(DiaryContract.DiaryEntry.TABLE_NAME,
                        null,null,null,null,null,
                        DiaryContract.DiaryEntry._ID+" DESC");
    }


    private void initButtons(){
        Button backButton = findViewById(R.id.diary_list_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 일기의 변경이 즉시 적용되도록 일기저장시에 커서 변경
        if(requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK){
            diaryAdapter.swapCursor(getDiaryCursor());
        }
    }



    private static class DiaryAdapter extends CursorAdapter{

        private String TAG = "DiaryAdapter";

        public DiaryAdapter(Context context, Cursor c) {

            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            return LayoutInflater.from(context)
                    .inflate(R.layout.diary_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView yearTextView = view.findViewById(R.id.diary_year_textView);
            TextView dateTextView = view.findViewById(R.id.diary_date_textView);
            TextView titleTextView = view.findViewById(R.id.diary_title_textView);
            ImageView moodImageView = view.findViewById(R.id.diary_image_textView);

            String title = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_DATE));
            String mood = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DiaryEntry.COLUMN_NAME_MOOD));

            titleTextView.setText(title);


            int moodImage;

            if (!mood.equals("null")){

                moodImage = Integer.parseInt(mood);
                moodImageView.setImageResource(moodImage);
            }

            // 기본값일 경우 mood1로 설정
            if (mood.equals("2131165320"))
                moodImageView.setImageResource(R.drawable.mood_1);

            if(date != null){

                String[] dateList = date.split("-");
                String year = dateList[0];
                String monthDay = dateList[1]+"/"+dateList[2];

                yearTextView.setText(year);
                dateTextView.setText(monthDay);
            }

        }
    }
}