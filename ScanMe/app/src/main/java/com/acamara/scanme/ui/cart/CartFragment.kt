package com.acamara.scanme.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.acamara.scanme.R
import kotlinx.android.synthetic.main.fragment_cart.view.*

class CartFragment : Fragment() {

    private lateinit var slideshowViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        //Handler().postDelayed({load(root)},1500)
        root.cart_toolbar.setNavigationOnClickListener { findNavController().navigate(R.id.action_nav_cart_to_nav_home) }
        return root
    }
    fun load(view: View){
        view.cart_progress_bar.visibility = View.GONE
//        view.history_tabs.visibility = View.VISIBLE
    }
}