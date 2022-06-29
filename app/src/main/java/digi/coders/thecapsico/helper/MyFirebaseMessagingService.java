package digi.coders.thecapsico.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.DashboardActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    String channelId = "channel-01";

    String imageUrl,timestamp;
    long[] pattern = {0, 100, 1000};
    SharedPreferences sPref;
    Vibrator v;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                handleDataMessage(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"),remoteMessage.getData().get("status"));
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }

    private void handleDataMessage(String title ,String desc,String status) {

//        SharedPreferences.Editor editor = sPref.edit();

        try {

                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent fullScreenIntent = new Intent(this, DashboardActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    fullScreenIntent.putExtra("order_id","order_id");

                    Intent intent = new Intent("GPSLocationUpdates");
                    // You can also include some extra data.
                    intent.putExtra("order_id", "order_id");
                    intent.putExtra("amount", "amount");
                    intent.putExtra("id", "id");
                    if(!status.equalsIgnoreCase("simple")) {
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent, imageUrl);
                    }
                } else {
                    // app is in background, show the notification in notification tray
                    Intent fullScreenIntent = new Intent(this, DashboardActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    fullScreenIntent.putExtra("order_id","order_id");



                    Intent intent = new Intent("GPSLocationUpdates");
                    // You can also include some extra data.
                    intent.putExtra("order_id", "order_id");
                    intent.putExtra("amount", "amount");
                    intent.putExtra("id", "id");
                    if(!status.equalsIgnoreCase("simple")) {
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }

                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent, imageUrl);
                    }
                }

        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context,channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context,channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
    MediaPlayer player;
    public void startRingtone(){
//        player=MediaPlayer.create(this, R.raw.ring); //select music file
//        player.setLooping(true); //set looping
//        v.vibrate(pattern,400);
//        player.start();

//        new CountDownTimer(40000,1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onFinish() {
//                // TODO Auto-generated method stub
//                if(player.isPlaying())
//                {
//                    player.stop();
//                    v.cancel();
//                }
//
//
//            }
//        }.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}