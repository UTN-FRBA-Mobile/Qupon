package edu.utn.mobile.qupon.ui.gallery.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import edu.utn.mobile.qupon.R;

public class CuponItemViewHolder extends RecyclerView.ViewHolder {

    int itemIndex;
    public TextView title;
    public TextView description;
    public ImageView image;

    public CuponItemViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.cupon_rv_item_title);
        description = itemView.findViewById(R.id.cupon_rv_item_desc);
        image = itemView.findViewById(R.id.cupon_rv_item_image);
    }
}
