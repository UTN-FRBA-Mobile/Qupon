package edu.utn.mobile.qupon.ui.slideshow;

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
import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
   // private Cupon cupon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        final TextView textView = root.findViewById(R.id.cupon_rv_item_desc);
        textView.setText( ((Cupon)getArguments().getSerializable("cupon")).desc );

        /*
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        return root;
    }
}