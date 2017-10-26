package com.dhdigital.lms.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dhdigital.lms.R;
import com.dhdigital.lms.activities.LoginActivity;
import com.dhdigital.lms.gcm.model.NotificationAction;
import com.dhdigital.lms.util.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by admin on 24/10/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        NotificationAction notiAction = new NotificationAction();
        notiAction = buildNotificationAction(remoteMessage.getNotification(), notiAction);
        sendNotification(notiAction);
    }

    /**
     * Used to build the Notification action by storing data from broadcasted GCM bundle
     *
     * @param data
     * @param notiAction
     * @return NotificationAction
     */
    private NotificationAction buildNotificationAction(RemoteMessage.Notification data, NotificationAction notiAction) {
        notiAction.setTitle(data.getTitle());
        notiAction.setBody(data.getBody());
        return notiAction;
    }


    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notiAction GCM message received.
     */
    private void sendNotification(NotificationAction notiAction) {
        String module = notiAction.getModule();
        String type = notiAction.getType();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(AppConstants.NAVIGATION, AppConstants.NOTIFICATION);
        intent.putExtra(AppConstants.EXTRA_NOTIFICATION_ACTION, notiAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.user_disp_icon)
                .setContentTitle(notiAction.getTitle())
                .setContentText(notiAction.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) (System.currentTimeMillis() % 10000), notificationBuilder.build());
    }
}
