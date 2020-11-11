package com.example.amolloang.Activity;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.amolloang.R;

import static android.content.ContentValues.TAG;

public class MoodDialog {

    private Context mContext;

    private Dialog dialog;

    private Button mood_1;
    private Button mood_2;
    private Button mood_3;
    private Button mood_4;
    private Button mood_5;
    private Button mood_6;
    private Button mood_7;
    private Button mood_8;
    private Button mood_9;
    private Button mood_10;
    private Button mood_11;
    private Button mood_12;

    private ImageView mood;

    private int buttonImage;

    public MoodDialog(Context context){
        mContext = context;
    }

    public void callDialog(ImageView mood){

        dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mood);
        dialog.show();

        this.mood = mood;

        initButtons();
        setButtonListener();
    }

    private void initButtons(){

        mood_1 = dialog.findViewById(R.id.mood_1);
        mood_2 = dialog.findViewById(R.id.mood_2);
        mood_3 = dialog.findViewById(R.id.mood_3);
        mood_4 = dialog.findViewById(R.id.mood_4);
        mood_5 = dialog.findViewById(R.id.mood_5);
        mood_6 = dialog.findViewById(R.id.mood_6);
        mood_7 = dialog.findViewById(R.id.mood_7);
        mood_8 = dialog.findViewById(R.id.mood_8);
        mood_9 = dialog.findViewById(R.id.mood_9);
        mood_10 = dialog.findViewById(R.id.mood_10);
        mood_11 = dialog.findViewById(R.id.mood_11);
        mood_12 = dialog.findViewById(R.id.mood_12);
    }

    private void setButtonListener(){

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, String.valueOf(v.getId()));
                switch (v.getId()){
                    case R.id.mood_1:
                        buttonImage = R.drawable.mood_1;
                        break;
                    case R.id.mood_2:
                        buttonImage = R.drawable.mood_2;
                        break;
                    case R.id.mood_3:
                        buttonImage = R.drawable.mood_3;
                        break;
                    case R.id.mood_4:
                        buttonImage = R.drawable.mood_4;
                        break;
                    case R.id.mood_5:
                        buttonImage = R.drawable.mood_5;
                        break;
                    case R.id.mood_6:
                        buttonImage = R.drawable.mood_6;
                        break;
                    case R.id.mood_7:
                        buttonImage = R.drawable.mood_7;
                        break;
                    case R.id.mood_8:
                        buttonImage = R.drawable.mood_8;
                        break;
                    case R.id.mood_9:
                        buttonImage = R.drawable.mood_9;
                        break;
                    case R.id.mood_10:
                        buttonImage = R.drawable.mood_10;
                        break;
                    case R.id.mood_11:
                        buttonImage = R.drawable.mood_11;
                        break;
                    case R.id.mood_12:
                        buttonImage = R.drawable.mood_12;
                        break;
                }

                mood.setImageResource(buttonImage);
                mood.setTag(buttonImage);
                dialog.dismiss();

            }
        };

        mood_1.setOnClickListener(listener);
        mood_2.setOnClickListener(listener);
        mood_3.setOnClickListener(listener);
        mood_4.setOnClickListener(listener);
        mood_5.setOnClickListener(listener);
        mood_6.setOnClickListener(listener);
        mood_7.setOnClickListener(listener);
        mood_8.setOnClickListener(listener);
        mood_9.setOnClickListener(listener);
        mood_10.setOnClickListener(listener);
        mood_11.setOnClickListener(listener);
        mood_12.setOnClickListener(listener);
    }
}
