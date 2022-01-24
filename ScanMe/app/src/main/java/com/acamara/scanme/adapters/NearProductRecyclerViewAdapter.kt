package com.acamara.scanme.adapters

import android.content.Intent
import android.media.Image
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
*This class is the adapter used with near_product_item
*to show the recyclerView for the product that are locally available for purchase
* 2020/12/06
*/
class NearProductRecyclerViewAdapter () : RecyclerView.Adapter<NearProductRecyclerViewAdapter.ProductListingViewHolder>(){
    private var proList = emptyList<Product_model>()
    class ProductListingViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.near_product_item,
            parent,
            false
        )
    ){
        private var title: TextView?=null
        private var image: ImageView?=null
        init {
            title = itemView.findViewById(R.id.product_name)
            image = itemView.findViewById(R.id.product_image)
        }
        fun bind(pro: Product_model){
                title?.text = pro.title
                Picasso.get().load(pro.photo_url).into(image)

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
