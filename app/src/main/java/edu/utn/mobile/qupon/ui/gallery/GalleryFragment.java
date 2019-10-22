package edu.utn.mobile.qupon.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.adapters.cupones_recycler_view_adapter;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;

public class GalleryFragment extends Fragment {


    private Integer ROWS = 10;
    private Integer COLUMNS = 5;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

    galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel.class);
    View root = inflater.inflate(R.layout.fragment_gallery, container, false);

    List<Cupon> dataSet = new ArrayList<Cupon>();
    dataSet.add(new Cupon("McDonals", "DESC", "https://picsum.photos/300"));
    dataSet.add(new Cupon("Burguer King", "DESC", "https://picsum.photos/301"));
    dataSet.add(new Cupon("Wendys", "DESC", "https://picsum.photos/302"));

        RecyclerView myRecyclerView = root.findViewById(R.id.gallery_fragment_recycler_view);
        myRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(lm);
        myRecyclerView.setAdapter(new cupones_recycler_view_adapter(getContext(), dataSet));






        return root;
    }
}