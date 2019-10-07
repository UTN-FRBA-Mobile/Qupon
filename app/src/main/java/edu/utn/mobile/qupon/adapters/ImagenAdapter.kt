package edu.utn.mobile.qupon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.utn.mobile.qupon.CuponItem
import edu.utn.mobile.qupon.R
import kotlinx.android.synthetic.main.fragment_cupon_item.view.*

class ImagenAdapter (
    private val mValues: List<CuponItem>
): RecyclerView.Adapter<ImagenAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_cupon_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuponActual: CuponItem = mValues.get(position);
        Picasso.get().load(cuponActual.urlImagen).into(holder.image)
        holder.nombreComercio.text = cuponActual.nombreComercio
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val image: ImageView = mView.imageView as ImageView
        val nombreComercio: TextView = mView.info_text
    }
}
