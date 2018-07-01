package com.example.dimon.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // https://habr.com/sandbox/34130/
        // https://startandroid.ru/ru/uroki/vse-uroki-spiskom/204-urok-119-pendingintent-flagi-requestcode-alarmmanager.html
        // https://www.youtube.com/watch?v=s1TVD0d0tBE

        /*NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.mipmap.ic_launcher, "Test", System.currentTimeMillis());
//Интент для активити, которую мы хотим запускать при нажатии на уведомление
        Intent intentTL = new Intent(context, MainActivity.class);
        notification.setLatestEventInfo(context, "Test", "Do something!",
                PendingIntent.getActivity(context, 0, intentTL,
                        PendingIntent.FLAG_CANCEL_CURRENT));
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notification);
// Установим следующее напоминание.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);*/
    }
}
