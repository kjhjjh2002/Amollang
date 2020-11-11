package com.example.amolloang.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;

import java.util.ArrayList;

public class PinLockActivity extends AppCompatActivity {

    private String TAG = "PinLockActivity";

    private ArrayList<ImageView> pinImageViewList;

    private TextView pinLockTextView;

    private AlertDialog alertDialog;

    private String userPin = "";
    private String pinLockType = "";
    private String resetPin = "";

    private boolean isRetry = false;

    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock);

        isFirst = getIntent().getBooleanExtra("first", false);

        initPinImage();

        initTextView();

        initButtons();
    }

    private void initPinImage(){

        ImageView pin1 = findViewById(R.id.pinImage1);
        ImageView pin2 = findViewById(R.id.pinImage2);
        ImageView pin3 = findViewById(R.id.pinImage3);
        ImageView pin4 = findViewById(R.id.pinImage4);

        Log.e(TAG, pin1+"");
        pinImageViewList = new ArrayList<>();
        pinImageViewList.add(pin1);
        pinImageViewList.add(pin2);
        pinImageViewList.add(pin3);
        pinImageViewList.add(pin4);
    }

    private void initTextView(){

        pinLockTextView = findViewById(R.id.pinLock_textView);

        pinLockType = getIntent().getStringExtra("type");

        assert pinLockType != null;
        setPinLockText(pinLockType);
    }

    private void setPinLockText(String pinLockType){

        String text = "";
        switch (pinLockType){
            case "lock":
                text = "암호를 입력하세요";
                break;
            case "set":
                text = "설정할 암호를 입력하세요";
                break;
            case "retry":
                text = "다시한번 입력하세요";
                break;
        }

        pinLockTextView.setText(text);
    }

    private void initButtons(){

        Button pin1 = findViewById(R.id.pin_button_1);
        pin1.setOnClickListener(clickListener);

        Button pin2 = findViewById(R.id.pin_button_2);
        pin2.setOnClickListener(clickListener);

        Button pin3 = findViewById(R.id.pin_button_3);
        pin3.setOnClickListener(clickListener);

        Button pin4 = findViewById(R.id.pin_button_4);
        pin4.setOnClickListener(clickListener);

        Button pin5 = findViewById(R.id.pin_button_5);
        pin5.setOnClickListener(clickListener);

        Button pin6 = findViewById(R.id.pin_button_6);
        pin6.setOnClickListener(clickListener);

        Button pin7 = findViewById(R.id.pin_button_7);
        pin7.setOnClickListener(clickListener);

        Button pin8 = findViewById(R.id.pin_button_8);
        pin8.setOnClickListener(clickListener);

        Button pin9 = findViewById(R.id.pin_button_9);
        pin9.setOnClickListener(clickListener);

        Button pin0 = findViewById(R.id.pin_button_0);
        pin0.setOnClickListener(clickListener);

        Button forgetPinButton = findViewById(R.id.forget_pin_button);
        forgetPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceManager.getString(v.getContext(), "question") != null)
                    startActivity(new Intent(v.getContext(), ForgetPinActivity.class));
                else
                    Toast.makeText(v.getContext(), "저장된 보안질문이 없습니다", Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinDelete();
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.pin_button_1:
                    addPinNumAndCheck(1);
                    break;
                case R.id.pin_button_2:
                    addPinNumAndCheck(2);
                    break;
                case R.id.pin_button_3:
                    addPinNumAndCheck(3);
                    break;
                case R.id.pin_button_4:
                    addPinNumAndCheck(4);
                    break;
                case R.id.pin_button_5:
                    addPinNumAndCheck(5);
                    break;
                case R.id.pin_button_6:
                    addPinNumAndCheck(6);
                    break;
                case R.id.pin_button_7:
                    addPinNumAndCheck(7);
                    break;
                case R.id.pin_button_8:
                    addPinNumAndCheck(8);
                    break;
                case R.id.pin_button_9:
                    addPinNumAndCheck(9);
                    break;
                case R.id.pin_button_0:
                    addPinNumAndCheck(0);
                    break;
            }
        }
    };


    private void addPinNumAndCheck(int num){
        if(userPin.length() < 4)
            userPin += String.valueOf(num);

        pinImageChange(userPin.length());

        if(userPin.length() > 3){
            if(pinLockType.equals("set"))
                resetPinNum();
            else
                checkPinCorrect();
        }

    }

    private void checkPinCorrect() {
        boolean isSuccess = true;

        if(isRetry){
            if(resetPin.equals(userPin)){
                PreferenceManager.setString(this, "setPin", resetPin);
                //startActivity(new Intent(this, LockQuestionActivity.class));
            }
            else
                isSuccess = false;
        } else {
            if(!userPin.equals(PreferenceManager.getString(this, "setPin")))
                isSuccess = false;
        }

        if(isSuccess)
            pinCorrectTrue();
        else
            pinCorrectFalse();
    }

    private void pinCorrectTrue() {
        if(pinLockType.equals("lock")){
            Log.e(TAG, "isFirst: "+isFirst);
            if(isFirst){
                Intent intent = new Intent(this, LockQuestionActivity.class);
                intent.putExtra("first", true);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, CenterActivity.class);
                intent.putExtra("unlock", true);
                startActivity(intent);
            }
        }else{
            resetPin();
        }
    }

    private void pinCorrectFalse() {
        TextView pinFail = findViewById(R.id.pinLockFailTextView);
        pinFail.setVisibility(View.VISIBLE);
        resetPin();
    }

    private void resetPin() {
        userPin = "";

        for(int i=0; i<=3; i++)
            pinImageViewList.get(i).setImageResource(R.drawable.pin_underbar);
    }

    private void resetPinNum() {
        resetPin = "";

        String[] pin = userPin.split("");
        for(int i=0; i<=3; i++)
            resetPin += pin[i];

        isRetry = true;
        pinLockType = "lock";
        setPinLockText("retry");

        resetPin();
    }


    private void pinImageChange(int index){
        int changeIndex = index-1;
        pinImageViewList.get(changeIndex).setImageResource(R.drawable.not_null);
    }

    private void pinImageDelete(int index){
        pinImageViewList.get(index).setImageResource(R.drawable.pin_underbar);
    }

    private void pinDelete(){

        if(userPin.length() != 0){

            String[] pin = userPin.split("");

            userPin = "";

            for(int i=0; i<pin.length-1; i++){
                userPin += pin[i];
            }

            pinImageDelete(userPin.length());
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