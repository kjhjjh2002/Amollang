package com.example.amolloang.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;

public class LockQuestionActivity extends AppCompatActivity {

    private String TAG = "LockQuestionActivity";

    private AlertDialog alertDialog;

    private EditText questionEditText;
    private EditText answerEditText;

    private Button backButton;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_question);

        initTextView();

        initButtons();

        initActivitySet();

    }


    private void initTextView(){

        questionEditText = findViewById(R.id.diary_lockQuestion_question_editText);
        answerEditText = findViewById(R.id.diary_lockQuestion_answer_editText);
    }

    private void initButtons(){

        Button saveQuestionButton = findViewById(R.id.diary_lockQuestion_Ok_button);
        saveQuestionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String question = questionEditText.getText().toString();
                String answer = answerEditText.getText().toString();

                PreferenceManager.setString(v.getContext(), "question", question);
                PreferenceManager.setString(v.getContext(), "answer", answer);

                Toast.makeText(v.getContext(), "보안질문이 설정되었습니다", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), CenterActivity.class);
                intent.putExtra("unlock", true);

                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.diary_lockQuestion_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initActivitySet(){

        // pin 설정 후 자동으로 보안질문 설정으로 넘어왔을때
        boolean isFirst = getIntent().getBooleanExtra("first", false);
        if(isFirst){

            backButton.setEnabled(false);
            backButton.setBackgroundColor(Color.parseColor("#00000000"));
        }
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