package com.example.amolloang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;

public class PinLockSettingActivity extends AppCompatActivity {

    CheckBox lockCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock_setting);

        initButton();
    }

    private void initButton(){

        Button backButton = findViewById(R.id.diary_setting_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayout setQuestionButton = findViewById(R.id.diary_setQuestion_LinearLayout);
        setQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LockQuestionActivity.class));
            }
        });

        lockCheckBox = findViewById(R.id.diary_lock_checkBox);
        lockCheckBox.setChecked(PreferenceManager.getBoolean(this, "isLock"));
        lockCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = PreferenceManager.getBoolean(v.getContext(), "isLock");
                PreferenceManager.setBoolean(v.getContext(), "isLock", !isCheck);

                if(lockCheckBox.isChecked()){
                    Intent intent = new Intent(v.getContext(), PinLockActivity.class);
                    intent.putExtra("type", "set");
                    intent.putExtra("first", true);
                    startActivity(intent);
                }
            }
        });

        LinearLayout setPinButton = findViewById(R.id.diary_setPin_LinearLayout);
        setPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PinLockActivity.class);
                intent.putExtra("type", "set");

                startActivity(intent);
            }
        });
    }
}