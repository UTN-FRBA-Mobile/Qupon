package edu.utn.mobile.qupon;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.RemoteException;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    private AppBarConfiguration mAppBarConfiguration;
    public static NavController navController;
    public static final String NOTIF_CHANNEL_ID = "qupon-channel-id";
    public static final int INTERVALO_MILLIS_POLLING_BEACON = 30000;
    public static final String PREFIJO_BEACON_TIMESTAMP_SHPREF = "beacon_anunciado_";

    protected static final String TAG_BEACONS = "BeaconActivity";
    private BeaconManager mBeaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        createNotificationChannel();

        mBeaconManager = BeaconManager.getInstanceForApplication(this);

        // En este ejemplo vamos a usar el protocolo Eddystone, así que tenemos que definirlo aquí
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.setForegroundBetweenScanPeriod(4000);

        // Bindea esta actividad al BeaconService
        Log.e(TAG_BEACONS, "Pre Bindeo");
        mBeaconManager.bind(this);
        Log.e(TAG_BEACONS, "Post Bindeo");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onBeaconServiceConnect() {
        Log.e(TAG_BEACONS, "onBeaconServiceConnect");
        // Encapsula un identificador de un beacon de una longitud arbitraria de bytes
        ArrayList<Identifier> identifiers = new ArrayList<>();

        // Asignar null para indicar que queremos buscar cualquier beacon
        identifiers.add(null);

        // Representa un criterio de campos utilizados para buscar beacons
        Region region = new Region("AllBeaconsRegion", identifiers);

        try {
            // Ordena al BeaconService empezar a buscar beacons que coincida con el objeto Region pasado
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Especifica una clase que debería ser llamada cada vez que BeaconsService obtiene datos, una vez por segundo por defecto
        mBeaconManager.addRangeNotifier(this);
    }


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.e(TAG_BEACONS, "Buscando beacons...");
        for (Beacon beacon : beacons) {
            Log.d(TAG_BEACONS, "Beacon UUID: " + beacon.getServiceUuid());
            Log.i(TAG_BEACONS, new StringBuilder().append("Beacon detectado (").append(beacon.getId2()).append(") se encuentra a una distancia de ").append(beacon.getDistance()).append(" metros.").toString());
            sendBeaconNotification(beacon);
        }
    }

    private void sendBeaconNotification(Beacon beacon) {
        Identifier beaconId = beacon.getId2();
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        long timestampUltimoAviso = sharedPref.getLong(PREFIJO_BEACON_TIMESTAMP_SHPREF + beaconId, 0);
        long now = System.currentTimeMillis();

        if (timestampUltimoAviso < now - INTERVALO_MILLIS_POLLING_BEACON) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent mainActivityIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Integer notificationId = Integer.decode(beaconId.toString());
            notificationManager.notify(notificationId, new NotificationCompat.Builder(this, MainActivity.NOTIF_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_coupon)
                    .setContentTitle("Qupon encontrado!")
                    .setContentText(beaconId.toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(mainActivityIntent) //Intent que se dispara al tocar notificacion
                    .setAutoCancel(true)
                    .build());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(PREFIJO_BEACON_TIMESTAMP_SHPREF + beaconId, now);
            editor.commit();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
