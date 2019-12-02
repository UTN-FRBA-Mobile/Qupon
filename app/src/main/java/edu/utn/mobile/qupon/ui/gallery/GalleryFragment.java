package edu.utn.mobile.qupon.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.repository.CuponesRepository;
import edu.utn.mobile.qupon.ui.gallery.adapters.CuponesRecyclerViewAdapter;
import edu.utn.mobile.qupon.entities.Cupon;
import edu.utn.mobile.qupon.ui.gallery.viewHolders.CuponItemViewHolder;

public class GalleryFragment extends Fragment {

    List<Cupon> dataSet;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        dataSet = new CuponesRepository().obtenerTodos();

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