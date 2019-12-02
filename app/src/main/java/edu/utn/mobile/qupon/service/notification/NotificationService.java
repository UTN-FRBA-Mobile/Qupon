package edu.utn.mobile.qupon.service.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.utn.mobile.qupon.MainActivity;
import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.entities.Cupon;

public class NotificationService {

    public static final String NOTIF_CHANNEL_ID = "qupon-channel-id";
    public static final int INTERVALO_MILLIS_POLLING_BEACON = 45000;
    public static final String PREFIJO_BEACON_TIMESTAMP_SHPREF = "beacon_anunciado_";

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void sendBeaconNotification(Context context, Integer notificationId, Cupon cupon) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        long timestampUltimoAviso = sharedPref.getLong(PREFIJO_BEACON_TIMESTAMP_SHPREF + notificationId, 0);
        long now = System.currentTimeMillis();

        if (timestampUltimoAviso < (now - INTERVALO_MILLIS_POLLING_BEACON)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent mainActivityIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_coupon)
                    .setContentTitle("Qupon encontrado!")
                    .setContentText(cupon.desc + " en " + cupon.title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(mainActivityIntent) //Intent que se dispara al tocar notificacion
                    .setAutoCancel(true)
                    .build());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(PREFIJO_BEACON_TIMESTAMP_SHPREF + notificationId, now);
            editor.commit();
        } else {
            Log.d("NotificationService", "Beacon ya encontrado hace " + ((now - timestampUltimoAviso) / 1000) + "\"");
        }
    }


    public static Notification buildForegroundScanNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        return new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Buscando cupones...")
                .setContentIntent(pendingIntent)
                .build();
    }
}
