package com.acamara.scanme.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acamara.scanme.R
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.ui.product.ProductActivity
import com.squareup.picasso.Picasso

/**
*This class is the adapter used with listing_item_view
*to show the recyclerView for the product in the HistoryFragment
* 2020/12/06
*/
class ProductRecyclerAdapterListing () : RecyclerView.Adapter<ProductRecyclerAdapterListing.ProductListingViewHolder>(){
    private var proList = emptyList<Product_model>()
    class ProductListingViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.listing_item_view,
            parent,
            false
        )
    ){
        private var title: TextView?=null
        init {
            title = itemView.findViewById(R.id.product_title)
        }
        fun bind(pro: Product_model){
            if (pro.title==null || pro.title=="")
                title?.text = pro.barcode
            else
                title?.text = pro.title

            itemView?.setOnClickListener {
                val intent= Intent(itemView.context, ProductActivity::class.java)
                intent.putExtra("barcode", pro.barcode)
                    .putExtra("title",pro.title)
                    .putExtra("place_holder", "https://tonsmb.org/wp-content/uploads/2014/03/default-placeholder.png")
                    .putExtra("image",pro.photo_url)
                itemView.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductListingViewHolder(inflater, parent)
    }
    fun setProduct(pro: List<Product_model>){
        this.proList = pro
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductListingViewHolder, position: Int) {
        val pro: Product_model = proList[position]
        holder.bind(pro)
    }

    override fun getItemCount(): Int = proList.size
}
