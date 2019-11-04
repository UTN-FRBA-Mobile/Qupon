package edu.utn.mobile.qupon.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.adapters.CuponesRecyclerViewAdapter;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;

public class GalleryFragment extends Fragment implements RecyclerView.OnItemTouchListener {


    private Integer ROWS = 10;
    private Integer COLUMNS = 5;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        List<Cupon> dataSet = new ArrayList<>();
        dataSet.add(new Cupon("McDonalds", "50% de descuento en Sundaes", "https://picsum.photos/300"));
        dataSet.add(new Cupon("Burguer King", "2x1 en Triple Kings", "https://picsum.photos/301"));
        dataSet.add(new Cupon("Wendys", "Llevate una ensalada de regalo", "https://picsum.photos/302"));

        RecyclerView myRecyclerView = root.findViewById(R.id.gallery_fragment_recycler_view);
        myRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(lm);
        myRecyclerView.setAdapter(new CuponesRecyclerViewAdapter(getContext(), dataSet));

        myRecyclerView.addOnItemTouchListener(this); //para una sola accion

        return root;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return true;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        NavController navController = Navigation.findNavController(this.getView());

        //para prevenir el doble click (https://stackoverflow.com/a/53737537)
        if (navController.getCurrentDestination().getId() == R.id.nav_gallery) {
            navController.navigate(R.id.action_nav_gallery_to_nav_slideshow);
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

}