package com.dhdigital.lms.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dhdigital.lms.R;
import com.dhdigital.lms.activities.LoginActivity;
import com.dhdigital.lms.gcm.model.NotificationAction;
import com.dhdigital.lms.net.HeaderManager;
import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by Shiv on 29/3/16.
 */
public class DHGcmListenerService extends GcmListenerService {
    private static final String TAG = DHGcmListenerService.class.getSimpleName();
    private final String EXTRA_NOTIFICATION_ACTION = "extra_notification_action";
//    public static final String REFERENCE_NO = "reference_no";
//    public static final String MODULE_NAME = "module_name";
//    public static final String TASK_ID = "task_id";


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "@Shiv  onMessageReceived.... ");
        NotificationAction notiAction = new NotificationAction();
        notiAction = buildNotificationAction(data,notiAction);
        sendNotification(notiAction);
    }

    /**
     * Used to build the Notification action by storing data from broadcasted GCM bundle
     * @param data
     * @param notiAction
     * @return NotificationAction
     */
    private NotificationAction buildNotificationAction(Bundle data, NotificationAction notiAction ){
        notiAction.setTitle(data.getString(DHGcmConstants.TITLE));
        notiAction.setBody(data.getString(DHGcmConstants.BODY));
        notiAction.setModule(data.getString(DHGcmConstants.MODULE));
        notiAction.setType(data.getString(DHGcmConstants.TYPE));
        notiAction.setTaskId(data.getString(DHGcmConstants.TASK_ID));
        notiAction.setReferenceNo(data.getString(DHGcmConstants.REFERENCE_NO));
        return notiAction;
    }

    /**
     * This API is used to check if user is logged in to Application
     * @return
     */
    private boolean isUserLoggedIn() {
        if (HeaderManager.isCookieAvailable(this)) {
            return true;
        }
        return false;
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notiAction GCM message received.
     */
    private void sendNotification(NotificationAction notiAction) {
        String module = notiAction.getModule();
        String type = notiAction.getType();
        Intent intent = null;

        if (!isUserLoggedIn()) {
            intent = new Intent(this, LoginActivity.class);
        } else {


        }

        intent.putExtra(EXTRA_NOTIFICATION_ACTION, notiAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notiAction.getTitle())
                .setContentText(notiAction.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) (System.currentTimeMillis() % 10000), notificationBuilder.build());
    }

}
