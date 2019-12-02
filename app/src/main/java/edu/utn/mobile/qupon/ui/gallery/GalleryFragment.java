package edu.utn.mobile.qupon.ui.gallery;

import android.os.Bundle;
import android.util.Log;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.adapters.CuponesRecyclerViewAdapter;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;
import edu.utn.mobile.qupon.ui.gallery.viewHolders.CuponItemViewHolder;

public class GalleryFragment extends Fragment {


    private Integer ROWS = 10;
    private Integer COLUMNS = 5;
    List<Cupon> dataSet;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        dataSet = new ArrayList<>();
        dataSet.add(new Cupon("0x00014567", "McDonalds", "50% de descuento en Sundaes", "https://picsum.photos/300",
                -34.6010156,-58.4287612, new Date()));
        dataSet.add(new Cupon("0x00014598", "Burguer King", "2x1 en Triple Kings", "https://picsum.photos/301",
                -34.6009797,-58.4287612, new Date()));
        dataSet.add(new Cupon("0x00014671", "Wendys", "Llevate una ensalada de regalo", "https://picsum.photos/302",
                -34.6008861,-58.4550259, new Date()));


        RecyclerView myRecyclerView = root.findViewById(R.id.gallery_fragment_recycler_view);
        myRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(lm);
        myRecyclerView.setAdapter(new CuponesRecyclerViewAdapter(getContext(), dataSet, new CuponItemViewHolder.OnViewClickListener() {
            @Override
            public void onViewClick(View v, int adapterPosition) {
                Log.i ( "tag", String.valueOf( adapterPosition) );
                NavController navController = Navigation.findNavController(getView());
                Bundle bundle  = new Bundle();
                Cupon cupon = dataSet.get(adapterPosition);
                bundle.putSerializable("cupon", cupon);
                bundle.putString("desc", "Descripción");
                navController.navigate(R.id.action_nav_gallery_to_nav_slideshow, bundle);
            }
        }));


        return root;
    }


}