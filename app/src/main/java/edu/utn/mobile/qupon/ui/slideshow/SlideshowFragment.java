package edu.utn.mobile.qupon.ui.slideshow;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.entities.Cupon;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public static final int QR_DIMENSION = 530;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        final TextView descripcionView = root.findViewById(R.id.cupon_rv_item_desc);
        Cupon cupon = (Cupon) getArguments().getSerializable("cupon");
        descripcionView.setText(cupon.desc);
        final TextView vencimientoView = root.findViewById(R.id.cupon_rv_item_venc);
        vencimientoView.setText(
                "Válido hasta el " +
                formatter.format(cupon.vencimiento));
        final TextView tituloView = root.findViewById(R.id.cupon_rv_item_title);
        tituloView.setText(cupon.title);

        final ImageView qrImage = root.findViewById(R.id.qr_image);

        QRGEncoder qrgEncoder = new QRGEncoder("http://api.qupon.com/redeem/" + cupon.beaconId,
                null, QRGContents.Type.TEXT, QR_DIMENSION);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("QR", e.toString());
        }

        /*
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                descripcionView.setText(s);
            }
        });
        */
        return root;
    }
}