package com.acamara.scanme.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acamara.scanme.R
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.ui.product.ProductActivity
import com.squareup.picasso.Picasso

/**
*This class is the adapter used with grid_item_view
*to show the recyclerView for the scanned product in the HomeFragment
* 2020/12/06
*/
class ProductRecyclerAdapter () : RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>(){
    private var proList = emptyList<Product_model>()
    class ProductViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.grid_item_view,
            parent,
            false
        )
    ) {
        private var title: TextView?=null
        private var image: ImageView?=null
        private var price: TextView?=null
        private var online: TextView?=null
        private var in_store: TextView?=null

        init {
            title = itemView.findViewById(R.id.upc_or_name)
            image = itemView.findViewById(R.id.not_found_or_found_image)
            price = itemView.findViewById(R.id.price_view)
            online = itemView.findViewById(R.id.online_view)
            in_store = itemView.findViewById(R.id.in_store_view)
        }
        fun bind(pro: Product_model){
            price?.text = pro.price
            if (!pro.foundNear.isNullOrBlank() && pro.foundNear.equals("true")){
                in_store?.visibility = View.VISIBLE
            }

            if (pro.title==null || pro.title=="")
                title?.text = pro.barcode
            else
                title?.text = pro.title.take(35)+"..."
            if (!pro.photo_url.isNullOrBlank()){
                Picasso.get().load(pro.photo_url).into(image)
            }else
                Picasso.get().load("https://tonsmb.org/wp-content/uploads/2014/03/default-placeholder.png").into(image)

            itemView?.setOnClickListener {
                val intent= Intent(itemView.context, ProductActivity::class.java)
                intent.putExtra("barcode", pro.barcode)
                        .putExtra("title",pro.title)
                        .putExtra("place_holder", "https://tonsmb.org/wp-content/uploads/2014/03/default-placeholder.png")
                        .putExtra("image",pro.photo_url)
                        .putExtra("price",pro.price)
                        .putExtra("in_store",pro.foundNear)
                        .putExtra("link",pro.link)
                itemView.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerAdapter.ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(inflater, parent)
    }
    fun setProduct(pro: List<Product_model>){
        this.proList = pro
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductRecyclerAdapter.ProductViewHolder, position: Int) {
        val pro: Product_model = proList[position]
        holder.bind(pro)
    }

    override fun getItemCount(): Int = proList.size
}
