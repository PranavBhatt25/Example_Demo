package notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import banksoftware.com.banksoftware.R;
import banksoftware.com.banksoftware.SplashActivity;


/**
 * Created by Pranav on 4/1/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Context mContext;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        mContext = getApplicationContext();

        // Log.e(TAG, "From: " + remoteMessage.getFrom());
        // Log.e(TAG, "Notification Message getMessageId: " + remoteMessage.getMessageId());
        Log.e(TAG, "Notification Message getMessage: " + remoteMessage.getData().get("message"));
        // Log.e(TAG, "Notification Message getAction: " + remoteMessage.getData().get("action"));
        //  Log.e(TAG, "Notification Message ImageURL: " + remoteMessage.getData().get("imageUrl"));
        //  Log.e(TAG, "Notification Message from: " + remoteMessage.getData().get("from"));
        //  Log.e(TAG, "Notification Message google.message_id " + remoteMessage.getData().get("google.message_id"));
        //  Log.e(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());

        // String msgaa = remoteMessage.getNotification().getBody();

        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("action"));
    }

    private void sendNotification(String message, String mAction) {

//        1 == Send Request
//        2 == Accept Request
//        4 == Challenge Request send
//        5 == Accept Challenge Request
//        7 == Challenge Response
//        8 == Challenge Vote

        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = null;
//        if (mAction.equals("1")) {
//            Intent notificationIntent = new Intent(this, NavigationDrawer.class);
//            notificationIntent.putExtra("showFriendListFragment", true);
//            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
//
//        } else if (mAction.equals("4")) {
//            Intent intent = new Intent(this, NotificationsActivity.class);
//            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//        } else {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilder.build());
    }
}
