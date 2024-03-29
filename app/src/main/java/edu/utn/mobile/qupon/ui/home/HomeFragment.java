package edu.utn.mobile.qupon.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.entities.Local;
import edu.utn.mobile.qupon.repository.LocalesRepository;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_CODE = 457;
    private HomeViewModel homeViewModel;
    private LocationManager locationManager;
    private String provider;
    private GoogleMap googleMap;
    private LocalesRepository localesRepository;
    private static LatLng defaultLatLng = new LatLng(-34.6157437, -58.4244954);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationManager = (LocationManager) container.getContext().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), true);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        localesRepository = new LocalesRepository();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.home_map_fragment);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    chequearHabilitarUbicacion(true);
            }
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        for (Local local: localesRepository.obtenerLocales()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(local.lat,local.lon))
                    .title(local.nombre));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 12.0f));
        googleMap.getUiSettings().setCompassEnabled(true);

        chequearHabilitarUbicacion(false);

    }

    private void chequearHabilitarUbicacion(boolean pedirQuePrendaGps) {
        provider = locationManager.getBestProvider(new Criteria(), true);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_CODE);
        } else if (googleMap != null && provider != null) {
            Log.d("Qupon", "Se permitió usar la ubicación");
            //Location userLocation = locationManager.getLastKnownLocation(provider);
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,14.0f));
                                googleMap.setMyLocationEnabled(true);
                            } else {
                                googleMap.setMyLocationEnabled(false);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            googleMap.setMyLocationEnabled(false);
                            e.printStackTrace();
                        }
                    });
        } else if(pedirQuePrendaGps){
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    Log.i("QUPON", "PERMISSION RESULT CALLBACK");
                    String bestProvider = locationManager.getBestProvider(new Criteria(), true);
                    if (bestProvider != null) {
                        Location userLocation = locationManager.getLastKnownLocation(bestProvider);
                        if (userLocation != null) {
                            LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                }

            } else {
                googleMap.setMyLocationEnabled(false);
            }
            return;
        }
    }
}