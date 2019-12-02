package edu.utn.mobile.qupon;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;

import java.util.ArrayList;
import java.util.Collection;

import edu.utn.mobile.qupon.repository.CuponesRepository;
import edu.utn.mobile.qupon.service.notification.NotificationService;
import edu.utn.mobile.qupon.entities.Cupon;

import static org.altbeacon.beacon.BeaconParser.EDDYSTONE_UID_LAYOUT;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier, BootstrapNotifier {

    private AppBarConfiguration mAppBarConfiguration;
    public static NavController navController;
    private BeaconManager mBeaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private CuponesRepository cuponesRepository;

    protected static final String TAG_BEACONS = "BeaconActivity";
    public static final int FOREGORUND_SERVICE_NOTIFICATION_ID = 99999;

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

        NotificationService.createNotificationChannel(this);

        cuponesRepository = new CuponesRepository();
        configurarBeaconManager();
    }

    private void configurarBeaconManager() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);

        // Usa el protocolo Eddystone
        mBeaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(EDDYSTONE_UID_LAYOUT));

        //mBeaconManager.setBackgroundScanPeriod(1100);
        //mBeaconManager.setBackgroundBetweenScanPeriod(0);//10 seg
        mBeaconManager.enableForegroundServiceScanning(
                NotificationService.buildForegroundScanNotification(this), FOREGORUND_SERVICE_NOTIFICATION_ID);
        mBeaconManager.setEnableScheduledScanJobs(false);
        mBeaconManager.setForegroundScanPeriod(2000);
        mBeaconManager.setForegroundBetweenScanPeriod(5000);//5 seg

        Log.d(TAG_BEACONS, "setting up background monitoring for beacons and power saving");
        //This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        // Bindea esta actividad al BeaconService
        Log.d(TAG_BEACONS, "Pre Bindeo");
        mBeaconManager.bind(this);
        Log.d(TAG_BEACONS, "Post Bindeo");
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
        mBeaconManager.removeAllMonitorNotifiers();
        mBeaconManager.addRangeNotifier(this);
    }


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.i(TAG_BEACONS, "Buscando beacons...");
        for (Beacon beacon : beacons) {
            Log.d(TAG_BEACONS, "Beacon UUID: " + beacon.getServiceUuid());
            Log.i(TAG_BEACONS, new StringBuilder().append("Beacon detectado (").append(beacon.getId2()).append(") se encuentra a ").append(beacon.getDistance()).append(" metros.").toString());

            Cupon cupon = cuponesRepository.obtenerPorBeaconId(beacon.getId2().toString());
            if(cupon != null){
                NotificationService.sendBeaconNotification(this, Integer.decode(beacon.getId2().toString()), cupon);
            }
        }
    }

    @Override
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG_BEACONS, "did enter region.");
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG_BEACONS, "auto launching MainActivity");

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            Log.d(TAG_BEACONS, "I see a beacon again");
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG_BEACONS, "I no longer see a beacon.");
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG_BEACONS, "Current region state is: " + (state == 1 ? "INSIDE" : "OUTSIDE (" + state + ")"));
    }
}
