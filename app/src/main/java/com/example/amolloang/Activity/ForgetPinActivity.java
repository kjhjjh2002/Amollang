package com.example.amolloang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;

public class ForgetPinActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin);

        final EditText answerEditText = findViewById(R.id.forget_pin_answer_editText);


        String question = PreferenceManager.getString(this, "question");

        TextView questionTextView = findViewById(R.id.forget_pin_question_textView);
        questionTextView.setText(question);

        // 의도치 않은 공백생성 방지를 위해 trim() 사용
        final String answer = PreferenceManager.getString(this, "answer").trim();
        final String pin = PreferenceManager.getString(this, "setPin");

        Button okButton = findViewById(R.id.forget_pin_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String answerEdit = answerEditText.getText().toString().trim();

                if(answer.equals(answerEdit))
                    Toast.makeText(v.getContext(), "암호는 "+pin+" 입니다", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(v.getContext(), "보안질문에 대한 답이 일치하지않습니다", Toast.LENGTH_SHORT).show();

            }
        });

        Button backButton = findViewById(R.id.forget_pin_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


    }
}