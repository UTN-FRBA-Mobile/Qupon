package edu.utn.mobile.qupon;

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

import java.util.ArrayList;
import java.util.Collection;

import edu.utn.mobile.qupon.service.notification.NotificationService;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    private AppBarConfiguration mAppBarConfiguration;
    public static NavController navController;
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

        NotificationService.createNotificationChannel(this);

        configurarBeaconManager();
    }

    private void configurarBeaconManager() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);

        // Usa el protocolo Eddystone
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
        Log.i(TAG_BEACONS, "Buscando beacons...");
        for (Beacon beacon : beacons) {
            Log.d(TAG_BEACONS, "Beacon UUID: " + beacon.getServiceUuid());
            Log.i(TAG_BEACONS, new StringBuilder().append("Beacon detectado (").append(beacon.getId2()).append(") se encuentra a ").append(beacon.getDistance()).append(" metros.").toString());
            NotificationService.sendBeaconNotification(this, Integer.decode(beacon.getId2().toString()));
        }
    }

}
