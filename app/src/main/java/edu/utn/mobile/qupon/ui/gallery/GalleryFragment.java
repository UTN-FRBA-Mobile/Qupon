package edu.utn.mobile.qupon.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.adapters.CuponesRecyclerViewAdapter;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;
import edu.utn.mobile.qupon.ui.slideshow.SlideshowFragment;

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
        Fragment newFragment2 = new SlideshowFragment();
        FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
        transaction2.add(R.id.cupon_rv_item_image, newFragment2);
        // transaction.addToBackStack(null);
        transaction2.commit();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

}