package edu.utn.mobile.qupon.ui.gallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.utn.mobile.qupon.R;
import edu.utn.mobile.qupon.ui.gallery.entities.Cupon;
import edu.utn.mobile.qupon.ui.gallery.viewHolders.CuponItemViewHolder;

public class CuponesRecyclerViewAdapter extends RecyclerView.Adapter<CuponItemViewHolder> {

    List<Cupon> mDataSet = new ArrayList<Cupon>();
    Context context;

    public CuponesRecyclerViewAdapter(Context context, List<Cupon> dataSet){
        this.context = context;
        mDataSet = dataSet;
    }


    @NonNull
    @Override
    public CuponItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cupon_recycler_view_item, parent,false);
        CuponItemViewHolder cv = new CuponItemViewHolder(v);
        //cv.touchListener = listener;
        return cv;
    }

    @Override
    public void onBindViewHolder(@NonNull CuponItemViewHolder holder, int position) {
        //holder.itemIndex = position;
        holder.description.setText(mDataSet.get(position).desc);
        holder.title.setText(mDataSet.get(position).title);
        Picasso.with(context).load(mDataSet.get(position).imgResource).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
