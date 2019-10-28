package edu.utn.mobile.qupon.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.utn.mobile.qupon.R;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.home_map_fragment);
        mapFragment.getMapAsync(this);


        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng bsas = new LatLng(-34.6157437, -58.4244954);

        googleMap.addMarker(new MarkerOptions().position(bsas)
                .title("Mc Donald´s"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-34.6203259,-58.3845563))
                .title("Burger King"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-34.6102944,-58.3956384))
                .title("Wendy´s"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bsas, 12.0f));
        googleMap.getUiSettings().setCompassEnabled(true);
    }

}