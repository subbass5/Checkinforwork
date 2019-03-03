package com.attendance.checkin.checkinforwork.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.attendance.checkin.checkinforwork.MainAppActivity;
import com.attendance.checkin.checkinforwork.R;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();

        /*
          Check if data needs to be processed by long running job
          For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
         */
        // Check if message contains a data payload.
        //if (remoteMessage.getData().size() > 0) {
        //scheduleJob();
        //}

        // for Notification messages and Data messages (Optional)
        handleNow(notification, data);

        // for Data Messages only
//        if (data.size() > 0) {
//            handleNow(null, data);
//        }
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag(MyJobService.class.getName())
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();
        dispatcher.mustSchedule(myJob);
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     *
     * @param notification
     * @param data
     */
    private void handleNow(RemoteMessage.Notification notification, Map<String, String> data) {
        Log.d(TAG, "Short lived task is done.");
        try {
            sendNotification(notification, data);
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param data FCM message body received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) throws IOException {
        String title;
        String body;
        if (notification != null) {
            title = notification.getTitle();
            body = notification.getBody();
        } else {
            title = data.get("title");
            body = data.get("body");
        }

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = new Intent(this, MainAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setSmallIcon(R.mipmap.ic_launcher);

        String image_url = data.get("image_url");
        if (image_url != null && image_url.length() != 0) {
            URL url = new URL(image_url);
            Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            notificationBuilder.setStyle(
                    new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(body)
            );
        }

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.BLUE, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
