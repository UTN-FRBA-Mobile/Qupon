package edu.utn.mobile.qupon;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
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

    protected static final String TAG = "RangingActivity";
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


        mBeaconManager = BeaconManager.getInstanceForApplication(this);

        // En este ejemplo vamos a usar el protocolo Eddystone, así que tenemos que definirlo aquí
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));

        // Bindea esta actividad al BeaconService
        Log.e("SDAD","SDASDASD3");
        mBeaconManager.bind(this);
        Log.e("SDAD","SDASDASD4");

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
        Log.e("SDAD","SDASDASD");
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
        Log.e("SDAD","SDASDASD2");
        for(Beacon b : beacons){
            Log.e("beacon", b.getServiceUuid()+"");
        }
        if (beacons.size() > 0) {
            Log.e(TAG, "El primer beacon detectado se encuentra a una distancia de "+beacons.iterator().next().getDistance()+" metros.");
        }
    }
}
