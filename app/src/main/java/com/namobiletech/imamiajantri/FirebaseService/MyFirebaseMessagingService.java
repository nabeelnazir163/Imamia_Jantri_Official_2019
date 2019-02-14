package com.namobiletech.imamiajantri.FirebaseService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.namobiletech.imamiajantri.R;
import com.namobiletech.imamiajantri.UI.Activities.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(remoteMessage.getNotification().getTitle());
        notification.setContentText(remoteMessage.getNotification().getBody());
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.notification_icon);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification.build());

        super.onMessageReceived(remoteMessage);
    }
}
