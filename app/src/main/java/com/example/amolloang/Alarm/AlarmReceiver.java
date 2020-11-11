package com.example.amolloang.Alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import com.example.amolloang.Activity.CenterActivity;
import com.example.amolloang.Database.PreferenceManager;
import com.example.amolloang.R;

import java.util.Calendar;


import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, CenterActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // 알림 클릭시 이동할 엑티비티
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");


        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            builder.setSmallIcon(R.drawable.egg_no_background);

            String channelName ="AlarmChannel";
            String description = "Alarm";

            //알림 중요도 설정
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                //알림 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        }else{
            // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }


        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("아몰랑")
                .setContentText("우리 일기쓰러가요!")
                .setContentInfo("알림을 눌려서 일기쓰러가기!")
                .setContentIntent(pendingIntent);


        boolean isAlarm = PreferenceManager.getBoolean(context, "isAlarm");

        if (notificationManager != null && isAlarm) {

            notificationManager.notify(1234, builder.build());

            //내일 같은 시간으로 알람시간 결정
            Calendar nextNotifyTime = Calendar.getInstance();
            nextNotifyTime.add(Calendar.DATE, 1);

            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor.putLong("nextAlarmTime", nextNotifyTime.getTimeInMillis());
            editor.apply();
        }
    }
}
