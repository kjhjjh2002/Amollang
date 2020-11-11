package com.example.amolloang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.amolloang.Alarm.AlarmReceiver;
import com.example.amolloang.Alarm.DeviceBootReceiver;
import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    private String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        final TimePicker picker = findViewById(R.id.alarm_timePicker);
        picker.setIs24HourView(true);

        SharedPreferences sharedPreferences = getSharedPreferences("alarm", MODE_PRIVATE);

        // 저장된 시간을 준다 저장된 시간이 없을경우 현재시간을 준다
        long millis = sharedPreferences.getLong("nextAlarmTime", Calendar.getInstance().getTimeInMillis());
        Calendar nextAlarmTime = new GregorianCalendar();
        nextAlarmTime.setTimeInMillis(millis);


        Date nextDate = nextAlarmTime.getTime();
        String dateText = new SimpleDateFormat("hh시 mm분", Locale.getDefault()).format(nextDate);
        Toast.makeText(getApplicationContext(), "알림이 "+dateText+"으로 설정되었습니다", Toast.LENGTH_SHORT).show();


        // 설정된 값으로 TimePicker 초기화
        Date currentTime = nextAlarmTime.getTime();
        SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

        int hour = Integer.parseInt(HourFormat.format(currentTime));
        int minute = Integer.parseInt(MinuteFormat.format(currentTime));

        picker.setHour(hour);
        picker.setMinute(minute);



        Button setAlarmButton = findViewById(R.id.set_alarm_button);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setBoolean(v.getContext(), "isAlarm", true);
                int hour_24, minute;

                hour_24 = picker.getHour();
                minute = picker.getMinute();

                // 현재 지정된 시간으로 알람 시간 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                //이미 지난시간이라면 다음날 같은 시간으로
                if(calendar.before(Calendar.getInstance()))
                    calendar.add(Calendar.DATE,1 );

                Date currentDateTime = calendar.getTime();
                String dateText = new SimpleDateFormat("hh시 mm분",Locale.getDefault()).format(currentDateTime);

                Toast.makeText(v.getContext(), dateText+"으로 알림이 설정되었습니다", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = getSharedPreferences("alarm", MODE_PRIVATE).edit();
                editor.putLong("nextAlarmTime", calendar.getTimeInMillis());
                editor.apply();

                diaryNotification(calendar);
            }
        });

        Button unSetAlarmButton = findViewById(R.id.unSet_alarm_button);
        unSetAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setBoolean(v.getContext(), "isAlarm", false);
                Toast.makeText(v.getContext(), "알림 설정이 해제되었습니다", Toast.LENGTH_SHORT).show();

            }
        });

        Button backButton = findViewById(R.id.alarm_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void diaryNotification(Calendar calendar){

        PackageManager packageManager = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(alarmManager != null){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        // 부팅 후 실행되는 리시버 사용가능하게 설정
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}