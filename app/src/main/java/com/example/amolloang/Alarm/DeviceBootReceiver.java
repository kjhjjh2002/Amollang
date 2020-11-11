package com.example.amolloang.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.amolloang.Alarm.AlarmReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // 디바이스 부팅이 완료되었을때 수행
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            SharedPreferences sharedPreferences = context.getSharedPreferences("alarm", MODE_PRIVATE);

            Calendar current_calendar = Calendar.getInstance();

            Calendar nextNotifyTime = new GregorianCalendar();

            long millis = sharedPreferences.getLong("nextAlarmTime", Calendar.getInstance().getTimeInMillis());
            nextNotifyTime.setTimeInMillis(sharedPreferences.getLong("nextAlarmTime", millis));

            // 현재 시간이 설정시간 이후라면 다음날 알람 설정
            if (current_calendar.after(nextNotifyTime)) {
                nextNotifyTime.add(Calendar.DATE, 1);
            }

            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }

    }

}
