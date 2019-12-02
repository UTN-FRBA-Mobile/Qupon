package edu.utn.mobile.qupon.ui.gallery.viewHolders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;


public class CuponItemViewHolder extends RecyclerView.ViewHolder {

    public int itemIndex;
    private final OnViewClickListener mListener;
    public TextView title;
    public TextView description;
    public ImageView image;

    public interface OnViewClickListener {
        void onViewClick(View v, int adapterPosition);
    }

    public CuponItemViewHolder(@NonNull View itemView, final OnViewClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        title = itemView.findViewById(R.id.cupon_rv_item_title);
        description = itemView.findViewById(R.id.cupon_rv_item_desc);
        image = itemView.findViewById(R.id.cupon_rv_item_image);
        image.setOnClickListener(view -> mListener.onViewClick(view, itemIndex));

    }

}
